/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package murugusmsdbui;

/**
 *
 * @author smbuthia
 */
// <editor-fold defaultstate="collapsed" desc="imports">
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ajwcc.pduUtils.test.integration.ReadMessages;
import org.smslib.AGateway;
import org.smslib.AGateway.GatewayStatuses;
import org.smslib.AGateway.Protocols;
import org.smslib.GatewayException;
import org.smslib.IGatewayStatusNotification;
import org.smslib.IInboundMessageNotification;
import org.smslib.IOutboundMessageNotification;
import org.smslib.InboundMessage;
import org.smslib.Library;
import org.smslib.Message.MessageTypes;
import org.smslib.OutboundMessage;
import org.smslib.SMSLibException;
import org.smslib.Service;
import org.smslib.TimeoutException;
import org.smslib.modem.SerialModemGateway;
// </editor-fold>
public class SMSMessenger implements Runnable {

    private String recepientPhoneNumber;
    private Database database;
    ErrorHandler eh = new ErrorHandler();
    Properties props = new Properties();

    public String getRecepientPhoneNumber() {
        return recepientPhoneNumber;
    }

    public void setRecepientPhoneNumber(String recepientPhoneNumber) {
        this.recepientPhoneNumber = recepientPhoneNumber;
    }

    public void doIt() throws Exception {
        // Define a list which will hold the read messages.
        List<InboundMessage> msgList;
        // Create the notification callback method for inbound & status report
        // messages.
        InboundNotification inboundNotification = new InboundNotification();
        OutboundNotification outboundNotification = new OutboundNotification();
        //Create the notification callback method for gateway statuses.
        GatewayStatusNotification statusNotification = new GatewayStatusNotification();

        try {
//            System.out.println("Example: Read messages from a serial gsm modem.");
//            System.out.println(Library.getLibraryDescription());
//            System.out.println("Version: " + Library.getLibraryVersion());
            props.load(new FileInputStream("C:\\Users\\smbuthia\\Desktop\\MuruguSMSDBUI\\src\\murugusmsdbui\\config.properties"));
            // Create the Gateway representing the serial GSM modem.
            SerialModemGateway gateway = new SerialModemGateway(
                    props.getProperty("id"),
                    props.getProperty("comPort"),
                    Integer.parseInt(props.getProperty("baudRate")),
                    props.getProperty("manufacturer"),
                    props.getProperty("model"));
            // Set the modem protocol to PDU (alternative is TEXT). PDU is the default, anyway...
            gateway.setProtocol(Protocols.PDU);
            // Do we want the Gateway to be used for Inbound messages?
            gateway.setInbound(true);
            // Do we want the Gateway to be used for Outbound messages?
            gateway.setOutbound(true);
            // Let SMSLib know which is the SIM PIN.
            gateway.setSimPin("5493");
            // Set up the notification methods.
            Service.getInstance().setInboundMessageNotification(inboundNotification);
            Service.getInstance().setOutboundMessageNotification(outboundNotification);
            Service.getInstance().setGatewayStatusNotification(statusNotification);

            // Add the Gateway to the Service object.
            Service.getInstance().addGateway(gateway);
            Service.getInstance().startService();

            System.out.println();
            System.out.println("Modem Information:");
            System.out.println("  Manufacturer: " + gateway.getManufacturer());
            System.out.println("  Signal Level: " + gateway.getSignalLevel() + " dBm");
            System.out.println();

//             Sleep now. Emulate real world situation and give a chance to the notifications
//             methods to be called in the event of message or voice call reception.
            System.out.println("Now Sleeping - Hit <enter> to stop service.");
            System.in.read();
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
            //eh.simpleMessageCreator("An issue occured. Failed to start sms service.");
            //System.exit(0);
        } finally {
            Service.getInstance().stopService();
        }
    }

    public void run() {
        try {
            doIt();
        } catch (Exception ex) {
            Logger.getLogger(SMSMessenger.class.getName()).log(Level.SEVERE, null, ex);
            eh.simpleMessageCreator(ex.getMessage());
        }
    }

    public class InboundNotification implements IInboundMessageNotification {

        @Override
        public void process(AGateway gateway, MessageTypes msgType, InboundMessage msg) {
            if (msgType == MessageTypes.INBOUND) {
                System.out.println(">>> New Inbound message detected from Gateway: " + gateway.getGatewayId());
            } else if (msgType == MessageTypes.STATUSREPORT) {
                System.out.println(">>> New Inbound Status Report message detected from Gateway: " + gateway.getGatewayId());
            }
            System.out.println(msg);
            //process message
            database = new Database();
            database.readDB(msg.getText().toString());

            sendMessage(msg.getOriginator(), database.getResponse());

            try {
                Service.getInstance().deleteMessage(msg);
            } catch (TimeoutException ex) {
                Logger.getLogger(ReadMessages.class.getName()).log(Level.SEVERE, null, ex);
                eh.simpleMessageCreator(ex.getMessage());
            } catch (GatewayException ex) {
                Logger.getLogger(ReadMessages.class.getName()).log(Level.SEVERE, null, ex);
                eh.simpleMessageCreator(ex.getMessage());
            } catch (IOException ex) {
                Logger.getLogger(ReadMessages.class.getName()).log(Level.SEVERE, null, ex);
                eh.simpleMessageCreator(ex.getMessage());
            } catch (InterruptedException ex) {
                Logger.getLogger(ReadMessages.class.getName()).log(Level.SEVERE, null, ex);
                eh.simpleMessageCreator(ex.getMessage());
            }
        }
    }

    public class OutboundNotification implements IOutboundMessageNotification {

        @Override
        public void process(AGateway gateway, OutboundMessage msg) {
            System.out.println("Outbound handler called from Gateway: " + gateway.getGatewayId());
            System.out.println(msg);
        }
    }

    public class GatewayStatusNotification implements IGatewayStatusNotification {

        @Override
        public void process(AGateway gateway, GatewayStatuses oldStatus, GatewayStatuses newStatus) {
//            System.out.println(">>> Gateway Status change for " + gateway.getGatewayId() + ", OLD: " + oldStatus + " -> NEW: " + newStatus);
        }
    }

    public void sendMessage(String recepient, String strMsg) {

        OutboundMessage msg = new OutboundMessage(recepient, strMsg + ". Thank you. Murugu Herbal Clinic. www.muruguclinic.com");
        try {
            Service.getInstance().sendMessage(msg);
        } catch (SMSLibException ex) {
            Logger.getLogger(ReadMessages.class.getName()).log(Level.SEVERE, null, ex);
            eh.simpleMessageCreator(ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(ReadMessages.class.getName()).log(Level.SEVERE, null, ex);
            eh.simpleMessageCreator(ex.getMessage());
        } catch (InterruptedException ex) {
            Logger.getLogger(ReadMessages.class.getName()).log(Level.SEVERE, null, ex);
            eh.simpleMessageCreator(ex.getMessage());
        }
    }
}
