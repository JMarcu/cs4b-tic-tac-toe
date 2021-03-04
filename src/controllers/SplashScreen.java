package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class SplashScreen {
    @FXML private ImageView splashImageView;
    @FXML private Label splashText;
    @FXML private Button splashButton;

    @FXML
    private void Initialize() {
        Image image = new Image("src/assets/images/trophy.png");
        splashImageView.setImage(image);
        splashText.setText("YOU WON!");
    }

    @FXML
    private void handleSplashButton(MouseEvent e){
        Stage stage = (Stage) splashButton.getScene().getWindow();
        stage.close();
    }

}
