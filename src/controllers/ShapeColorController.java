package controllers;

import models.ColorScheme;
import models.Player;
import models.ReturnToCallback;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import models.MarkerShape;

public class ShapeColorController {
    @FXML
    private ImageView CurrentPlayerImage;
    private Color currentColor;
    private MarkerShape myShape;
    private Player player;
    private ReturnToCallback returnCB;

    public void acceptPlayer(Player player)
    {
        CurrentPlayerImage.setImage(new Image("assets/images/" + player.getShape().getFilename()));
        currentColor = player.getColor();
        this.player = player;
        ColorScheme.adjustImageColor(CurrentPlayerImage, currentColor);
    }

    @FXML protected void ReturnButtonClicked(ActionEvent event) {
        player.setColor(currentColor != null ? currentColor : Color.BLACK);
        player.setShape(myShape);
        this.returnCB.returnToCB();
    }

    @FXML protected void XButtonClicked(ActionEvent event) {
        Image image = new Image("assets/images/x.png");
        CurrentPlayerImage.setImage(image);
        myShape = MarkerShape.X;
    }

    @FXML protected void OButtonClicked(ActionEvent event) {
        Image image = new Image("assets/images/o.png");
        CurrentPlayerImage.setImage(image);
        myShape = MarkerShape.O;
    }

    @FXML protected void SmileyButtonClicked(ActionEvent event) {
        Image image = new Image("assets/images/SmileyFace.png");
        CurrentPlayerImage.setImage(image);
        myShape = MarkerShape.SMILEY;
    }

    @FXML protected void FrownyButtonClicked(ActionEvent event) {
        Image image = new Image("assets/images/FrownyFace.png");
        CurrentPlayerImage.setImage(image);
        myShape = MarkerShape.FROWNY;
    }

    @FXML protected void ColorFilledButtonClicked(ActionEvent event) {
        Image image = new Image("assets/images/ColorFilled.png");
        CurrentPlayerImage.setImage(image);
        myShape = MarkerShape.FILLED;
    }

    @FXML protected void OutlineFilledButtonClicked(ActionEvent event) {
        Image image = new Image("assets/images/OutlineFilled.png");
        CurrentPlayerImage.setImage(image);
        myShape = MarkerShape.OUTLINE;
    }

    @FXML protected void DragonButtonClicked(ActionEvent event) {
        Image image = new Image("assets/images/Dragon.png");
        CurrentPlayerImage.setImage(image);
        myShape = MarkerShape.DRAGON;
    }

    @FXML protected void CatButtonClicked(ActionEvent event) {
        Image image = new Image("assets/images/CatFace.png");
        CurrentPlayerImage.setImage(image);
        myShape = MarkerShape.CAT;
    }

    @FXML protected void StarButtonClicked(ActionEvent event) {
        Image image = new Image("assets/images/Star.png");
        CurrentPlayerImage.setImage(image);
        myShape = MarkerShape.STAR;
    }

    @FXML protected void YellowButtonClicked(ActionEvent event) {
        currentColor = Color.YELLOW;
        ColorScheme.adjustImageColor(CurrentPlayerImage, currentColor);
    }

    @FXML protected void RedButtonClicked(ActionEvent event) {
        currentColor = Color.RED;
        ColorScheme.adjustImageColor(CurrentPlayerImage, currentColor);
    }

    @FXML protected void OrangeButtonClicked(ActionEvent event) {
        currentColor = Color.ORANGE;
        ColorScheme.adjustImageColor(CurrentPlayerImage, currentColor);
    }

    @FXML protected void PinkButtonClicked(ActionEvent event) {
        currentColor = Color.PINK;
        ColorScheme.adjustImageColor(CurrentPlayerImage, currentColor);
    }

    @FXML protected void BlueButtonClicked(ActionEvent event) {
        currentColor = Color.BLUE;
        ColorScheme.adjustImageColor(CurrentPlayerImage, currentColor);
    }

    @FXML protected void GreenButtonClicked(ActionEvent event) {
        currentColor = Color.GREEN;
        ColorScheme.adjustImageColor(CurrentPlayerImage, currentColor);
    }

    @FXML protected void TealButtonClicked(ActionEvent event) {
        currentColor = Color.TEAL;
        ColorScheme.adjustImageColor(CurrentPlayerImage, currentColor);
    }

    @FXML protected void PurpleButtonClicked(ActionEvent event) {
        currentColor = Color.PURPLE;
        ColorScheme.adjustImageColor(CurrentPlayerImage, currentColor);
    }

    @FXML protected void BlackButtonClicked(ActionEvent event) {
        currentColor = Color.BLACK;
        ColorScheme.adjustImageColor(CurrentPlayerImage, currentColor);
    }

    public void setReturnCB(ReturnToCallback returnCB){
        this.returnCB = returnCB;
    }
}
