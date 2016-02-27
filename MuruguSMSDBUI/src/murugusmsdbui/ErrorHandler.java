/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package murugusmsdbui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author smbuthia
 */
public class ErrorHandler {

    protected void simpleMessageCreator(String message) {
        JOptionPane.showMessageDialog(new JFrame(), message);
    }
}
