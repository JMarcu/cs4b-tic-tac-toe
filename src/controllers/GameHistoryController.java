package controllers;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.util.Pair;
import models.Player;
import models.SceneCallback.ReturnToCallback;
import models.Game;
import models.GameForTable;

public class GameHistoryController {

    @FXML
    private ScrollPane root;
    @FXML private TableColumn<GameForTable, Integer> GameId;
    @FXML private TableColumn<GameForTable, String> CreatedBy;
    @FXML private TableColumn<GameForTable, String> PlayerOne;
    @FXML private TableColumn<GameForTable, String> PlayerTwo;
    @FXML private TableColumn<GameForTable, Pair<String,Long>> GameMoves;
    @FXML private TableColumn<GameForTable, String> Winner;
    @FXML private TableColumn<GameForTable, Long> StartTime;
    @FXML private TableColumn<GameForTable, Long> EndTime;
    @FXML private TableColumn<GameForTable, String> Spectators;
    @FXML private TableView<GameForTable> Table;

    private final int FIXED_COLUMN_WIDTH = 97;



    private ReturnToCallback returnCB;

    public ScrollPane getRoot(){ return this.root; }

    @FXML void initialize(){
        root.getStylesheets().add(getClass().getResource("/styles/color-theme.css").toExternalForm());
        root.getStylesheets().add(getClass().getResource("/styles/gameHistory.css").toExternalForm());

        this.GameMoves.prefWidthProperty().bind(Table.widthProperty().divide(31/5));
        this.GameId.prefWidthProperty().bind(Table.widthProperty().divide(31/2));
        this.CreatedBy.prefWidthProperty().bind(Table.widthProperty().divide(31/3));
        this.PlayerOne.prefWidthProperty().bind(Table.widthProperty().divide(31/3));
        this.PlayerTwo.prefWidthProperty().bind(Table.widthProperty().divide(31/3));
        this.Winner.prefWidthProperty().bind(Table.widthProperty().divide(31/3));
        this.StartTime.prefWidthProperty().bind(Table.widthProperty().divide(31/3));
        this.EndTime.prefWidthProperty().bind(Table.widthProperty().divide(31/3));
        this.Spectators.prefWidthProperty().bind(Table.widthProperty().divide(31/5));
    }

    @FXML 
    protected void ReturnButtonClicked(ActionEvent event) {
        returnCB.returnTo();
    }

    public void addGames(List<GameForTable> games)
    {

        ObservableList<GameForTable> observableLobbies = FXCollections.observableArrayList(games);
        this.Table.setItems(observableLobbies);
      
        this.GameId.setCellValueFactory(new PropertyValueFactory<GameForTable, Integer>("gameId"));
        this.CreatedBy.setCellValueFactory(new PropertyValueFactory<GameForTable, String>("creator"));
        this.PlayerOne.setCellValueFactory(new PropertyValueFactory<GameForTable, String>("playerOne"));
        this.PlayerTwo.setCellValueFactory(new PropertyValueFactory<GameForTable, String>("playerTwo"));
        this.GameMoves.setCellValueFactory(new PropertyValueFactory<GameForTable, Pair<String,Long>>("moves"));
        this.Winner.setCellValueFactory(new PropertyValueFactory<GameForTable, String>("winner"));
        this.StartTime.setCellValueFactory(new PropertyValueFactory<GameForTable, Long>("start"));
        this.EndTime.setCellValueFactory(new PropertyValueFactory<GameForTable, Long>("end"));
        this.Spectators.setCellValueFactory(new PropertyValueFactory<GameForTable, String>("spectators"));

        
    }

    public void set()
    {

    }

    public void setReturnCB(ReturnToCallback returnCB){
        this.returnCB = returnCB;
    }
    
}
