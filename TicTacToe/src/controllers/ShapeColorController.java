package controllers;

import models.Color;
import models.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.*;

public class ShapeColorController {
    @FXML
    private ImageView CurrentPlayerImage;
    private Color currentColor;

    public void acceptPlayer(Player player)
    {
        //CurrentPlayerImage = "assets/images/" + player.shape;
        //String currentColor = player.color;

        //Change player to have an image instead of a shape and color?

        //fix when getting them to communicate
    }

    @FXML protected void ReturnButtonClicked(ActionEvent event) {
        //assign player color and player image

        //put callback here?
    }

    @FXML protected void XButtonClicked(ActionEvent event) {
        Image image = new Image("assets/images/X.png");
        CurrentPlayerImage.setImage(image);
    }

    @FXML protected void OButtonClicked(ActionEvent event) {
        Image image = new Image("assets/images/O.png");
        CurrentPlayerImage.setImage(image);
    }

    @FXML protected void SmileyButtonClicked(ActionEvent event) {
        Image image = new Image("assets/images/SmileyFace.png");
        CurrentPlayerImage.setImage(image);
    }

    @FXML protected void FrownyButtonClicked(ActionEvent event) {
        Image image = new Image("assets/images/FrownyFace.png");
        CurrentPlayerImage.setImage(image);
    }

    @FXML protected void ColorFilledButtonClicked(ActionEvent event) {
        Image image = new Image("assets/images/ColorFilled.png");
        CurrentPlayerImage.setImage(image);
    }

    @FXML protected void OutlineFilledButtonClicked(ActionEvent event) {
        Image image = new Image("assets/images/OutlineFilled.png");
        CurrentPlayerImage.setImage(image);
    }

    @FXML protected void DragonButtonClicked(ActionEvent event) {
        Image image = new Image("assets/images/Dragon.png");
        CurrentPlayerImage.setImage(image);
    }

    @FXML protected void CatButtonClicked(ActionEvent event) {
        Image image = new Image("assets/images/CatFace.png");
        CurrentPlayerImage.setImage(image);
    }

    @FXML protected void StarButtonClicked(ActionEvent event) {
        Image image = new Image("assets/images/Star.png");
        CurrentPlayerImage.setImage(image);
    }

    @FXML protected void YellowButtonClicked(ActionEvent event) {
        currentColor = Color.YELLOW;
        Color.adjustImageColor(CurrentPlayerImage, currentColor);
    }

    @FXML protected void RedButtonClicked(ActionEvent event) {
        currentColor = Color.RED;
        Color.adjustImageColor(CurrentPlayerImage, currentColor);
    }

    @FXML protected void OrangeButtonClicked(ActionEvent event) {
        currentColor = Color.ORANGE;
        Color.adjustImageColor(CurrentPlayerImage, currentColor);
    }

    @FXML protected void PinkButtonClicked(ActionEvent event) {
        currentColor = Color.PINK;
        Color.adjustImageColor(CurrentPlayerImage, currentColor);
    }

    @FXML protected void BlueButtonClicked(ActionEvent event) {
        currentColor = Color.BLUE;
        Color.adjustImageColor(CurrentPlayerImage, currentColor);
    }

    @FXML protected void GreenButtonClicked(ActionEvent event) {
        currentColor = Color.GREEN;
        Color.adjustImageColor(CurrentPlayerImage, currentColor);
    }

    @FXML protected void TealButtonClicked(ActionEvent event) {
        currentColor = Color.TEAL;
        Color.adjustImageColor(CurrentPlayerImage, currentColor);
    }

    @FXML protected void PurpleButtonClicked(ActionEvent event) {
        currentColor = Color.PURPLE;
        Color.adjustImageColor(CurrentPlayerImage, currentColor);
    }

    @FXML protected void BlackButtonClicked(ActionEvent event) {
        currentColor = Color.BLACK;
        Color.adjustImageColor(CurrentPlayerImage, currentColor);
    }
}