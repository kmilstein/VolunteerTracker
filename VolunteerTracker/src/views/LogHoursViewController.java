/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javafx.scene.control.TextField;
import models.Volunteer;
import java.sql.*;

/**
 * FXML Controller class
 *
 * @author Ksenia
 */
public class LogHoursViewController implements Initializable, ControllerClass {

    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField hoursWorkedTextField;
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
    }    

    @Override
    public void preloadData(Volunteer volunteer) {
        this.volunteer = volunteer;
        volunteerIDLabel.setText(Integer.toString(volunteer.getVolunteerID()));
        firstNameLabel.setText(volunteer.getFirstName());
        lastNameLabel.setText(volunteer.getLastName());
        datePicker.setValue(LocalDate.now());
        hoursWorkedTextField.setText("8");
    }
    
    /**
     * This method will read/validate the inputs and store the information in a hoursWorked table
     */
    public void saveButtonPushed(ActionEvent event) {
        try {
            volunteer.logHours(datePicker.getValue(), Integer.parseInt(hoursWorkedTextField.getText()));
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
