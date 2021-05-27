package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.*;
import models.Player;
import models.SceneCallback.ReturnToCallback;
import models.Game;

public class GameHistoryController {

    @FXML
    private ScrollPane root;
    @FXML private TableColumn GameId;
    @FXML private TableColumn CreatedBy;
    @FXML private TableColumn PlayerOne;
    @FXML private TableColumn PlayerTwo;
    @FXML private TableColumn GameMoves;
    @FXML private TableColumn Winner;
    @FXML private TableColumn StartTime;
    @FXML private TableColumn EndTime;
    @FXML private TableColumn Spectators;
    @FXML private TableView Table;


    private ReturnToCallback returnCB;

    public ScrollPane getRoot(){ return this.root; }

    @FXML 
    protected void ReturnButtonClicked(ActionEvent event) {
        returnCB.returnTo();
    }

    public void addGame(Game game)
    {
        
    }

    public void set()
    {

    }

    public void setReturnCB(ReturnToCallback returnCB){
        this.returnCB = returnCB;
    }
    
}
