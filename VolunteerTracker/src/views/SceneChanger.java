package views;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Volunteer;

/**
 *
 * @author Ksenia
 */
public class SceneChanger {
    
    private static Stage currentStage;
    
    public static void setStage(Stage theStage) {
        currentStage = theStage;
    }
    
    /**
     * This method will accept the title of the new scene, the .fxml file name
     * for the view and the ActionEvent that triggered the change
     */
    public static void changeScenes(String viewName, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(SceneChanger.class.getResource(viewName));
        Parent parent = loader.load();
        
        Scene scene = new Scene(parent);
        
        //Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        currentStage.setTitle(title);
        currentStage.setScene(scene);
        currentStage.show();
    }
    /**
     * This method will change scenes and preload the next scene with a Volunteer object
     * @param viewName
     * @param title
     * @param volunteer
     * @param controllerClass
     * @throws java.io.IOException
     */
    public static void changeScenes(String viewName, String title, Volunteer volunteer, 
            ControllerClass controllerClass) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(SceneChanger.class.getResource(viewName));
        Parent parent = loader.load();
        
        Scene scene = new Scene(parent);
        
        controllerClass = loader.getController();
        controllerClass.preloadData(volunteer);
        
        currentStage.setTitle(title);
        currentStage.setScene(scene);
        currentStage.show();
    }
}