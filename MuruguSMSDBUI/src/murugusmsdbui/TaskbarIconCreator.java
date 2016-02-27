/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package murugusmsdbui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 *
 * @author smbuthia
 */
public class TaskbarIconCreator implements Runnable {

    private Thread t;
    private PopupMenu popup;
    private MenuItem startSMSResponder;
    private MenuItem stopSMSResponder;
    ErrorHandler eh = new ErrorHandler();

    public TaskbarIconCreator(final Thread t) {
        this.t = t;
    }

    protected void createTaskBarIcon() {


        //Check the SystemTray support
        if (!SystemTray.isSupported()) {
//                System.out.println("SystemTray is not supported");
            eh.simpleMessageCreator("SystemTray is not supported");
            return;
        }
        popup = new PopupMenu();
        final TrayIcon trayIcon =
                new TrayIcon(createImage("resources/murugu_icon.png", "tray icon"));
        final SystemTray tray = SystemTray.getSystemTray();

        // Create a popup menu components
        startSMSResponder = new MenuItem("Start SMS Responder");
        stopSMSResponder = new MenuItem("Stop SMS Responder");
        MenuItem editSMSDatabase = new MenuItem("Edit SMS Database");
        MenuItem exitItem = new MenuItem("Exit");
        //Add components to popup menu


        popup.add(startSMSResponder);
        popup.add(stopSMSResponder);
        popup.addSeparator();
        popup.add(editSMSDatabase);
        popup.addSeparator();
        popup.add(exitItem);
        popup.addSeparator();

        startSMSResponder.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (!t.isAlive()) {
                    t.start();
                }
            }
        });
        stopSMSResponder.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (t.isAlive()) {
                    t.interrupt();
                }
            }
        });
        editSMSDatabase.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                MuruguSMSDBUIApp.getApplication().getMainFrame().setVisible(true);
                tray.remove(trayIcon);
            }
        });
        exitItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                tray.remove(trayIcon);
                System.exit(0);
            }
        });
        trayIcon.setPopupMenu(popup);

        try {
            tray.add(trayIcon);
        } catch (Exception e) {
//                System.out.println("TrayIcon could not be added.");
            eh.simpleMessageCreator("TrayIcon could not be added.");

            return;
        }



        new Thread(this).start();
    }

    protected static Image createImage(String path, String description) {
        URL imageURL = MuruguSMSDBUIView.class.getResource(path);


        if (imageURL
                == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }

    public void run() {
        while (true) {
            if (t.isAlive()) {
                startSMSResponder.setEnabled(false);
                stopSMSResponder.setEnabled(true);
            } else {
                startSMSResponder.setEnabled(true);
                stopSMSResponder.setEnabled(false);
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                Logger.getLogger(TaskbarIconCreator.class.getName()).log(Level.SEVERE, null, ex);
                eh.simpleMessageCreator(ex.getMessage());
            }
        }
    }
}
