package views;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Blob;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.PasswordGenerator;

public class LoginViewController implements Initializable {
    
    @FXML
    private TextField volunteerIDTextfield;
    @FXML
    private PasswordField pwField;
    @FXML
    private Label errMsgLabel;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errMsgLabel.setText("");
    }    
    
    /**
     * Query the database with the volunteerID provided, get the salt and 
     * encrypted password in the database
     */
    public void loginButtonPushed(ActionEvent event) throws IOException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        
        int volunteerNum = Integer.parseInt(volunteerIDTextfield.getText());
        
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/volunteer?autoReconnect=true&useSSL=false",
                    "root", "MySQLPassword1");
            
            String sql = "SELECT password, salt FROM volunteers WHERE volunteerID = ?";
            
            ps = conn.prepareStatement(sql);
            
            ps.setInt(1, volunteerNum);
            
            resultSet = ps.executeQuery();
            
            String dbPassword = null;
            byte[] salt = null;
            while (resultSet.next()) {
                dbPassword = resultSet.getString("password");
                
                Blob blob = resultSet.getBlob("salt");
                int blobLength = (int)blob.length();
                salt = blob.getBytes(1, blobLength);
            }
            
            String userPW = PasswordGenerator.getSHA512Password(pwField.getText(), salt);
            
            if (userPW.equals(dbPassword)) {
                SceneChanger.changeScenes("VolunteerTableView.fxml", "All Volunteers");
            } else {
                errMsgLabel.setText("The VolunteerPD and password do not match");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
