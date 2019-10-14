/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import models.Volunteer;

/**
 * FXML Controller class
 *
 * @author Ksenia
 */
public class VolunteerTableViewController implements Initializable {

    @FXML 
    private TableView<Volunteer> volunteerTable;
    @FXML
    private TableColumn<Volunteer, Integer> volunteerIDColumn;
    @FXML
    private TableColumn<Volunteer, String> FirstNameColumn;
    @FXML
    private TableColumn<Volunteer, String> lastNameColumn;
    @FXML
    private TableColumn<Volunteer, String> phoneNumberColumn;
    @FXML
    private TableColumn<Volunteer, LocalDate> birthdayColumn;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
