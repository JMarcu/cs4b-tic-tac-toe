package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.*;
import models.Player;
import models.SceneCallback.ReturnToCallback;
import models.Game;

public class GameHistoryController {

    @FXML
    private ScrollPane root;

    private ReturnToCallback returnCB;

    public ScrollPane getRoot(){ return this.root; }

    @FXML 
    protected void ReturnButtonClicked(ActionEvent event) {
        returnCB.returnTo();
    }

    public void addGame(Game game)
    {

    }

    public void setReturnCB(ReturnToCallback returnCB){
        this.returnCB = returnCB;
    }
    
}
