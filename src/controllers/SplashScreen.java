package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

enum Splash{
    title, lose, tie, win;
}

public class SplashScreen {
    private final String SPLASH_DIRECTORY = "src/assets/images/splash";

    @FXML private ImageView splashImageView;
    @FXML private Label splashText;
    @FXML private Button splashButton;

    @FXML
    private void Initialize() {
    }

    @FXML
    public void showSplash(Splash type){
        String splashLocation = "";
        String message = "";
        switch(type){
            case title: splashLocation = SPLASH_DIRECTORY.concat("title.png");
                break;
            case lose:  splashLocation = SPLASH_DIRECTORY.concat("grave.png");
                        message = "YOU LOSE";
                break;
            case tie:   splashLocation = SPLASH_DIRECTORY.concat("tie.png");
                        message = "YOU TIED";
                break;
            case win:   splashLocation = SPLASH_DIRECTORY.concat("trophy.png");
                        message = "YOU WON";
                break;
            default:
                break;
        }

        Image image = new Image(splashLocation);
        splashImageView.setImage(image);
        splashText.setText(message);
    }

    @FXML
    private void handleSplashButton(MouseEvent e){
        Stage stage = (Stage) splashButton.getScene().getWindow();
        stage.close();
    }

}
