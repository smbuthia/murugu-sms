/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package murugusmsdbui;
// <editor-fold defaultstate="collapsed" desc="imports">
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
// </editor-fold>
/**
 *
 * @author smbuthia
 */
public class Database {

    private Connection conn = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    private String response = "Sorry send message in correct format. ";
    ErrorHandler eh = new ErrorHandler();

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void readDB(String text) {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/murugusms", "root", "r00t");

            preparedStatement = conn.prepareStatement("SELECT response FROM sms WHERE text = '" + text + "' LIMIT 1");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                setResponse(resultSet.getString("response"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            eh.simpleMessageCreator(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            eh.simpleMessageCreator(ex.getMessage());
        }
    }
}
