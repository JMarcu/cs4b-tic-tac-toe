package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import models.SceneCallback.ReturnToCallback;
import models.SceneCallback.LaunchMainMenuCallback;

public class OptionsController {

    private LaunchMainMenuCallback mainMenuCB;
    private ReturnToCallback returnToCB;

    @FXML private GridPane root;

    public GridPane getRoot(){ return this.root; }

    @FXML protected void ReturnButtonClicked(ActionEvent event) {
        returnToCB.returnTo();
    }

    @FXML protected void SFXButtonClicked(ActionEvent event) {
        // changes sound effects volume
    }

    @FXML protected void VolumeButtonClicked(ActionEvent event) {
        // Lets you change the volume
        // CycleVolume();
    }

    @FXML protected void RestartButtonClicked(ActionEvent event) {
        // Restarts the current game
        // Restart()
    }

    @FXML protected void MainMenuButtonClicked(ActionEvent event) {
        // Returns you to main menu (from game)
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
