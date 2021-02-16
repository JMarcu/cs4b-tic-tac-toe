package controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class Board{
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
        // Image i = new Image("x.jpg");
        Image i = new Image("assets\\images/"); //FIXING
        ImageView iv = new ImageView(i);
        leftTop.getChildren().add(iv);
    }

    @FXML
    private void handlecenterTop(MouseEvent e) {
        // Image i = new Image("x.jpg");
        Image i = new Image("X.png");
        ImageView iv = new ImageView(i);
        centerTop.getChildren().add(iv);
    }

    @FXML
    private void handleRightTop(MouseEvent e) {
        // Image i = new Image("x.jpg");
        Image i = new Image("X.png");
        ImageView iv = new ImageView(i);
        rightTop.getChildren().add(iv);
    }

    @FXML
    private void handleLeftMid(MouseEvent e) {
        // Image i = new Image("x.jpg");
        Image i = new Image("X.png");
        ImageView iv = new ImageView(i);
        leftMid.getChildren().add(iv);
    }

    @FXML
    private void handleCenterMid(MouseEvent e) {
        // Image i = new Image("x.jpg");
        Image i = new Image("X.png");
        ImageView iv = new ImageView(i);
        centerMid.getChildren().add(iv);
    }

    @FXML
    private void handleRightMid(MouseEvent e) {
        // Image i = new Image("x.jpg");
        Image i = new Image("X.png");
        ImageView iv = new ImageView(i);
        rightMid.getChildren().add(iv);
    }

    @FXML
    private void handleLeftBtm(MouseEvent e) {
        // Image i = new Image("x.jpg");
        Image i = new Image("X.png");
        ImageView iv = new ImageView(i);
        leftBtm.getChildren().add(iv);
    }

    @FXML
    private void handleCenterBtm(MouseEvent e) {
        // Image i = new Image("x.jpg");
        Image i = new Image("X.png");
        ImageView iv = new ImageView(i);
        centerBtm.getChildren().add(iv);
    }

    @FXML
    private void handleRightBtm(MouseEvent e) {
        // Image i = new Image("x.jpg");
        Image i = new Image("X.png");
        ImageView iv = new ImageView(i);
        rightBtm.getChildren().add(iv);
    }

	//set PlayerObject
    //wrap logic in if statement, check isAI
}