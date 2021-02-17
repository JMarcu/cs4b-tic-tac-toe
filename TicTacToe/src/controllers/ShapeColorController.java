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


    public ShapeColorController()
    {
    }

    public void acceptPlayer(Player player)
    {
        CurrentPlayerImage.setImage(new Image("assets/images/" + player.getShape().getFilename()));
        String currentColor = player.getColor();

        switch(currentColor)
        {
            case "yellow":
                ColorAdjust yellow = new ColorAdjust();
                yellow.setContrast(1);     
                yellow.setBrightness(0);  
                yellow.setSaturation(1);
                yellow.setHue(.33);
                CurrentPlayerImage.setEffect(yellow);
                currentColor = "yellow";
                break;
            case "blue":
                ColorAdjust blue = new ColorAdjust();
                blue.setContrast(1);     
                blue.setBrightness(0);  
                blue.setSaturation(1);
                blue.setHue(-.7);
                CurrentPlayerImage.setEffect(blue);
                currentColor = "blue";
                break;
            case "red":
                ColorAdjust red = new ColorAdjust();
                red.setContrast(1);     
                red.setBrightness(0);  
                red.setSaturation(1);
                red.setHue(0);
                CurrentPlayerImage.setEffect(red);
                currentColor = "red";
                break;
            case "green":
                ColorAdjust green = new ColorAdjust();
                green.setContrast(1);     
                green.setBrightness(0);  
                green.setSaturation(1);
                green.setHue(.66);
                CurrentPlayerImage.setEffect(green);
                currentColor = "green";
                break;
            case "pink":
                ColorAdjust pink = new ColorAdjust();
                pink.setContrast(1);     
                pink.setBrightness(0);  
                pink.setSaturation(1);
                pink.setHue(-.22);
                CurrentPlayerImage.setEffect(pink);
                currentColor = "pink";
            break;
            case "purple":
                ColorAdjust purple = new ColorAdjust();
                purple.setContrast(1);     
                purple.setBrightness(0);  
                purple.setSaturation(1);
                purple.setHue(-.45);
                CurrentPlayerImage.setEffect(purple);
                currentColor = "purple";
                break;
            case "orange":
                ColorAdjust orange = new ColorAdjust();
                orange.setContrast(1);     
                orange.setBrightness(0);  
                orange.setSaturation(1);
                orange.setHue(.17);
                CurrentPlayerImage.setEffect(orange);
                currentColor = "orange";
                break;
            case "teal":
                ColorAdjust teal = new ColorAdjust();
                teal.setContrast(1);     
                teal.setBrightness(0);  
                teal.setSaturation(1);
                teal.setHue(1);
                CurrentPlayerImage.setEffect(teal);
                currentColor = "teal";
                break;
            default:
                ColorAdjust black = new ColorAdjust();
                black.setBrightness(-1);  
                CurrentPlayerImage.setEffect(black);
                currentColor = "black";
        }
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