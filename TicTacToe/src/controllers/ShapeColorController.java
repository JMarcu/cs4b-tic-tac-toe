package controllers;

import models.Color;
import models.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.*;
import models.MarkerShape;

public class ShapeColorController {
    @FXML
    private ImageView CurrentPlayerImage;

    private Color currentColor;
    private MarkerShape myShape;


    public ShapeColorController()
    {
    }

    public void acceptPlayer(Player player)
    {
        CurrentPlayerImage.setImage(new Image("assets/images/" + player.getShape().getFilename()));
        currentColor = player.getColor();
        Color.adjustImageColor(CurrentPlayerImage, currentColor);
    }

    @FXML protected void ReturnButtonClicked(ActionEvent event) {
        //assign player color and player image
        //player.setShape(myShape);
        //put callback here?
    }

    @FXML protected void XButtonClicked(ActionEvent event) {
        Image image = new Image("assets/images/" + MarkerShape.X.getFilename());
        CurrentPlayerImage.setImage(image);
        myShape = MarkerShape.X;
    }

    @FXML protected void OButtonClicked(ActionEvent event) {
        Image image = new Image("assets/images/" + MarkerShape.O.getFilename());
        CurrentPlayerImage.setImage(image);
        myShape = MarkerShape.O;
    }

    @FXML protected void SmileyButtonClicked(ActionEvent event) {
        Image image = new Image("assets/images/" + MarkerShape.SMILEY.getFilename());
        CurrentPlayerImage.setImage(image);
        myShape = MarkerShape.SMILEY;
    }

    @FXML protected void FrownyButtonClicked(ActionEvent event) {
        Image image = new Image("assets/images/" + MarkerShape.FROWNY.getFilename());
        CurrentPlayerImage.setImage(image);
        myShape = MarkerShape.FROWNY;
    }

    @FXML protected void ColorFilledButtonClicked(ActionEvent event) {
        Image image = new Image("assets/images/" + MarkerShape.FILLED.getFilename());
        CurrentPlayerImage.setImage(image);
        myShape = MarkerShape.FILLED;
    }

    @FXML protected void OutlineFilledButtonClicked(ActionEvent event) {
        Image image = new Image("assets/images/" + MarkerShape.OUTLINE.getFilename());
        CurrentPlayerImage.setImage(image);
        myShape = MarkerShape.OUTLINE;
    }

    @FXML protected void DragonButtonClicked(ActionEvent event) {
        Image image = new Image("assets/images/" + MarkerShape.DRAGON.getFilename());
        CurrentPlayerImage.setImage(image);
        myShape = MarkerShape.DRAGON;
    }

    @FXML protected void CatButtonClicked(ActionEvent event) {
        Image image = new Image("assets/images/" + MarkerShape.CAT.getFilename());
        CurrentPlayerImage.setImage(image);
        myShape = MarkerShape.CAT;
    }

    @FXML protected void StarButtonClicked(ActionEvent event) {
        Image image = new Image("assets/images/" + MarkerShape.STAR.getFilename());
        CurrentPlayerImage.setImage(image);
        myShape = MarkerShape.STAR;
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
