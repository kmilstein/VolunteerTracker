package views;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Volunteer;
import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

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
    private TableColumn<Volunteer, String> firstNameColumn;
    @FXML
    private TableColumn<Volunteer, String> lastNameColumn;
    @FXML
    private TableColumn<Volunteer, String> phoneNumberColumn;
    @FXML
    private TableColumn<Volunteer, LocalDate> birthdayColumn;
    @FXML
    private Button editVolunteerButton;
    @FXML
    private Button logHoursButton;

    /**
     * If the edit button is pushed, pass the selected Volunteer to the
     * NewUserView and preload it with the data.
     */
    @FXML
    public void editButtonPushed(ActionEvent event) throws IOException {
        Volunteer volunteer = volunteerTable.getSelectionModel().getSelectedItem();
        NewUserViewController npvc = new NewUserViewController();

        SceneChanger.changeScenes("NewUserView.fxml", "Edit Volunteer", volunteer, npvc);
    }

    /**
     * This method will switch to the NewUserView scene when the button is
     * pushed
     */
    @FXML
    public void newVolunteerPush(ActionEvent event) throws IOException {
        SceneChanger.changeScenes("NewUserView.fxml", "Create New Volunteer");
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        editVolunteerButton.setDisable(true);
        logHoursButton.setDisable(true);

        volunteerIDColumn.setCellValueFactory(new PropertyValueFactory<>("volunteerID"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        birthdayColumn.setCellValueFactory(new PropertyValueFactory<>("birthday"));

        try {
            loadVolunteers();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * This method will load the volunteers from the database and load them into
     * the TableView object
     *
     * @throws java.sql.SQLException
     */
    public void loadVolunteers() throws SQLException {
        ObservableList<Volunteer> volunteers = FXCollections.observableArrayList();

        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/volunteer?autoReconnect=true&useSSL=false",
                    "root", "MySQLPassword1");
            statement = conn.createStatement();

            resultSet = statement.executeQuery("SELECT * FROM volunteers");

            while (resultSet.next()) {
                Volunteer newVolunteer = new Volunteer(resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("phoneNumber"),
                        resultSet.getDate("birthday").toLocalDate(),
                        resultSet.getString("password"));
                newVolunteer.setVolunteerID(resultSet.getInt("volunteerID"));
                newVolunteer.setImageFile(new File(resultSet.getString("imageFile")));
                volunteers.add(newVolunteer);
            }
            volunteerTable.getItems().addAll(volunteers);
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            if (conn != null) {
                conn.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        }
    }
    
    /**
     * This method will open the loghours view 
     */
    public void logHoursButtonPushed(ActionEvent event) throws IOException {
        Volunteer volunteer = volunteerTable.getSelectionModel().getSelectedItem();
        LogHoursViewController lhvc = new LogHoursViewController();
        SceneChanger.changeScenes("LogHoursView.fxml", "Log Hours View", volunteer, lhvc);
    }

    /**
     * If a user has been selected in the table, enable edit button
     */
    @FXML
    public void volunteerSelected() {
        editVolunteerButton.setDisable(false);
        logHoursButton.setDisable(false);
    }
}
