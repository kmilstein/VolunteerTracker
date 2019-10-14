
package views;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import models.Volunteer;

/**
 * FXML Controller class
 *
 * @author Ksenia
 */
public class NewUserViewController implements Initializable {

    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField phoneTextField;
    @FXML
    private DatePicker birthday;
    @FXML
    private Label errMsgLabel;
    
    /**
     * This method will read from the scene and try to create a new instance of a Volunteer
     * If a volunteer was successfully created, it is updated in the database.
     */
    
    public void saveVolunteerButtonPushed(ActionEvent event) {
        try {
            Volunteer volunteer = new Volunteer(firstNameTextField.getText(), lastNameTextField.getText(), phoneTextField.getText(), birthday.getValue());
            errMsgLabel.setText("");
            volunteer.insertIntoDataBase();
        } catch (Exception e) {
            errMsgLabel.setText(e.getMessage());
        }
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errMsgLabel.setText("");
    }    
    
}
