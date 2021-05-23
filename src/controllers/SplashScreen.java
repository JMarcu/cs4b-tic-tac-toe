package controllers;

import interfaces.CallBackable;
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
import models.Player;

public class SplashScreen {

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

    private CallBackable closeCB;
    private CallBackable playAgainCB;
    private Player player;
    private CallBackable returnToMenuCB;

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
        this.closeCB = null;
        this.playAgainCB = null;
        this.player = null;
        this.returnToMenuCB = null;
    }

    /** Sets the default state of the view's interactive elements. */
    @FXML
    void initialize(){
        //Load external style sheets
        root.getStylesheets().add(getClass().getResource("/styles/color-theme.css").toExternalForm());
        root.getStylesheets().add(getClass().getResource("/styles/splash-screen.css").toExternalForm());
        if(player != null){
            personName.setText(player.getName());
        }
    }

    public VBox getRoot(){
        return this.root;
    }

    public void setCloseCallback(CallBackable closeCB){
        this.closeCB = closeCB;
    }

    public void setPlayAgainCallback(CallBackable playAgainCB){
        this.playAgainCB = playAgainCB;
    }

    public void setPlayer(Player player){
        this.player = player;
        if(personName != null){
            personName.setText(player.getName());
        }
    }

    public void setReturnToMenuCallback(CallBackable returnToMenuCB){
        this.returnToMenuCB = returnToMenuCB;
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
    private void onBgClick(MouseEvent event){
        if(closeCB != null){
            closeCB.callback();
        }
    }

    @FXML
    private void onPlayAgainAction(ActionEvent event){
        this.playAgainCB.callback();
    }

    @FXML
    private void onBackToMenuAction(ActionEvent event){
        this.returnToMenuCB.callback();
    }
}
