package controllers;

import java.util.concurrent.Flow.*;

import org.javatuples.Pair;

import java.util.Vector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
//import javafx.scene.layout.BorderPane;
import javafx.scene.control.ScrollPane;
import models.Ai;
import models.ColorScheme;
import models.GameState;
import models.Player;
import models.SceneCallback.LaunchOptionsMenuCallback;
import models.SceneCallback.LaunchScoreBoardCallback;
import models.SceneCallback.LaunchShapePickerCallback;
import models.TTTScene;

public class GameBoard{
    
    private GameState                 gameState;
    private Subscription              gameStateSubscription;
    private LaunchOptionsMenuCallback optionsMenuCB;
    private Subscription              playerOneSubscription;
    private Subscription              playerTwoSubscription;
    private LaunchShapePickerCallback shapePickerCB;
    private LaunchScoreBoardCallback  scoreBoardCB;
    private boolean                   viewInit;
    private boolean                   read;
    private Vector<GameState>         gameHistory = new Vector<GameState>();

    private final String ASSETS_DIRECTORY = "/assets/images/";

    @FXML private Button optionButton;
    @FXML private Button scoreButton;
    @FXML private TextField playerOneTF;
    @FXML private TextField playerTwoTF;
    @FXML private ImageView playerOneShapeIV;
    @FXML private ImageView playerTwoShapeIV;
    @FXML private ImageView options;
    @FXML private Board boardController;
    @FXML private ScrollPane root;
    
    public GameBoard(){
        this.gameState = null;
        this.gameStateSubscription = null;
        this.optionsMenuCB = null;
        this.playerOneSubscription = null;
        this.playerTwoSubscription = null;
        this.shapePickerCB = null;
        this.scoreBoardCB = null;
        this.viewInit = false;
    }
    
    @FXML 
    private void initialize(){ 
        this.viewInit = true; 

        if(gameState != null){
            finallyInitialize();
        }

        this.root.getStylesheets().add(getClass().getResource("/styles/color-theme.css").toExternalForm());
        this.root.getStylesheets().add(getClass().getResource("/styles/game-board.css").toExternalForm());

        ColorScheme.adjustImageColor(options, ColorScheme.TEXT_ON_SECONDARY.getColor());
    }

    private void finallyInitialize(){
        if(!read) gameHistory.add(gameState);
        read = false;

        boardController.setGameState(gameState);

        playerOneTF.setText(gameState.getPlayers().getValue0().getName());
        playerTwoTF.setText(gameState.getPlayers().getValue1().getName());

        updateImage(playerOneShapeIV, gameState.getPlayers().getValue0());
        updateImage(playerTwoShapeIV, gameState.getPlayers().getValue1());
    }

    /************************************************************************************************************
     * ACCESSORS & MUTATORS
     ************************************************************************************************************/

    public ScrollPane getRoot(){ return this.root; }

    //Loads data from launchGame
    public void setGameState(GameState gameState){
        this.gameState = gameState;
        if(!read){ gameHistory.add(gameState); read = true;}

        if(gameStateSubscription != null){ gameStateSubscription.cancel(); }
        if(playerOneSubscription != null){ playerOneSubscription.cancel(); }
        if(playerTwoSubscription != null){ playerTwoSubscription.cancel(); }
        
        this.gameState.subscribe(new Subscriber<GameState.Patch>(){
			@Override public void onSubscribe(Subscription subscription) { 
                gameStateSubscription = subscription; 
                gameStateSubscription.request(1);
            }
			@Override public void onNext(GameState.Patch item) { 
                onGameStatePatch(item); 
                gameStateSubscription.request(1);
            }
			@Override public void onError(Throwable throwable) { }
			@Override public void onComplete() { }
        });
        
        this.gameState.getPlayers().getValue0().subscribe(new Subscriber<Player.Patch>(){
			@Override public void onSubscribe(Subscription subscription) { 
                playerOneSubscription = subscription; 
                playerOneSubscription.request(1);
            }
			@Override public void onNext(Player.Patch item) { 
                onPlayerPatch(gameState.getPlayers().getValue0(), playerOneShapeIV, item); 
                playerOneSubscription.request(1);
            }
			@Override public void onError(Throwable throwable) { }
			@Override public void onComplete() { }
        });
        if(!read){ gameHistory.add(gameState); read = true;}

        this.gameState.getPlayers().getValue1().subscribe(new Subscriber<Player.Patch>(){
			@Override public void onSubscribe(Subscription subscription) { 
                playerTwoSubscription = subscription; 
                playerTwoSubscription.request(1);
            }
			@Override public void onNext(Player.Patch item) { 
                onPlayerPatch(gameState.getPlayers().getValue1(), playerTwoShapeIV, item); 
                playerTwoSubscription.request(1);
            }
			@Override public void onError(Throwable throwable) { }
			@Override public void onComplete() { }
        });

        if(!read){ gameHistory.add(gameState); read = true;}
        
        if(viewInit){
            finallyInitialize();
        }
    }

    public void setOptionsMenuCB(LaunchOptionsMenuCallback optionsMenuCB) {this.optionsMenuCB = optionsMenuCB;}
    public void setScoreBoardCB(LaunchScoreBoardCallback scoreBoardCB)    {this.scoreBoardCB = scoreBoardCB;}
    public void setShapePickerCB(LaunchShapePickerCallback shapePickerCB) {this.shapePickerCB = shapePickerCB;}

    //Checks whetheer or not the image is already in use or null and sets it if both ar false
    private void updateImage(ImageView iv, Player player){
        final String newUrl = ASSETS_DIRECTORY.concat(player.getShape().getFilename());
        if(iv.getImage() == null || !iv.getImage().getUrl().equals(newUrl)){
            final Image newImage = new Image(newUrl);
            iv.setImage(newImage);
        }
        ColorScheme.adjustImageColor(iv, player.getColor());
    }

    /************************************************************************************************************
     * EVENT HANDLERS
     ************************************************************************************************************/

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
        this.shapePickerCB.launchShapePicker(gameState.getPlayers().getValue0());
    }

    @FXML //Allows playertwo to pick a new shape/image to use for the board by pressig the shape button
    private void onPlayerTwoShapeAction(ActionEvent event) {
        this.shapePickerCB.launchShapePicker(gameState.getPlayers().getValue1());
    }

    @FXML //Allows playerone to use the options menu by pressing the gear button
    private void onOption(ActionEvent event){
      // System.out.println("onOption");
        this.optionsMenuCB.launchOptionsMenu("Game");
    }

    @FXML //Allows playerone to access the scoreboard by pressing the scoreboard button
    private void onScoreBoard(ActionEvent event){
        // System.out.println("onScoreBoard");
        this.scoreBoardCB.launchScoreBoard(gameState.getPlayers().getValue0().getUuid(), TTTScene.GAME_BOARD, gameHistory);
        for(int i=0; i<gameHistory.size(); i++)    
            this.gameHistory.remove(i);
    }

    /************************************************************************************************************
     * SUBSCRIPTION HANDLERS
     ************************************************************************************************************/

    private void onGameStatePatch(GameState.Patch patch){
        System.out.println("onGameStatePatch");
        if(patch.getCurrentPlayer() != null){
            System.out.println("patch.getCurrentPlayer.getIsAi(): " + patch.getCurrentPlayer().getIsAI());
        } else{
            System.out.println("patch.getCurrentPlayer: " + patch.getCurrentPlayer());
        }
        if(patch.getCurrentPlayer() != null && patch.getCurrentPlayer().getIsAI()){
            System.out.println("is AI");
            Ai aiPlayer = (Ai)patch.getCurrentPlayer();
            Pair<Integer, Integer> move = aiPlayer.generateMove(gameState);
            System.out.println(move);
            gameState.setCell(move.getValue0(), move.getValue1());
        }
    }

    private void onPlayerPatch(Player player, ImageView iv, Player.Patch patch){
        System.out.println("onPlayerPatch");
        if(patch.getColor() != null || patch.getShape() != null){
            updateImage(iv, player);
        }
    }
}
