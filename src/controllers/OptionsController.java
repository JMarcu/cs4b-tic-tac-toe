package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import models.ReturnToCallback;
import models.SceneCallback.LaunchMainMenuCallback;

public class OptionsController {

    private LaunchMainMenuCallback mainMenuCB;
    private ReturnToCallback returnToCB;

    @FXML private GridPane root;

    public GridPane getRoot(){ return this.root; }

    @FXML protected void ReturnButtonClicked(ActionEvent event) {
        returnToCB.returnToCB();
    }

    @FXML protected void BackgroundButtonClicked(ActionEvent event) {
        // Lets you change your background... cycle through?
        // CycleBackground();
    }

    @FXML protected void VolumeButtonClicked(ActionEvent event) {
        // Lets you change the volume
        // CycleVolume();
    }

    @FXML protected void RestartButtonClicked(ActionEvent event) {
        // Restarts the current game
        // Restart()
    }

    @FXML protected void StatsButtonClicked(ActionEvent event) {
        // Shows you the Stats Board
    }

    @FXML protected void ExitButtonClicked(ActionEvent event) {
        this.mainMenuCB.launchMainMenu();
    }

    public void setMainMenuCB(LaunchMainMenuCallback mainMenuCB){
        this.mainMenuCB = mainMenuCB;
    }

    public void setReturnToCB(ReturnToCallback returnToCB){
        this.returnToCB = returnToCB;
    }
}
