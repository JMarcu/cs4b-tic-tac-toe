package controllers;

import java.util.ArrayList;
import java.util.concurrent.Flow.*;

import javax.lang.model.type.NullType;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

import javafx.event.ActionEvent;
import models.Color;
import models.GameMode;
import models.Player;

import models.SceneCallback.LaunchOptionsMenuCallback;
import models.SceneCallback.LaunchShapePickerCallback;
import models.SceneCallback.LaunchScoreBoardCallback;


public class GameBoard{

    private boolean singlePlayer;
    private GameMode gameMode; 
    private Player playerOne;
    private Player playerTwo;
    private int secondaryOption;

    private LaunchOptionsMenuCallback optionsMenuCB;
    private LaunchShapePickerCallback shapePickerCB;
    private LaunchScoreBoardCallback scoreBoardCB;
    private ArrayList<Subscription> subscriptions;

    private final String ASSETS_DIRECTORY = "/assets/images/";
    
    @FXML
    private Button optionButton;

    @FXML
    private Button scoreButton;
    
    @FXML
    private TextField playerOneTF;

    @FXML
    private TextField playerTwoTF;

    @FXML
    private ImageView playerOneShapeIV;

    @FXML
    private ImageView playerTwoShapeIV;

    @FXML
    private Board boardController;
    
    public GameBoard(){
        this.singlePlayer = false;
        this.gameMode = null;
        this.playerOne = null;
        this.playerTwo = null;
        this.secondaryOption = 0;
        this.subscriptions = new ArrayList<>();
    }

    //Loads data from launchGame
    public void loadData(boolean singlePlayer, GameMode gameMode, Player playerOne, Player playerTwo, int secondaryOption){

        this.singlePlayer = singlePlayer;
        this.gameMode = gameMode;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.secondaryOption = secondaryOption;

        playerOneTF.setText(playerOne.getName());
        playerTwoTF.setText(playerTwo.getName());

        bindPlayers(playerOneShapeIV, playerOne);
        bindPlayers(playerTwoShapeIV, playerTwo);

        updateImage(playerOneShapeIV, playerOne);
        updateImage(playerTwoShapeIV, playerTwo);

        boardController.setPlayer(playerOne);
    }
    
    @FXML 
    private void initialize(){ }

    @FXML //Sets Playerone's name on the textfield for the gameboard
    private void onPlayerOneTF(KeyEvent event){
        this.playerOne.setName(this.playerOneTF.getText());
    }

    @FXML //Sets Playertwo's name on the textfield for the gameboard
    private void onPlayerTwoTF(KeyEvent event){
        this.playerTwo.setName(this.playerTwoTF.getText());
    }

    @FXML //Allows playerone to pick a new shape/image to use for the board by pressig the shape button
    private void onPlayerOneShapeAction(ActionEvent event) {
        this.shapePickerCB.launchShapePicker(this.playerOne);
    }

    @FXML //Allows playertwo to pick a new shape/image to use for the board by pressig the shape button
    private void onPlayerTwoShapeAction(ActionEvent event) {
        this.shapePickerCB.launchShapePicker(this.playerTwo);
    }

    @FXML //Allows playerone to use the options menu by pressing the gear button
    private void onOption(ActionEvent event){
      // System.out.println("onOption");
        this.optionsMenuCB.launchOptionsMenu(this.playerOne.getUuid());
    }

    @FXML //Allows playerone to access the scoreboard by pressing the scoreboard button
    private void onScoreBoard(ActionEvent event){
        System.out.println("onScoreBoard");
        this.scoreBoardCB.launchScoreBoard(this.playerOne.getUuid());
    }

    @FXML //Checks whetheer or not the image is already in use or null and sets it if both ar false
    private void updateImage(ImageView iv, Player player){
        final String newUrl = ASSETS_DIRECTORY.concat(player.getShape().getFilename());
        if(iv.getImage() == null || !iv.getImage().getUrl().equals(newUrl)){
            final Image newImage = new Image(newUrl);
            iv.setImage(newImage);
        }
        Color.adjustImageColor(iv, player.getColor());
    }


    //Binds the player to their chosen image/shape
    private void bindPlayers(ImageView iv, Player player){
        player.subscribe(new Subscriber<NullType>(){
			@Override
			public void onSubscribe(Subscription subscription) {
                subscriptions.add(subscription);
            }

			@Override
			public void onNext(NullType item) {
                updateImage(iv, player);
			}

			@Override
			public void onError(Throwable throwable) { }

			@Override
			public void onComplete() { }
        });
    }

    public void setShapePickerCB(LaunchShapePickerCallback shapePickerCB){this.shapePickerCB = shapePickerCB;}
    public void setOptionsMenuCB(LaunchOptionsMenuCallback optionsMenuCB){this.optionsMenuCB = optionsMenuCB;}
    public void setScoreBoardCB(LaunchScoreBoardCallback scoreBoardCB){this.scoreBoardCB = scoreBoardCB;}
    
    public Player TEMPORARY_GET_PLAYER_FOR_TESTING(){return this.playerOne;}
}