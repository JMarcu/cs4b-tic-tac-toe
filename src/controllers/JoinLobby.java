package controllers;

import interfaces.CallBackable;
import java.net.URL;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import models.ColorScheme;
import models.Lobby;
import models.MusicPlayer;
import models.SceneCallback.LaunchCreateLobbyCallback;
import models.SceneCallback.LaunchGameCallback;
import models.SceneCallback.LaunchMainMenuCallback;
import models.SceneCallback.LaunchOptionsMenuCallback;
import models.SceneCallback.ReturnToCallback;
import services.LobbyService;

public class JoinLobby {
    private LaunchCreateLobbyCallback createLobbyCB;
    private LaunchGameCallback launchGameCB;
    private LaunchMainMenuCallback mainMenuCB;
    private MusicPlayer music;
    private LaunchOptionsMenuCallback optionsCB;
    private ReturnToCallback returnToCB;

    @FXML private Button backBtn;
    @FXML private Button createLobbyBtn;
    @FXML private ImageView gearIV;
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
        
        //Color the options button to fit the color scheme.
        ColorScheme.adjustImageColor(gearIV, ColorScheme.TEXT_ON_SECONDARY.getColor());

        this.lobbyColumn.prefWidthProperty().bind(lobbyTable.widthProperty().subtract(3 * FIXED_COLUMN_WIDTH));
        
        this.joinBtn.disableProperty().set(true);
        this.spectateBtn.disableProperty().set(true);

        this.loadLobbies();
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
        try {
            LobbyService.getInstance().joinLobby(
                this.lobbyTable.getSelectionModel().getSelectedItem().getId(),
                new CallBackable(){
                    @Override
                    public void callback() {
                        Platform.runLater(new Runnable(){
                            @Override
                            public void run() {
                                JoinLobby.this.launchGameCB.launchGame();
                            }
                        });
                    }
                }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onOptions(ActionEvent event){
        this.optionsCB.launchOptionsMenu("");
    }

    @FXML
    private void onPlayOfflineAction(ActionEvent event){
        this.mainMenuCB.launchMainMenu();
    }

    @FXML
    private void onRefreshAction(ActionEvent event) {
        this.loadLobbies();
    }

    @FXML
    private void onSpectateAction(ActionEvent event) {
        try {
            LobbyService.getInstance().spectate(
                this.lobbyTable.getSelectionModel().getSelectedItem().getId(),
                new CallBackable(){
                    @Override
                    public void callback() {
                        Platform.runLater(new Runnable(){
                            @Override
                            public void run() {
                                JoinLobby.this.launchGameCB.launchGame();
                            }
                        });
                    }
                }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BorderPane getRoot(){ 
        return this.root; 
    }

    public void loadLobbies(){
        try {
            LobbyService.getInstance().listLobbies(new Consumer<ArrayList<Lobby>>(){
                @Override
                public void accept(ArrayList<Lobby> lobbies) {
                    JoinLobby.this.onLobbyList(lobbies);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLaunchCreateLobbyCB(LaunchCreateLobbyCallback createLobbyCB){
        this.createLobbyCB = createLobbyCB;
    }

    public void setLaunchGameCB(LaunchGameCallback launchGameCB){
        this.launchGameCB = launchGameCB;
    }

    public void setLaunchMainMenuCB(LaunchMainMenuCallback mainMenuCB){
        this.mainMenuCB = mainMenuCB;
    }

    public void setLaunchOptionsMenuCB(LaunchOptionsMenuCallback optionsCB){
        this.optionsCB = optionsCB;
    }

    public void setReturnToCB(ReturnToCallback returnToCB){
        this.returnToCB = returnToCB;
    }

    private void onLobbyList(ArrayList<Lobby> lobbies){
        ObservableList<Lobby> observableLobbies = FXCollections.observableArrayList(lobbies);
        this.lobbyTable.setItems(observableLobbies);
        this.lobbyTable.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<Lobby>(){
            @Override
            public void onChanged(Change<? extends Lobby> change) {
                JoinLobby.this.joinBtn.disableProperty().set(change.getList().size() == 0);
                JoinLobby.this.spectateBtn.disableProperty().set(change.getList().size() == 0);
            }
        });

        this.lobbyColumn.setCellValueFactory(new PropertyValueFactory<Lobby, String>("name"));
        this.playersColumn.setCellValueFactory(new PropertyValueFactory<Lobby, String>("playerCount"));
        this.spectatorsColumn.setCellValueFactory(new PropertyValueFactory<Lobby, String>("spectatorCount"));
        this.statusColumn.setCellValueFactory(new PropertyValueFactory<Lobby, String>("statusString"));
    }
}