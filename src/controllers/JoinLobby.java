package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import models.Lobby;
import models.MusicPlayer;
import models.SceneCallback.LaunchCreateLobbyCallback;
import models.SceneCallback.ReturnToCallback;

public class JoinLobby {
    private LaunchCreateLobbyCallback createLobbyCB;
    private MusicPlayer music;
    private ReturnToCallback returnToCB;

    @FXML private Button backBtn;
    @FXML private Button createLobbyBtn;
    @FXML private Button joinBtn;
    @FXML private TableColumn<Lobby, String> lobbyColumn;
    @FXML private TableView<Lobby> lobbyTable;
    @FXML private URL location;
    @FXML private TableColumn<Lobby, String> playersColumn;
    @FXML private Button refreshBtn;
    @FXML private ResourceBundle resources;
    @FXML private BorderPane root;
    @FXML private Button spectateBtn;
    @FXML private TableColumn<Lobby, String> spectatorsColumn;
    @FXML private TableColumn<Lobby, String> statusColumn;

    private final int FIXED_COLUMN_WIDTH = 90;

    public JoinLobby(){
        music = new MusicPlayer();
    }

    @FXML
    void initialize() {
        root.getStylesheets().add(getClass().getResource("/styles/color-theme.css").toExternalForm());
        root.getStylesheets().add(getClass().getResource("/styles/join-lobby.css").toExternalForm());

        this.lobbyColumn.prefWidthProperty().bind(lobbyTable.widthProperty().subtract(3 * FIXED_COLUMN_WIDTH));

        ObservableList<Lobby> lobbies = FXCollections.observableArrayList();
        lobbies.add(new Lobby("Lobby One"));
        lobbies.add(new Lobby("Lobby Two"));
        lobbies.add(new Lobby("Lobby Three"));
        lobbies.add(new Lobby("Lobby Four"));
        this.lobbyTable.setItems(lobbies);
        this.lobbyTable.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<Lobby>(){
            @Override
            public void onChanged(Change<? extends Lobby> change) {
                JoinLobby.this.joinBtn.disableProperty().set(change.getList().size() == 0);
                JoinLobby.this.spectateBtn.disableProperty().set(change.getList().size() == 0);
            }
        });
        this.joinBtn.disableProperty().set(true);
        this.spectateBtn.disableProperty().set(true);

        this.lobbyColumn.setCellValueFactory(new PropertyValueFactory<Lobby, String>("name"));
        this.playersColumn.setCellValueFactory(new PropertyValueFactory<Lobby, String>("playerCount"));
        this.spectatorsColumn.setCellValueFactory(new PropertyValueFactory<Lobby, String>("spectatorCount"));
        this.statusColumn.setCellValueFactory(new PropertyValueFactory<Lobby, String>("statusString"));
    }

    @FXML
    void onBackAction(ActionEvent event) {
        if (music.getShouldPlaySFX()){
            MusicPlayer music2 = new MusicPlayer();
            music2.playSFX(MusicPlayer.Track.exitMenu);
        }
        returnToCB.returnTo();
    }

    @FXML
    private void onCreateLobbyAction(ActionEvent event) {
        this.createLobbyCB.launchCreateLobby();
    }

    @FXML
    private void onJoinAction(ActionEvent event) {

    }

    @FXML
    private void onRefreshAction(ActionEvent event) {

    }

    @FXML
    private void onSpectateAction(ActionEvent event) {

    }

    public BorderPane getRoot(){ 
        return this.root; 
    }

    public void setLaunchCreateLobbyCB(LaunchCreateLobbyCallback createLobbyCB){
        this.createLobbyCB = createLobbyCB;
    }

    public void setReturnToCB(ReturnToCallback returnToCB){
        this.returnToCB = returnToCB;
    }
}