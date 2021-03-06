package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import models.SceneCallback.ReturnToCallback;

public class SplashScreen {
    public enum SplashType{
        TITLE ("", "title"),
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

    private ReturnToCallback returnToCB;

    private final String SPLASH_DIRECTORY = "/assets/images/splash/";

    @FXML private ImageView splashImageView;
    @FXML private Label splashText;
    @FXML private Button splashButton;
    @FXML private VBox root;

    public VBox getRoot(){
        return this.root;
    }

    public void setReturnCB(ReturnToCallback returnToCB){
        this.returnToCB = returnToCB;
    }
    
    public void setSplashType(SplashType splashType){
        StringBuilder sb = new StringBuilder();
        sb.append(SPLASH_DIRECTORY);
        sb.append(splashType.getFilename());
        sb.append(".png");
        splashImageView.setImage(new Image(sb.toString()));
        splashText.setText(splashType.getMessage());
    }

    @FXML
    private void handleSplashButton(MouseEvent e){
        if(returnToCB != null){
            returnToCB.returnTo();
        }
    }
}
