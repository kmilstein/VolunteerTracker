package views;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import models.Volunteer;

/**
 * FXML Controller class
 *
 * @author Ksenia
 */
public class NewUserViewController implements Initializable, ControllerClass {

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
    @FXML
    private Label headerLabel;
    @FXML
    private ImageView imageView;

    private File imageFile;
    private boolean imageFileChanged;
    private Volunteer volunteer;

    @FXML
    private PasswordField pwField;
    @FXML
    private PasswordField confirmPwField;

    /**
     * This method will change to the TableView without adding a user. All data
     * in the form will be post
     */
    @FXML
    public void cancelButtonPushed(ActionEvent event) throws IOException {
        SceneChanger.changeScenes("VolunteerTableView.fxml", "All Volunteers");
    }

    /**
     * This method opens a FileChooser object to allow the user to browse for a
     * new image file.When that is completed, it will update the view to the a
     * new image.
     *
     * @param event
     */
    @FXML
    public void chooseImageButtonPushed(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Item");

        FileChooser.ExtensionFilter jpgFilter = new FileChooser.ExtensionFilter("Image File (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter("Image File (*.png)", "*.png");

        fileChooser.getExtensionFilters().addAll(jpgFilter, pngFilter);

        String userDirectoryString = System.getProperty("user.home") + "\\Pictures";
        File userDirectory = new File(userDirectoryString);

        if (!userDirectory.canRead()) {
            userDirectory = new File(System.getProperty("user.home"));
        }
        fileChooser.setInitialDirectory(userDirectory);

        File tempImageFile = fileChooser.showOpenDialog(stage);

        if (tempImageFile != null) {
            imageFile = tempImageFile;

            if (imageFile != null) {
                try {
                    BufferedImage bufferedImage = ImageIO.read(imageFile);
                    Image img = SwingFXUtils.toFXImage(bufferedImage, null);
                    imageView.setImage(img);
                    imageFileChanged = true;
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        birthday.setValue(LocalDate.now().minusYears(18));
        imageFileChanged = false;
        errMsgLabel.setText("");

        try {
            imageFile = new File("./src/images/default.jpg");
            BufferedImage bufferedImage = ImageIO.read(imageFile);
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            imageView.setImage(image);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * This method will read from the scene and try to create a new instance of
     * a Volunteer If a volunteer was successfully created, it is updated in the
     * database.
     */
    @FXML
    public void saveVolunteerButtonPushed(ActionEvent event) {
        if (validPassword()) {
            try {
                if (volunteer != null) {
                    updateVolunteer();
                    volunteer.updateVolunteerDataBase();
                } else {
                    if (imageFileChanged) {
                        volunteer = new Volunteer(firstNameTextField.getText(), lastNameTextField.getText(),
                                phoneTextField.getText(), birthday.getValue(), imageFile, pwField.getText());
                    } else {
                        volunteer = new Volunteer(firstNameTextField.getText(), lastNameTextField.getText(),
                                phoneTextField.getText(), birthday.getValue(), pwField.getText());
                    }

                    errMsgLabel.setText("");
                    volunteer.insertIntoDataBase();
                }

                SceneChanger.changeScenes("VolunteerTableView.fxml", "All Volunteers");

            } catch (Exception e) {
                errMsgLabel.setText(e.getMessage());
                System.err.println(e);
            }
        }
    }

    @Override
    public void preloadData(Volunteer volunteer) {
        firstNameTextField.setText(volunteer.getFirstName());
        lastNameTextField.setText(volunteer.getLastName());
        birthday.setValue(volunteer.getBirthday());
        phoneTextField.setText(volunteer.getPhoneNumber());
        headerLabel.setText("Edit Volunteer");

        try {
            String imgLocation = ".\\src\\images\\" + volunteer.getImageFile().getName();
            imageFile = new File(imgLocation);
            BufferedImage bufferedImage = ImageIO.read(imageFile);
            Image img = SwingFXUtils.toFXImage(bufferedImage, null);
            imageView.setImage(img);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * This method will read from the GUI fields and update the volunteer object
     */
    private void updateVolunteer() throws IOException {
        volunteer.setFirstName(this.firstNameTextField.getText());
        volunteer.setLastName(this.lastNameTextField.getText());
        volunteer.setPhoneNumber(this.phoneTextField.getText());
        volunteer.setBirthday(this.birthday.getValue());
        volunteer.setImageFile(imageFile);
        volunteer.copyImageFile();
    }

    /**
     * This method will validate that the passwords match
     */
    private boolean validPassword() {
        if (pwField.getText().length() < 5) {
        errMsgLabel.setText("Password must be greater than 5 characters in length");
            return false;
        }
        return (this.pwField.getText().equals(this.confirmPwField.getText()));
    }
}
