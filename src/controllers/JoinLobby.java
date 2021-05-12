package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.AnchorPane;
import models.MusicPlayer;
import models.SceneCallback.LaunchLobbyFinderCallback;
import models.SceneCallback.ReturnToCallback;

public class JoinLobby {
    private MusicPlayer musicSFX;

    @FXML private Button returnBtn;
    @FXML private Label title;
    @FXML private MenuButton timeMenu;
    @FXML private Button refreshBtn;
    @FXML private ListView<String> lobbyView;
    @FXML private AnchorPane root;

    private ReturnToCallback returnToCB;
    private LaunchLobbyFinderCallback launchLobbyFinderCB;

    /** Sets the default state of the view's interactive elements. */
    @FXML
    void initialize(){
        //Load external style sheets
        root.getStylesheets().add(getClass().getResource("/styles/color-theme.css").toExternalForm());
        root.getStylesheets().add(getClass().getResource("/styles/join-lobby.css").toExternalForm());

        musicSFX = new MusicPlayer();
    }

    @FXML protected void returnButtonClicked(ActionEvent event) {
        //System.out.println("currentColor: " + currentColor);
        // musicSFX.playSFX(MusicPlayer.Track.exitMenu);

        // returnCB.returnTo();
    }

    public void setLaunchLobbyFinderCallback(LaunchLobbyFinderCallback launchLobbyFinderCB){ this.launchLobbyFinderCB = launchLobbyFinderCB;}

    public void setReturnCB(ReturnToCallback returnToCB){
        this.returnToCB = returnToCB;
    }
}
