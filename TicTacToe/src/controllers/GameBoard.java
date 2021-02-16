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

import models.GameMode;
import models.Player;

import models.SceneCallback.LaunchOptionsMenuCallback;
import models.SceneCallback.LaunchShapePickerCallback;


public class GameBoard{

    private boolean singlePlayer;
    private GameMode gameMode; 
    private Player playerOne;
    private Player playerTwo;
    private int secondaryOption;

    private LaunchOptionsMenuCallback optionsMenuCB;
    private LaunchShapePickerCallback shapePickerCB;
    private ArrayList<Subscription> subscriptions;

    private final String ASSETS_DIRECTORY = "/assets/images/";
    
    @FXML
    private Button optionButton;
    
    @FXML
    private TextField playerOneTF;

    @FXML
    private TextField playerTwoTF;

    @FXML
    private ImageView playerOneShapeIV;

    @FXML
    private ImageView playerTwoShapeIV;

    
    public GameBoard(){
        this.singlePlayer = false;
        this.gameMode = null;
        this.playerOne = null;
        this.playerTwo = null;
        this.secondaryOption = 0;
        this.subscriptions = new ArrayList<>();
    }

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
    }
    
    @FXML
    private void initialize(){
        System.out.println("initialize");
    }

    @FXML
    private void onPlayerOneTF(KeyEvent event){
        this.playerOne.setName(this.playerOneTF.getText());
    }

    @FXML
    private void onPlayerTwoTF(KeyEvent event){
        this.playerTwo.setName(this.playerTwoTF.getText());
    }

    @FXML
    private void onPlayerOneShapeAction(ActionEvent event) {
        this.shapePickerCB.launchShapePicker(this.playerOne);
    }

    @FXML
    private void onPlayerTwoShapeAction(ActionEvent event) {
        this.shapePickerCB.launchShapePicker(this.playerTwo);
    }

    @FXML
    private void onOption(ActionEvent event){
        System.out.println("onOption");
        this.optionsMenuCB.launchOptionsMenu(this.playerOne.getUuid());
    }

    @FXML
    private void updateImage(ImageView iv, Player player){
        final String newUrl = ASSETS_DIRECTORY.concat(player.getShape().getFilename());
        if(iv.getImage() == null || !iv.getImage().getUrl().equals(newUrl)){
            final Image newImage = new Image(newUrl);
            iv.setImage(newImage);
        }
    }

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
    
    public Player TEMPORARY_GET_PLAYER_FOR_TESTING(){return this.playerOne;}
}