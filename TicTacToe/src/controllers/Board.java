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
import models.Color;

public class Board{
    private final String ASSETS_DIRECTORY = "/assets/images/";
    
    private Player player;
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
        if(!player.getIsAI()){
            leftTop.getChildren().add(createImageView());
        }
    }

    @FXML
    private void handleCenterTop(MouseEvent e) {
        if(!player.getIsAI()){
            centerTop.getChildren().add(createImageView());
        }
    }

    @FXML
    private void handleRightTop(MouseEvent e) {
        if(!player.getIsAI()){
            rightTop.getChildren().add(createImageView());
        }
    }

    @FXML
    private void handleLeftMid(MouseEvent e) {
        
        if(!player.getIsAI()){
            leftMid.getChildren().add(createImageView());
        }
    }

    @FXML
    private void handleCenterMid(MouseEvent e) {
        if(!player.getIsAI()){
            centerMid.getChildren().add(createImageView());
        }
    }

    @FXML
    private void handleRightMid(MouseEvent e) {
        if(!player.getIsAI()){
            rightMid.getChildren().add(createImageView());
        }
    }

    @FXML
    private void handleLeftBtm(MouseEvent e) {
        if(!player.getIsAI()){
            leftBtm.getChildren().add(createImageView());
        }
    }

    @FXML
    private void handleCenterBtm(MouseEvent e) {
        if(!player.getIsAI()){
            centerBtm.getChildren().add(createImageView());
        }
    }

    @FXML
    private void handleRightBtm(MouseEvent e) {
        if(!player.getIsAI()){
            rightBtm.getChildren().add(createImageView());
        }
    }

    private ImageView createImageView(){
        final String newUrl = ASSETS_DIRECTORY.concat(player.getShape().getFilename());
        ImageView iv = new ImageView(new Image(newUrl));
        Color.adjustImageColor(iv, player.getColor());
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
        Color.adjustImageColor(iv, player.getColor());
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
}