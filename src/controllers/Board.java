package controllers;

import models.Ai;

import java.util.ArrayList;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.HashMap;
import java.util.UUID;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import models.ColorScheme;
import models.GameState;
import models.Player;

public class Board {
    private GameState                           gameState;
    private Subscription                        gameStateSubscription;
    private ImageView[][]                       imageViewGrid;
    private HashMap<UUID, ArrayList<ImageView>> playerIVMap;
    private Subscription                        playerOneSubscription;
    private Subscription                        playerTwoSubscription;
    private boolean                             viewInit;

    @FXML private ImageView leftTop;
    @FXML private ImageView centerTop;
    @FXML private ImageView rightTop;
    @FXML private ImageView leftMid;
    @FXML private ImageView centerMid;
    @FXML private ImageView rightMid;
    @FXML private ImageView leftBtm;
    @FXML private ImageView centerBtm;
    @FXML private ImageView rightBtm;

    private final String ASSETS_DIRECTORY = "/assets/images/";

    public Board() {
        this.imageViewGrid = null;
        this.gameState = null;
        this.gameStateSubscription = null;
        this.playerIVMap = new HashMap<UUID, ArrayList<ImageView>>();
        this.playerOneSubscription = null;
        this.playerTwoSubscription = null;
        this.viewInit = false;
    }

    @FXML
    private void initialize() {
        this.imageViewGrid = new ImageView[3][3];
        this.imageViewGrid[0][0] = leftTop;
        this.imageViewGrid[0][1] = centerTop;
        this.imageViewGrid[0][2] = rightTop;
        this.imageViewGrid[1][0] = leftMid;
        this.imageViewGrid[1][1] = centerMid;
        this.imageViewGrid[1][2] = rightMid;
        this.imageViewGrid[2][0] = leftBtm;
        this.imageViewGrid[2][1] = centerBtm;
        this.imageViewGrid[2][2] = rightBtm;
        this.viewInit = true;
        if(this.gameState != null){
            this.initializeIVGrid();
        }
    }

    /************************************************************************************************************
     * ACCESSORS & MUTATORS
     ************************************************************************************************************/
    public void setGameState(GameState gameState){
        this.gameState = gameState;

        if(this.gameStateSubscription != null){ this.gameStateSubscription.cancel(); }
        if(this.playerOneSubscription != null){ this.playerOneSubscription.cancel(); }
        if(this.playerTwoSubscription != null){ this.playerTwoSubscription.cancel(); }
        
        this.gameState.subscribe(new Subscriber<GameState.Patch>(){
			@Override public void onSubscribe(Subscription subscription) {gameStateSubscription = subscription; }
			@Override public void onNext(GameState.Patch item) { onGameStatePatch(item); }
			@Override public void onError(Throwable throwable) { }
			@Override public void onComplete() { }
        });
        
        this.gameState.getPlayers().getValue0().subscribe(new Subscriber<Player.Patch>(){
			@Override public void onSubscribe(Subscription subscription) { 
                playerOneSubscription = subscription; 
                playerOneSubscription.request(1);
            }
			@Override public void onNext(Player.Patch item) { 
                onPlayerPatch(gameState.getPlayers().getValue0(), item); 
                playerOneSubscription.request(1);
            }
			@Override public void onError(Throwable throwable) { }
			@Override public void onComplete() { }
        });

        this.gameState.getPlayers().getValue1().subscribe(new Subscriber<Player.Patch>(){
			@Override public void onSubscribe(Subscription subscription) { 
                playerTwoSubscription = subscription; 
                playerTwoSubscription.request(1);
            }
			@Override public void onNext(Player.Patch item) { 
                onPlayerPatch(gameState.getPlayers().getValue1(), item); 
                playerTwoSubscription.request(1);
            }
			@Override public void onError(Throwable throwable) { }
			@Override public void onComplete() { }
        });

        if(this.viewInit){
            this.initializeIVGrid();
        }
    }

    private void createCellImage(Player player, ImageView iv) {
        final String newUrl = ASSETS_DIRECTORY.concat(player.getShape().getFilename());
        iv.setImage(new Image(newUrl));
        ColorScheme.adjustImageColor(iv, player.getColor());
    }

    private void initializeIVGrid(){
        playerIVMap = new HashMap<UUID, ArrayList<ImageView>>();
        this.playerIVMap.put(gameState.getPlayers().getValue0().getUuid(), new ArrayList<ImageView>());
        this.playerIVMap.put(gameState.getPlayers().getValue1().getUuid(), new ArrayList<ImageView>());

        for(int i = 0; i < imageViewGrid.length; i++){
            for(int j = 0; j < imageViewGrid[i].length; j++){
                if(gameState.getCell(i, j) != null){
                    createCellImage(gameState.getCell(i, j), imageViewGrid[i][j]);
                    this.playerIVMap.get(gameState.getCell(i, j).getUuid()).add(imageViewGrid[i][j]);
                }
            }
        }
    }

    /************************************************************************************************************
     * EVENT HANDLERS
     ************************************************************************************************************/

    @FXML private void handleLeftTop(MouseEvent e)   { gameState.setCell(0, 0); }
    @FXML private void handleCenterTop(MouseEvent e) { gameState.setCell(0, 1); }
    @FXML private void handleRightTop(MouseEvent e)  { gameState.setCell(0, 2); }
    @FXML private void handleLeftMid(MouseEvent e)   { gameState.setCell(1, 0); }
    @FXML private void handleCenterMid(MouseEvent e) { gameState.setCell(1, 1); }
    @FXML private void handleRightMid(MouseEvent e)  { gameState.setCell(1, 2); }
    @FXML private void handleLeftBtm(MouseEvent e)   { gameState.setCell(2, 0); }
    @FXML private void handleCenterBtm(MouseEvent e) { gameState.setCell(2, 1); }
    @FXML private void handleRightBtm(MouseEvent e)  { gameState.setCell(2, 2); }
    
    // private void AiPlay(){
        

    // } 

    /************************************************************************************************************
     * SUBSCRIPTION HANDLERS
     ************************************************************************************************************/

    private void onGameStatePatch(GameState.Patch patch){
        if(patch.getStatus() == GameState.Status.WON){
            //TODO Transition to victory splash screen.
        } else if(patch.getStatus() == GameState.Status.DRAW){
            //TODO Transition to draw splash screen.
        } else if(patch.getMove() != null){
            this.createCellImage(patch.getMove().getValue0(), imageViewGrid[patch.getMove().getValue1()][patch.getMove().getValue2()]);
            this.playerIVMap.get(patch.getMove().getValue0().getUuid()).add(imageViewGrid[patch.getMove().getValue1()][patch.getMove().getValue2()]);
            System.out.println("this.playerIVMap.get(patch.getMove().getValue0().getUuid()).size(): " + this.playerIVMap.get(patch.getMove().getValue0().getUuid()).size());
        }
    }

    private synchronized void onPlayerPatch(Player player, Player.Patch patch){
        if(patch.getColor() != null){
            this.playerIVMap.get(player.getUuid()).forEach((ImageView iv) -> {
                ColorScheme.adjustImageColor(iv, patch.getColor());
            });
        }

        if(patch.getShape() != null){
            for(int i = 0; i < this.playerIVMap.get(player.getUuid()).size(); i++){
                this.createCellImage(player, this.playerIVMap.get(player.getUuid()).get(i));
            }
        }
    }
}