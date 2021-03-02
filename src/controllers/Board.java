package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

import javax.lang.model.type.NullType;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import models.Player;
import models.ColorScheme;

public class Board{
    private final String ASSETS_DIRECTORY = "/assets/images/";
    
    private Player player;
    private Player winner;
    private Player players[] = new Player[2];
    private boolean draw = false;
    private int boardCount;
    private int playerSwitch;
    private int[][] boardNum = new int[3][3];
    private ArrayList<Subscription> subscriptions;
    private HashMap<UUID, ArrayList<ImageView>> views;

    public Board(){
        this.player = null;
        this.subscriptions = new ArrayList<Subscription>();
        this.views = new HashMap<UUID, ArrayList<ImageView>>();
    }

    @FXML
    private StackPane leftTop;
    @FXML
    private StackPane centerTop;
    @FXML
    private StackPane rightTop;
    @FXML
    private StackPane leftMid;
    @FXML
    private StackPane centerMid;
    @FXML
    private StackPane rightMid;
    @FXML
    private StackPane leftBtm;
    @FXML
    private StackPane centerBtm;
    @FXML
    private StackPane rightBtm;


    @FXML
    private void handleLeftTop(MouseEvent e) {
        if(!player.getIsAI() && boardNum[0][0]==0){
            leftTop.getChildren().add(createImageView());
            playerSwitch = (playerSwitch+1)%2;
            boardNum[0][0] = playerSwitch+1;
            boardCount++;
            this.setPlayer(players[playerSwitch]);
        }
    }

    @FXML
    private void handleCenterTop(MouseEvent e) {
        if(!player.getIsAI() && boardNum[0][1]==0){
            centerTop.getChildren().add(createImageView());
            playerSwitch = (playerSwitch+1)%2;
            boardNum[0][1] = playerSwitch+1;
            boardCount++;
            this.setPlayer(players[playerSwitch]);
        }
    }

    @FXML
    private void handleRightTop(MouseEvent e) {
        if(!player.getIsAI() && boardNum[0][2]==0){
            rightTop.getChildren().add(createImageView());
            playerSwitch = (playerSwitch+1)%2;
            boardNum[0][2] = playerSwitch+1;
            boardCount++;
            this.setPlayer(players[playerSwitch]);
        }
    }

    @FXML
    private void handleLeftMid(MouseEvent e) {
        
        if(!player.getIsAI() && boardNum[1][0]==0){
            leftMid.getChildren().add(createImageView());
            playerSwitch = (playerSwitch+1)%2;
            boardNum[1][0] = playerSwitch+1;
            boardCount++;
            this.setPlayer(players[playerSwitch]);
        }
    }

    @FXML
    private void handleCenterMid(MouseEvent e) {
        if(!player.getIsAI() && boardNum[1][1]==0){
            centerMid.getChildren().add(createImageView());
            playerSwitch = (playerSwitch+1)%2;
            boardNum[1][1] = playerSwitch+1;
            boardCount++;
            this.setPlayer(players[playerSwitch]);
        }
    }

    @FXML
    private void handleRightMid(MouseEvent e) {
        if(!player.getIsAI() && boardNum[1][2]==0){
            rightMid.getChildren().add(createImageView());
            playerSwitch = (playerSwitch+1)%2;
            boardNum[1][2] = playerSwitch+1;
            boardCount++;
            this.setPlayer(players[playerSwitch]);
        }
    }

    @FXML
    private void handleLeftBtm(MouseEvent e) {
        if(!player.getIsAI() && boardNum[2][0]==0){
            leftBtm.getChildren().add(createImageView());
            playerSwitch = (playerSwitch+1)%2;
            boardNum[2][0] = playerSwitch+1;
            boardCount++;
            this.setPlayer(players[playerSwitch]);
        }
    }

    @FXML
    private void handleCenterBtm(MouseEvent e) {
        if(!player.getIsAI() && boardNum[2][1]==0){
            centerBtm.getChildren().add(createImageView());
            playerSwitch = (playerSwitch+1)%2;
            boardNum[2][1] = playerSwitch+1;
            boardCount++;
            this.setPlayer(players[playerSwitch]);
        }
    }

    @FXML
    private void handleRightBtm(MouseEvent e) {
        if(!player.getIsAI() && boardNum[2][2]==0){
            rightBtm.getChildren().add(createImageView());
            playerSwitch = (playerSwitch+1)%2;
            boardNum[2][2] = playerSwitch+1;
            boardCount++;
            this.setPlayer(players[playerSwitch]);
        }
    }

    private ImageView createImageView(){
        final String newUrl = ASSETS_DIRECTORY.concat(player.getShape().getFilename());
        ImageView iv = new ImageView(new Image(newUrl));
        ColorScheme.adjustImageColor(iv, player.getColor());
        iv.setFitHeight(100);
        iv.setFitWidth(100);
        if(!views.containsKey(player.getUuid())){
            views.put(player.getUuid(), new ArrayList<ImageView>());
        }
        views.get(player.getUuid()).add(iv);
        return iv;
    }

    private void updateImage(ImageView iv, Player playerToUpdate){
        final String newUrl = ASSETS_DIRECTORY.concat(player.getShape().getFilename());
        if(iv.getImage() == null || !iv.getImage().getUrl().equals(newUrl)){
            final Image newImage = new Image(newUrl);
            iv.setImage(newImage);
        }
        ColorScheme.adjustImageColor(iv, player.getColor());
    }

    public void setPlayer(Player playerToSet) {
        this.player = playerToSet;
        this.player.subscribe(new Subscriber<NullType>(){
			@Override
			public void onSubscribe(Subscription subscription) {
                subscriptions.add(subscription);
            }

			@Override
			public void onNext(NullType item) {
                System.out.println("PlayerToSet: " + playerToSet == null ? "null" : playerToSet.getName());
                views.get(playerToSet.getUuid()).forEach((ImageView iv) -> {
                    updateImage(iv, playerToSet);
                });
			}

			@Override
			public void onError(Throwable throwable) { }

			@Override
			public void onComplete() { }
        });
    }

    public void setPlayers(Player playerOne, Player playerTwo){
        this.players[0] = playerOne;
        this.players[1] = playerTwo;
        this.setPlayer(playerOne);
        this.playerSwitch = 0;
    } 

    public boolean isEnding(){
        if((boardNum[0][1]==boardNum[1][1] && boardNum[1][1]==boardNum[2][1]) ||
        (boardNum[1][0]==boardNum[1][1] && boardNum[1][1]==boardNum[1][2]) ||
        (boardNum[0][0]==boardNum[1][1] && boardNum[1][1]==boardNum[2][2]) ||
        (boardNum[0][2]==boardNum[1][1] && boardNum[1][1]==boardNum[2][0])){
            winner = players[boardNum[1][1]-1];
            draw = false;
            return true;
        }
        else if((boardNum[0][0]==boardNum[0][1] && boardNum[0][1]==boardNum[0][2]) ||
        (boardNum[0][0]==boardNum[1][0] && boardNum[1][0]==boardNum[2][0])){
            winner = players[boardNum[0][0]-1];
            draw = false;
            return true;
        }
        else if((boardNum[0][2]==boardNum[1][2] && boardNum[1][2]==boardNum[2][2]) ||
        (boardNum[2][0]==boardNum[2][1] && boardNum[2][1]==boardNum[2][2])){
            winner = players[boardNum[2][2]-1];
            draw = false;
            return true;
        }
        else if(boardCount == 9){
            draw = true;
            return true;
        }
        return false;
    }

    public Player getWinner(){  return winner;}
    public boolean isDraw(){    return draw;}
}