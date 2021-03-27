package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.SceneCallback.LaunchGameCallback;
import models.SceneCallback.LaunchMainMenuCallback;
import models.SceneCallback.ReturnToCallback;

public class CreateLobby {
    @FXML private Label title;
    @FXML private Label subtitle;
    @FXML private Button returnBtn;
    @FXML private ImageView icon;
    @FXML private Button makeBtn;
    @FXML private Button joinBtn;
    @FXML private HBox buttonsBox;
    @FXML private VBox root;


    public CreateLobby(){
        icon.setImage(new Image("/assets/images/splash/globe.png"));
    }

    /** Sets the default state of the view's interactive elements. */
    @FXML
    void initialize(){
        //Load external style sheets
        root.getStylesheets().add(getClass().getResource("/styles/color-theme.css").toExternalForm());
        root.getStylesheets().add(getClass().getResource("/styles/splash-screen.css").toExternalForm());
    }

}
