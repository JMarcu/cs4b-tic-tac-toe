package controllers;

import interfaces.CallBackable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.ResourceBundle;
import java.util.UUID;

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
import javafx.util.Pair;
import models.ColorScheme;
import models.Lobby;
import models.MusicPlayer;
import models.SceneCallback.LaunchCreateLobbyCallback;
import models.SceneCallback.LaunchGameCallback;
import models.SceneCallback.LaunchMainMenuCallback;
import models.SceneCallback.LaunchOptionsMenuCallback;
import models.SceneCallback.LaunchGameHistoryCallback;
import models.SceneCallback.ReturnToCallback;
import models.Game;
import services.LobbyService;

public class JoinLobby {
    private LaunchCreateLobbyCallback createLobbyCB;
    private LaunchGameCallback launchGameCB;
    private LaunchMainMenuCallback mainMenuCB;
    private MusicPlayer music;
    private LaunchOptionsMenuCallback optionsCB;
    private LaunchGameHistoryCallback  gameHistoryCB;
    private ReturnToCallback returnToCB;

    @FXML private Button backBtn;
    @FXML private Button createLobbyBtn;
    @FXML private ImageView gearIV;
    @FXML private Button joinBtn;
    @FXML private Button GameHistoryButton;
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

        this.gameHistoryCB = null;

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
    private void GameHistoryButtonClicked(ActionEvent event) {


        List<Game> gameHistory = new ArrayList<Game>();

            Game testGame1 = new Game();
            List<Pair<Integer,Integer>> allMoves = new ArrayList<Pair<Integer,Integer>>();
            allMoves.add(new Pair(0,0));
            allMoves.add(new Pair(1,1));
            allMoves.add(new Pair(2,2));
            allMoves.add(new Pair(0,2));
            allMoves.add(new Pair(2,0));
            allMoves.add(new Pair(2,1));
            allMoves.add(new Pair(1,0));
            testGame1.setAllMoves(allMoves);

            List<UUID> allSpectators = new ArrayList<UUID>();
            allSpectators.add(UUID.randomUUID());
            allSpectators.add(UUID.randomUUID());
            allSpectators.add(UUID.randomUUID());
            testGame1.setAllSpectators(allSpectators);

            UUID playerOne = UUID.randomUUID();
            UUID creator = playerOne;
            UUID winner = playerOne;
            UUID playerTwo = UUID.randomUUID();
            testGame1.setPlayers(new Pair(playerOne, playerTwo));
            testGame1.setCreator(creator);
            testGame1.setWinner(winner);
            testGame1.setEndManually(9000);
            testGame1.setStart(5000);
            testGame1.setGameId(1);

            List<Long> moveTimes = new ArrayList<Long>();
            moveTimes.add(Long.valueOf(1));
            moveTimes.add(Long.valueOf(2));
            moveTimes.add(Long.valueOf(3));
            moveTimes.add(Long.valueOf(4));
            moveTimes.add(Long.valueOf(5));
            moveTimes.add(Long.valueOf(6));
            moveTimes.add(Long.valueOf(7));
            testGame1.setMoveTimes(moveTimes);

            gameHistory.add(testGame1);




            Game testGame2 = new Game();
            List<Pair<Integer,Integer>> allMoves2 = new ArrayList<Pair<Integer,Integer>>();
            allMoves2.add(new Pair(0,0));
            allMoves2.add(new Pair(1,1));
            allMoves2.add(new Pair(2,2));
            allMoves2.add(new Pair(0,2));
            allMoves2.add(new Pair(2,0));
            allMoves2.add(new Pair(2,1));
            allMoves2.add(new Pair(1,0));
            allMoves2.add(new Pair(2,2));
            allMoves2.add(new Pair(2,1));

            testGame2.setAllMoves(allMoves);

            List<UUID> allSpectators2 = new ArrayList<UUID>();
            allSpectators2.add(UUID.randomUUID());
            allSpectators2.add(UUID.randomUUID());
            allSpectators2.add(UUID.randomUUID());
            testGame2.setAllSpectators(allSpectators);

            UUID playerOne2 = UUID.randomUUID();
            UUID creator2 = playerOne;
            UUID winner2 = playerOne;
            UUID playerTwo2 = UUID.randomUUID();
            testGame2.setPlayers(new Pair(playerOne, playerTwo));
            testGame2.setCreator(creator);
            testGame2.setWinner(winner);
            testGame2.setEndManually(90352);
            testGame2.setStart(1231);
            testGame2.setGameId(2);

            List<Long> moveTimes2 = new ArrayList<Long>();
            moveTimes2.add(Long.valueOf(1));
            moveTimes2.add(Long.valueOf(2234));
            moveTimes2.add(Long.valueOf(3));
            moveTimes2.add(Long.valueOf(4323));
            moveTimes2.add(Long.valueOf(5));
            moveTimes2.add(Long.valueOf(6));
            moveTimes2.add(Long.valueOf(7));
            moveTimes2.add(Long.valueOf(712));
            moveTimes2.add(Long.valueOf(742));

            testGame2.setMoveTimes(moveTimes);

            gameHistory.add(testGame2);


        this.gameHistoryCB.launchGameHistory(gameHistory);

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

    public void setGameHistoryCB(LaunchGameHistoryCallback gameHistoryCB)    {this.gameHistoryCB = gameHistoryCB;}

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