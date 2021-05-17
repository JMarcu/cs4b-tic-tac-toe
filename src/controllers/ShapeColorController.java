package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import models.ColorScheme;
import models.MarkerShape;
import models.Player;
import models.SceneCallback.ReturnToCallback;
import models.MusicPlayer;

public class ShapeColorController {
    @FXML
    private ImageView CurrentPlayerImage;

    @FXML
    private ScrollPane root;

    private Color currentColor;
    private MarkerShape myShape;
    private Player player;
    private ReturnToCallback returnCB;
    private MusicPlayer musicSFX;
    private MusicPlayer music;

    public void acceptPlayer(Player player, MusicPlayer music)
    {
        //Load external style sheets.
        root.getStylesheets().add(getClass().getResource("/styles/color-theme.css").toExternalForm());
        root.getStylesheets().add(getClass().getResource("/styles/shapeColor.css").toExternalForm());

        CurrentPlayerImage.setImage(new Image("assets/images/" + player.getShape().getFilename()));
        currentColor = player.getColor();
        myShape = player.getShape();
        this.player = player;
        ColorScheme.adjustImageColor(CurrentPlayerImage, currentColor);
        musicSFX = new MusicPlayer();
        this.music = music;
    }

    public ScrollPane getRoot(){ return this.root; }

    @FXML protected void ReturnButtonClicked(ActionEvent event) {
        if (music.getShouldPlaySFX()){
            musicSFX.playSFX(MusicPlayer.Track.exitMenu);
        }

        player.setMarker(
            myShape,
            currentColor != null ? currentColor : Color.BLACK
        );
        returnCB.returnTo();
    }

    @FXML protected void XButtonClicked(ActionEvent event) {
        Image image = new Image("assets/images/x.png");
        CurrentPlayerImage.setImage(image);
        myShape = MarkerShape.X;
        if (music.getShouldPlaySFX()){
            musicSFX.playSFX(MusicPlayer.Track.changeMarker);
        }
    }

    @FXML protected void OButtonClicked(ActionEvent event) {
        Image image = new Image("assets/images/o.png");
        CurrentPlayerImage.setImage(image);
        myShape = MarkerShape.O;
        if (music.getShouldPlaySFX()){
            musicSFX.playSFX(MusicPlayer.Track.changeMarker);
        }
    }

    @FXML protected void SmileyButtonClicked(ActionEvent event) {
        Image image = new Image("assets/images/SmileyFace.png");
        CurrentPlayerImage.setImage(image);
        myShape = MarkerShape.SMILEY;
        if (music.getShouldPlaySFX()){
            musicSFX.playSFX(MusicPlayer.Track.changeMarker);
        }
    }

    @FXML protected void FrownyButtonClicked(ActionEvent event) {
        Image image = new Image("assets/images/FrownyFace.png");
        CurrentPlayerImage.setImage(image);
        myShape = MarkerShape.FROWNY;
        if (music.getShouldPlaySFX()){
            musicSFX.playSFX(MusicPlayer.Track.changeMarker);
        }
    }

    @FXML protected void ColorFilledButtonClicked(ActionEvent event) {
        Image image = new Image("assets/images/ColorFilled.png");
        CurrentPlayerImage.setImage(image);
        myShape = MarkerShape.FILLED;
        if (music.getShouldPlaySFX()){
            musicSFX.playSFX(MusicPlayer.Track.changeMarker);
        }
    }

    @FXML protected void OutlineFilledButtonClicked(ActionEvent event) {
        Image image = new Image("assets/images/OutlineFilled.png");
        CurrentPlayerImage.setImage(image);
        myShape = MarkerShape.OUTLINE;
        if (music.getShouldPlaySFX()){
            musicSFX.playSFX(MusicPlayer.Track.changeMarker);
        }
    }

    @FXML protected void DragonButtonClicked(ActionEvent event) {
        Image image = new Image("assets/images/Dragon.png");
        CurrentPlayerImage.setImage(image);
        myShape = MarkerShape.DRAGON;
        if (music.getShouldPlaySFX()){
            musicSFX.playSFX(MusicPlayer.Track.changeMarker);
        }
    }

    @FXML protected void CatButtonClicked(ActionEvent event) {
        Image image = new Image("assets/images/CatFace.png");
        CurrentPlayerImage.setImage(image);
        myShape = MarkerShape.CAT;
        if (music.getShouldPlaySFX()){
            musicSFX.playSFX(MusicPlayer.Track.changeMarker);
        }
    }

    @FXML protected void StarButtonClicked(ActionEvent event) {
        Image image = new Image("assets/images/Star.png");
        CurrentPlayerImage.setImage(image);
        myShape = MarkerShape.STAR;
        if (music.getShouldPlaySFX()){
            musicSFX.playSFX(MusicPlayer.Track.changeMarker);
        }
    }

    @FXML protected void YellowButtonClicked(ActionEvent event) {
        currentColor = Color.YELLOW;
        ColorScheme.adjustImageColor(CurrentPlayerImage, currentColor);
        if (music.getShouldPlaySFX()){
            musicSFX.playSFX(MusicPlayer.Track.changeMarker);
        }
    }

    @FXML protected void RedButtonClicked(ActionEvent event) {
        currentColor = Color.RED;
        ColorScheme.adjustImageColor(CurrentPlayerImage, currentColor);
        if (music.getShouldPlaySFX()){
            musicSFX.playSFX(MusicPlayer.Track.changeMarker);
        }
    }

    @FXML protected void OrangeButtonClicked(ActionEvent event) {
        currentColor = Color.ORANGE;
        ColorScheme.adjustImageColor(CurrentPlayerImage, currentColor);
        if (music.getShouldPlaySFX()){
            musicSFX.playSFX(MusicPlayer.Track.changeMarker);
        }
    }

    @FXML protected void PinkButtonClicked(ActionEvent event) {
        currentColor = Color.PINK;
        ColorScheme.adjustImageColor(CurrentPlayerImage, currentColor);
        if (music.getShouldPlaySFX()){
            musicSFX.playSFX(MusicPlayer.Track.changeMarker);
        }
    }

    @FXML protected void BlueButtonClicked(ActionEvent event) {
        currentColor = Color.BLUE;
        ColorScheme.adjustImageColor(CurrentPlayerImage, currentColor);
        if (music.getShouldPlaySFX()){
            musicSFX.playSFX(MusicPlayer.Track.changeMarker);
        }
    }

    @FXML protected void GreenButtonClicked(ActionEvent event) {
        currentColor = Color.GREEN;
        ColorScheme.adjustImageColor(CurrentPlayerImage, currentColor);
        if (music.getShouldPlaySFX()){
            musicSFX.playSFX(MusicPlayer.Track.changeMarker);
        }
    }

    @FXML protected void TealButtonClicked(ActionEvent event) {
        currentColor = Color.TEAL;
        ColorScheme.adjustImageColor(CurrentPlayerImage, currentColor);
        if (music.getShouldPlaySFX()){
            musicSFX.playSFX(MusicPlayer.Track.changeMarker);
        }
    }

    @FXML protected void PurpleButtonClicked(ActionEvent event) {
        currentColor = Color.PURPLE;
        ColorScheme.adjustImageColor(CurrentPlayerImage, currentColor);
        if (music.getShouldPlaySFX()){
            musicSFX.playSFX(MusicPlayer.Track.changeMarker);
        }
    }

    @FXML protected void BlackButtonClicked(ActionEvent event) {
        currentColor = Color.BLACK;
        ColorScheme.adjustImageColor(CurrentPlayerImage, currentColor);
        if (music.getShouldPlaySFX()){
            musicSFX.playSFX(MusicPlayer.Track.changeMarker);
        }
    }

    public void setReturnCB(ReturnToCallback returnCB){
        this.returnCB = returnCB;
    }
}
