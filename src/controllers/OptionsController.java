package controllers;

import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import models.SceneCallback.ReturnToCallback;
import services.AuthService;
import models.SceneCallback.LaunchLoginCallback;
import models.SceneCallback.LaunchMainMenuCallback;
import models.MusicPlayer;
import models.Player;

public class OptionsController {

    private LaunchLoginCallback launchLoginCB;
    private LaunchMainMenuCallback mainMenuCB;
    private ReturnToCallback returnToCB;
    private MusicPlayer music;
    private Player player;

    @FXML
    private Button RestartButton;

    @FXML
    private Button MainMenuButton;

    public void acceptCaller(String caller, MusicPlayer music)
    {
        //Load external style sheets.
        root.getStylesheets().add(getClass().getResource("/styles/color-theme.css").toExternalForm());
        root.getStylesheets().add(getClass().getResource("/styles/options-menu.css").toExternalForm());
        //System.out.println(caller);
        if(caller == "MainMenu") // if called from the main menu don't let the player use these two buttons
        {
            RestartButton.setDisable(true);
            MainMenuButton.setDisable(true);
        }
        else
        {
            RestartButton.setDisable(false);
            MainMenuButton.setDisable(false);
        }

        this.music = music;
    }

    public void acceptPlayer(Player player){
        this.player = player;
    }

    @FXML private StackPane root;

    public StackPane getRoot(){ return this.root; }
    
    @FXML protected void ReturnButtonClicked(ActionEvent event) {
        if (music.getShouldPlaySFX()){
            MusicPlayer music2 = new MusicPlayer();
            music2.playSFX(MusicPlayer.Track.exitMenu);
        }
        returnToCB.returnTo();
    }

    @FXML protected void SFXButtonClicked(ActionEvent event) {
        // changes sound effects volume

        //play sound effect to show effect
        if (music.getShouldPlaySFX()){
            MusicPlayer music2 = new MusicPlayer();
            music2.playSFX(MusicPlayer.Track.adjustSound);
        }

        music.setShouldPlaySFX(!music.getShouldPlaySFX());
    }

    @FXML protected void VolumeButtonClicked(ActionEvent event) {
        // Lets you change the volume
        // CycleVolume();

        //play sound effect to show effect
        if (music.getShouldPlaySFX()){
            MusicPlayer music2 = new MusicPlayer();
            music2.playSFX(MusicPlayer.Track.adjustSound);
        }

        music.setShouldPlay(!music.getShouldPlay());
    }

    @FXML protected void RestartButtonClicked(ActionEvent event) {
        // Restarts the current game
        // Restart()
    }

    @FXML protected void MainMenuButtonClicked(ActionEvent event) {
        // Returns you to main menu (from game)
        this.mainMenuCB.launchMainMenu();
    }

    @FXML protected void ExitButtonClicked(ActionEvent event) {
        //Exits program

        System.exit(0);
    }

    @FXML protected void onLogoutAction(ActionEvent e){
        try {
            AuthService.getInstance().logout(
                player.getUuid(),
                new Consumer<Boolean>(){
                    @Override
                    public void accept(Boolean success) {
                        System.out.println("Inside Consumer");
                        Platform.runLater(new Runnable(){
                            @Override
                            public void run() {
                                OptionsController.this.launchLoginCB.launchLogin();
                            }
                        });
                    }
                }
            );
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public void setLoginCB(LaunchLoginCallback launchLoginCB){
        this.launchLoginCB = launchLoginCB;
    }

    public void setMainMenuCB(LaunchMainMenuCallback mainMenuCB){
        this.mainMenuCB = mainMenuCB;
    }
    
    public void setReturnToCB(ReturnToCallback returnToCB){
        this.returnToCB = returnToCB;
    }
}
