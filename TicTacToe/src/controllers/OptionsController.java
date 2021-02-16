package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class OptionsController {

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
        // Returns you to what you were at
    }
}
