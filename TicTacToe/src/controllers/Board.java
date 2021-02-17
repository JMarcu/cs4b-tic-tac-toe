package controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import models.Player;

public class Board{
    private final String ASSETS_DIRECTORY = "/assets/images/";
    Player player;
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
        final String newUrl = ASSETS_DIRECTORY.concat(player.getShape().getFilename());//UNTESTED
        ImageView iv = new ImageView(new Image(newUrl));
        leftTop.getChildren().add(iv);
    }

    @FXML
    private void handlecenterTop(MouseEvent e) {
        final String newUrl = ASSETS_DIRECTORY.concat(player.getShape().getFilename());//UNTESTED
        ImageView iv = new ImageView(new Image(newUrl));
        centerTop.getChildren().add(iv);
    }

    @FXML
    private void handleRightTop(MouseEvent e) {
        final String newUrl = ASSETS_DIRECTORY.concat(player.getShape().getFilename());//UNTESTED
        ImageView iv = new ImageView(new Image(newUrl));
        rightTop.getChildren().add(iv);
    }

    @FXML
    private void handleLeftMid(MouseEvent e) {
        final String newUrl = ASSETS_DIRECTORY.concat(player.getShape().getFilename());//UNTESTED
        ImageView iv = new ImageView(new Image(newUrl));
        leftMid.getChildren().add(iv);
    }

    @FXML
    private void handleCenterMid(MouseEvent e) {
        final String newUrl = ASSETS_DIRECTORY.concat(player.getShape().getFilename());//UNTESTED
        ImageView iv = new ImageView(new Image(newUrl));
        centerMid.getChildren().add(iv);
    }

    @FXML
    private void handleRightMid(MouseEvent e) {
        final String newUrl = ASSETS_DIRECTORY.concat(player.getShape().getFilename());//UNTESTED
        ImageView iv = new ImageView(new Image(newUrl));
        rightMid.getChildren().add(iv);
    }

    @FXML
    private void handleLeftBtm(MouseEvent e) {
        final String newUrl = ASSETS_DIRECTORY.concat(player.getShape().getFilename());//UNTESTED
        ImageView iv = new ImageView(new Image(newUrl));
        leftBtm.getChildren().add(iv);
    }

    @FXML
    private void handleCenterBtm(MouseEvent e) {
        final String newUrl = ASSETS_DIRECTORY.concat(player.getShape().getFilename());//UNTESTED
        ImageView iv = new ImageView(new Image(newUrl));
        centerBtm.getChildren().add(iv);
    }

    @FXML
    private void handleRightBtm(MouseEvent e) {
        final String newUrl = ASSETS_DIRECTORY.concat(player.getShape().getFilename());//UNTESTED
        ImageView iv = new ImageView(new Image(newUrl));
        rightBtm.getChildren().add(iv);
    }

    @FXML
    private void setPlayerObject(Player p) {
        if(!p.getIsAI()){
            player.setColor(p.getColor());
            player.setName(p.getName());
            player.setShape(p.getShape());
        }
    }
}