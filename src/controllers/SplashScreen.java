package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.SceneCallback.LaunchGameCallback;
import models.SceneCallback.LaunchMainMenuCallback;
import models.SceneCallback.ReturnToCallback;
import models.GameState;

public class SplashScreen {
    private GameState gameState;

    public enum SplashType{
        TITLE ("Click anywhere to continue...", "title"),
        LOSE ("YOU LOSE", "grave"),
        DRAW ("YOU TIED", "tie"),
        WIN ("YOU WIN", "trophy");

        private String filename;
        private String message;

        SplashType(String message, String filename){
            this.filename = filename;
            this.message = message;
        }

        public String getMessage(){ return this.message; }
        public String getFilename(){ return this.filename; }
    };

    private LaunchGameCallback launchGameCB;
    private LaunchMainMenuCallback launchMainMenuCB;
    private ReturnToCallback returnToCB;

    private final String SPLASH_DIRECTORY = "/assets/images/splash/";

    @FXML private ImageView splashImageView;
    @FXML private Label personName;
    @FXML private Label splashText;
    @FXML private Button replayButton;
    @FXML private Separator spacer;
    @FXML private Button continueButton;
    @FXML private HBox buttons;
    @FXML private VBox root;

    public SplashScreen(){
        this.gameState = null;
    }

    /** Sets the default state of the view's interactive elements. */
    @FXML
    void initialize(){
        //Load external style sheets
        root.getStylesheets().add(getClass().getResource("/styles/color-theme.css").toExternalForm());
        root.getStylesheets().add(getClass().getResource("/styles/splash-screen.css").toExternalForm());
        if(gameState != null){
            personName.setText(gameState.getPlayers().getValue0().getName());
        }
    }

    public VBox getRoot(){
        return this.root;
    }

    public void setLaunchGameCB(LaunchGameCallback launchGameCB){
        this.launchGameCB = launchGameCB;
    }

    public void setLaunchMainMenuCB(LaunchMainMenuCallback launchMainMenuCB){
        this.launchMainMenuCB = launchMainMenuCB;
    }

    public void setReturnCB(ReturnToCallback returnToCB){
        this.returnToCB = returnToCB;
    }

    public void setGameState(GameState gameState){
        this.gameState = gameState;
    }
    
    public void setSplashType(SplashType splashType){
        StringBuilder sb = new StringBuilder();
        sb.append(SPLASH_DIRECTORY);
        sb.append(splashType.getFilename());
        sb.append(".png");
        splashImageView.setImage(new Image(sb.toString()));
        splashText.setText(splashType.getMessage());
        if(splashType.getFilename() == "title"){
            personName.setVisible(false);
            buttons.setVisible(false);
        }
        else{
            personName.setVisible(true);
            buttons.setVisible(true);
        }
    }

    @FXML
    private void handleReplayButton(MouseEvent e){
        // Replay Logic goes here
    }

    /** Closes the splash screen when the element is clicked on. */
    @FXML
    private void closeSplash(MouseEvent e){
        if(returnToCB != null){
            returnToCB.returnTo();
        }
    }

    @FXML
    private void onPlayAgainAction(ActionEvent event){
        launchGameCB.launchGame(gameState);
    }

    @FXML
    private void onBackToMenuAction(ActionEvent event){
        launchMainMenuCB.launchMainMenu();
    }
}
