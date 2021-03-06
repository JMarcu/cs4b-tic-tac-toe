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
import javafx.scene.layout.BorderPane;
import javafx.event.ActionEvent;
import models.ColorScheme;
import models.GameState;
import models.Player;
import models.TTTScene;
import models.SceneCallback.LaunchOptionsMenuCallback;
import models.SceneCallback.LaunchShapePickerCallback;
import models.SceneCallback.LaunchScoreBoardCallback;


public class GameBoard{
    
    private GameState gameState;

    private LaunchOptionsMenuCallback optionsMenuCB;
    private LaunchShapePickerCallback shapePickerCB;
    private LaunchScoreBoardCallback scoreBoardCB;
    private ArrayList<Subscription> subscriptions;

    private final String ASSETS_DIRECTORY = "/assets/images/";

    @FXML 
    private BorderPane root;
    
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
        this.gameState = new GameState();
        this.subscriptions = new ArrayList<>();
    }

    //Loads data from launchGame
    public void setGameState(GameState gameState){
        this.gameState = gameState;

        playerOneTF.setText(gameState.getPlayers().getValue0().getName());
        playerTwoTF.setText(gameState.getPlayers().getValue1().getName());

        bindPlayers(playerOneShapeIV, gameState.getPlayers().getValue0());
        bindPlayers(playerTwoShapeIV, gameState.getPlayers().getValue1());

        updateImage(playerOneShapeIV, gameState.getPlayers().getValue0());
        updateImage(playerTwoShapeIV, gameState.getPlayers().getValue1());

        boardController.setPlayers(gameState.getPlayers().getValue0(), gameState.getPlayers().getValue1());
    }
    
    @FXML 
    private void initialize(){
        this.root.getStylesheets().add(getClass().getResource("/styles/color-theme.css").toExternalForm());
        this.root.getStylesheets().add(getClass().getResource("/styles/game-board.css").toExternalForm());
     }

    @FXML //Sets Playerone's name on the textfield for the gameboard
    private void onPlayerOneTF(KeyEvent event){
        gameState.getPlayers().getValue0().setName(this.playerOneTF.getText());
    }

    @FXML //Sets Playertwo's name on the textfield for the gameboard
    private void onPlayerTwoTF(KeyEvent event){
        gameState.getPlayers().getValue1().setName(this.playerTwoTF.getText());
    }

    @FXML //Allows playerone to pick a new shape/image to use for the board by pressig the shape button
    private void onPlayerOneShapeAction(ActionEvent event) {
        this.shapePickerCB.launchShapePicker(gameState.getPlayers().getValue0(), TTTScene.GAME_BOARD, gameState);
    }

    @FXML //Allows playertwo to pick a new shape/image to use for the board by pressig the shape button
    private void onPlayerTwoShapeAction(ActionEvent event) {
        this.shapePickerCB.launchShapePicker(gameState.getPlayers().getValue1(), TTTScene.GAME_BOARD, gameState);
    }

    @FXML //Allows playerone to use the options menu by pressing the gear button
    private void onOption(ActionEvent event){
      // System.out.println("onOption");
        this.optionsMenuCB.launchOptionsMenu(gameState.getPlayers().getValue0().getUuid(), TTTScene.GAME_BOARD, gameState);
    }

    @FXML //Allows playerone to access the scoreboard by pressing the scoreboard button
    private void onScoreBoard(ActionEvent event){
        // System.out.println("onScoreBoard");
        this.scoreBoardCB.launchScoreBoard(gameState.getPlayers().getValue0().getUuid(), TTTScene.GAME_BOARD, gameState);
    }

    @FXML //Checks whetheer or not the image is already in use or null and sets it if both ar false
    private void updateImage(ImageView iv, Player player){
        final String newUrl = ASSETS_DIRECTORY.concat(player.getShape().getFilename());
        if(iv.getImage() == null || !iv.getImage().getUrl().equals(newUrl)){
            final Image newImage = new Image(newUrl);
            iv.setImage(newImage);
        }
        ColorScheme.adjustImageColor(iv, player.getColor());
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
}