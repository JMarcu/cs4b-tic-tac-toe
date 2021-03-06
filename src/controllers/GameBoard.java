package controllers;

import java.util.concurrent.Flow.*;
import org.javatuples.Pair;
import java.util.UUID;
import java.util.Vector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.ScrollPane;
import models.Ai;
import models.ColorScheme;
import models.GameState;
import models.Player;
import models.SceneCallback.LaunchOptionsMenuCallback;
import models.SceneCallback.LaunchScoreBoardCallback;
import models.SceneCallback.LaunchShapePickerCallback;
import services.AuthService;
import services.GameStateService;
import models.TTTScene;

public class GameBoard{
    
    private Vector<GameState>         gameHistory = new Vector<GameState>();
    private Subscription              gameStateSubscription;
    private Subscription              gameStatePatchSubscription;
    private LaunchOptionsMenuCallback optionsMenuCB;
    private Subscription              playerOneSubscription;
    private Subscription              playerTwoSubscription;
    private boolean                   read;
    private LaunchShapePickerCallback shapePickerCB;
    private LaunchScoreBoardCallback  scoreBoardCB;
    private boolean                   viewInit;

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
        this.gameStatePatchSubscription = null;
        this.optionsMenuCB = null;
        this.playerOneSubscription = null;
        this.playerTwoSubscription = null;
        this.shapePickerCB = null;
        this.scoreBoardCB = null;
        this.viewInit = false;

        GameState none = new GameState();
        this.gameHistory.add(none);

        GameStateService.getInstance().subscribe(new Subscriber<GameState>(){
            @Override public void onSubscribe(Subscription subscription) { 
                gameStateSubscription = subscription;
                gameStateSubscription.request(1);
            }
            @Override public void onNext(GameState item) { 
                if(item != null){
                    onGameState(); 
                }
                gameStateSubscription.request(1);
            };
            @Override public void onError(Throwable throwable) { }
            @Override public void onComplete() { }
        });
    }
    
    @FXML 
    private void initialize(){ 
        this.viewInit = true; 
        this.read = false;

        if(GameStateService.getInstance().getGameState() != null){
            finallyInitialize();
        }

        this.root.getStylesheets().add(getClass().getResource("/styles/color-theme.css").toExternalForm());
        this.root.getStylesheets().add(getClass().getResource("/styles/game-board.css").toExternalForm());

        ColorScheme.adjustImageColor(options, ColorScheme.TEXT_ON_SECONDARY.getColor());
    }

    private void finallyInitialize(){
        read = false;

        this.updatePlayerViews();

        GameState gameState = GameStateService.getInstance().getGameState();
        if(gameState.getStatus() == GameState.Status.IN_PROGRESS){
            boardController.setEnable();
        } else{
            boardController.setDisable();
        }

        if(gameState.getOnline()){
            this.playerOneTF.setDisable(true);
            this.playerTwoTF.setDisable(true);
        }
    }

    //Loads data from launchGame
    public void onGameState(){
        try{
            GameState gameState = GameStateService.getInstance().getGameState();

            if(gameStatePatchSubscription != null){ gameStatePatchSubscription.cancel(); }
            
            gameState.subscribe(new Subscriber<GameState.Patch>(){
                @Override public void onSubscribe(Subscription subscription) { 
                    gameStatePatchSubscription = subscription; 
                    gameStatePatchSubscription.request(1);
                }
                @Override public void onNext(GameState.Patch item) { 
                    onGameStatePatch(item); 
                    gameStatePatchSubscription.request(1);
                }
                @Override public void onError(Throwable throwable) { }
                @Override public void onComplete() { }
            });

            this.subscribeToPlayers();

            if(!read){ 
                gameHistory.remove(gameHistory.lastElement());
                gameHistory.add(gameState); read = true;
            }

            gameHistory.add(gameState); read = true;
            
            if(viewInit){
                finallyInitialize();
                read = false;
            }

            setBoardStatus();
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /************************************************************************************************************
     * ACCESSORS & MUTATORS
     ************************************************************************************************************/

    public ScrollPane getRoot(){ return this.root; }

    public void setOptionsMenuCB(LaunchOptionsMenuCallback optionsMenuCB) {this.optionsMenuCB = optionsMenuCB;}
    public void setScoreBoardCB(LaunchScoreBoardCallback scoreBoardCB)    {this.scoreBoardCB = scoreBoardCB;}
    public void setShapePickerCB(LaunchShapePickerCallback shapePickerCB) {this.shapePickerCB = shapePickerCB;}

    //Checks whetheer or not the image is already in use or null and sets it if both ar false
    private void updateImage(ImageView iv, Player player){
        String newUrl = player != null
            ? ASSETS_DIRECTORY.concat(player.getShape().getFilename())
            : ASSETS_DIRECTORY.concat("empty.png");
        if(iv.getImage() == null || !iv.getImage().getUrl().equals(newUrl)){
            final Image newImage = new Image(newUrl);
            iv.setImage(newImage);
        }
        System.out.println("iv.getImage(): " + iv.getImage());
        System.out.println("!iv.getImage().getUrl().equals(newUrl): " + !iv.getImage().getUrl().equals(newUrl));
        if(player != null){
            ColorScheme.adjustImageColor(iv, player.getColor());
        }
    }

    /************************************************************************************************************
     * EVENT HANDLERS
     ************************************************************************************************************/

    @FXML //Sets Playerone's name on the textfield for the gameboard
    private void onPlayerOneTF(KeyEvent event){
        GameState gameState = GameStateService.getInstance().getGameState();
        gameState.getPlayers().getValue0().setName(this.playerOneTF.getText());
    }

    @FXML //Sets Playertwo's name on the textfield for the gameboard
    private void onPlayerTwoTF(KeyEvent event){
        GameState gameState = GameStateService.getInstance().getGameState();
        gameState.getPlayers().getValue1().setName(this.playerTwoTF.getText());
    }

    @FXML //Allows playerone to pick a new shape/image to use for the board by pressig the shape button
    private void onPlayerOneShapeAction(ActionEvent event) {
        GameState gameState = GameStateService.getInstance().getGameState();
        if(!gameState.getOnline()){
            this.shapePickerCB.launchShapePicker(gameState.getPlayers().getValue0());
        }
    }

    @FXML //Allows playertwo to pick a new shape/image to use for the board by pressig the shape button
    private void onPlayerTwoShapeAction(ActionEvent event) {
        GameState gameState = GameStateService.getInstance().getGameState();
        if(!gameState.getOnline()){
            this.shapePickerCB.launchShapePicker(gameState.getPlayers().getValue1());
        }
    }

    @FXML //Allows playerone to use the options menu by pressing the gear button
    private void onOption(ActionEvent event){
        this.optionsMenuCB.launchOptionsMenu("Game");
    }

    @FXML //Allows playerone to access the scoreboard by pressing the scoreboard button
    private void onScoreBoard(ActionEvent event){
        this.scoreBoardCB.launchScoreBoard(TTTScene.GAME_BOARD, gameHistory);
        
        for(int i=0; i<gameHistory.size(); i++)    
            this.gameHistory.remove(i);
        GameState none = new GameState();
        this.gameHistory.add(none);
    }

    /************************************************************************************************************
     * SUBSCRIPTION HANDLERS
     ************************************************************************************************************/

    private void onGameStatePatch(GameState.Patch patch){
        if(patch.getStatus() != null && boardController != null){
            if(patch.getStatus() == GameState.Status.IN_PROGRESS){
                boardController.setEnable();
            } else{
                boardController.setDisable();
            }
        }

        if(patch.getPlayers() != null){
            this.subscribeToPlayers();
            this.updatePlayerViews();
        }

        GameState gameState = GameStateService.getInstance().getGameState();
        if(
            !gameState.getOnline() &&
            patch.getCurrentPlayer() != null && 
            patch.getCurrentPlayer().getIsAI()
        ){
            Ai aiPlayer = (Ai)patch.getCurrentPlayer();
            Pair<Integer, Integer> move = aiPlayer.generateMove(gameState);
            gameState.setCell(move.getValue0(), move.getValue1());
        }

        setBoardStatus();
    }

    private void onPlayerPatch(Player player, ImageView iv, Player.Patch patch){
        if(patch.getColor() != null || patch.getShape() != null){
            updateImage(iv, player);
        }
    }

    private void setBoardStatus(){
        boolean disableBoard = true;
        GameState gameState = GameStateService.getInstance().getGameState();

        //Only enable the board if the game is in progress.
        if(gameState.getStatus() == GameState.Status.IN_PROGRESS){

            if(gameState.getOnline()){
                //For online play, enable the board if the logged in player is the game's current player.
                UUID playerId = AuthService.getInstance().getPlayer().getUuid();
                UUID currentPlayerId = gameState.getCurrentPlayer().getUuid();
                if(playerId.equals(currentPlayerId)){
                    disableBoard = false;
                    boardController.setEnable();
                }
            } else{
                if(!gameState.getSinglePlayer()){
                    //For offline player, enable the board if it is a two player game...
                    disableBoard = false;
                    boardController.setEnable();
                } else if(!gameState.getCurrentPlayer().getIsAI()){
                    //...or if it is not the AI's turn.
                    disableBoard = false;
                    boardController.setEnable();
                }
            }
        }

        //If none of the special conditions above were met, disable the board.
        if(disableBoard){
            boardController.setDisable();
        }
    }

    private void subscribeToPlayers(){
        GameState gameState = GameStateService.getInstance().getGameState();
        if(playerOneSubscription != null){ playerOneSubscription.cancel(); }
        if(playerTwoSubscription != null){ playerTwoSubscription.cancel(); }
           
        if(gameState.getPlayers().getValue0() != null){
            gameState.getPlayers().getValue0().subscribe(new Subscriber<Player.Patch>(){
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
        }

        if(gameState.getPlayers().getValue1() != null){
            gameState.getPlayers().getValue1().subscribe(new Subscriber<Player.Patch>(){
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
        }
    }

    private void updatePlayerViews(){
        System.out.println("updatePlayerViews");

        GameState gameState = GameStateService.getInstance().getGameState();
        
        System.out.println("Setting playerOneTF.text to " + (gameState.getPlayers().getValue0() != null ? gameState.getPlayers().getValue0().getName() : ""));
        playerOneTF.setText(
            gameState.getPlayers().getValue0() != null
            ? gameState.getPlayers().getValue0().getName()
            : ". . ."
        );
        System.out.println("Setting playerTwoTF.text to " + (gameState.getPlayers().getValue1() != null ? gameState.getPlayers().getValue1().getName() : ""));
        playerTwoTF.setText(
            gameState.getPlayers().getValue1() != null
            ? gameState.getPlayers().getValue1().getName()
            : ". . ."
        );

        updateImage(playerOneShapeIV, gameState.getPlayers().getValue0());
        updateImage(playerTwoShapeIV, gameState.getPlayers().getValue1());
    }
}