package views;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import models.Volunteer;
import java.sql.*;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

/**
 * FXML Controller class
 *
 * @author Ksenia
 */
public class LogHoursViewController implements Initializable, ControllerClass {

    @FXML
    private DatePicker datePicker;
    @FXML
    private Spinner hoursWorkedSpinner;
    @FXML
    private Label volunteerIDLabel;
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label errMsgLabel;
    
    private Volunteer volunteer;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errMsgLabel.setText("");
        SpinnerValueFactory<Integer> valueFactory = 
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 18, 8);
        hoursWorkedSpinner.setValueFactory(valueFactory);
    }    

    @Override
    public void preloadData(Volunteer volunteer) {
        this.volunteer = volunteer;
        volunteerIDLabel.setText(Integer.toString(volunteer.getVolunteerID()));
        firstNameLabel.setText(volunteer.getFirstName());
        lastNameLabel.setText(volunteer.getLastName());
        datePicker.setValue(LocalDate.now());
    }
    
    /**
     * This method will read/validate the inputs and store the information in a hoursWorked table
     */
    public void saveButtonPushed(ActionEvent event) {
        try {
            volunteer.logHours(datePicker.getValue(), (int) hoursWorkedSpinner.getValue());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            errMsgLabel.setText(e.getMessage());
        }
    }
    
        /**
     * This will return the user to the table of all volunteers
     */
    public void cancelButtonPushed(ActionEvent event) throws IOException {
        SceneChanger.changeScenes("VolunteerTableView.fxml", "All Volunteers");
    }
}
