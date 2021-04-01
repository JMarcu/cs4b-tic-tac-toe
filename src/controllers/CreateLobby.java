package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import models.MusicPlayer;
import models.SceneCallback.ReturnToCallback;

public class CreateLobby {
    @FXML private Button returnBtn;
    @FXML private Label title;
    @FXML private ImageView icon;
    @FXML private Button joinBtn;
    @FXML private TextField playerName;
    @FXML private Button markerBtn;
    @FXML private ImageView marker;
    @FXML private MenuButton timeMenu;
    @FXML private Button createBtn;
    @FXML private AnchorPane root;

    private ReturnToCallback returnCB;
    private MusicPlayer musicSFX;

    public CreateLobby(){
        icon.setImage(new Image("/assets/images/splash/globe.png"));
    }

    /** Sets the default state of the view's interactive elements. */
    @FXML
    void initialize(){
        //Load external style sheets
        root.getStylesheets().add(getClass().getResource("/styles/color-theme.css").toExternalForm());
        root.getStylesheets().add(getClass().getResource("/styles/create-lobby.css").toExternalForm());
    }

    /** Closes the splash screen when the element is clicked on. */
    @FXML
    private void joinLobbyAction(ActionEvent e){

    }
}
