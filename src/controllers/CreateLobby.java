package controllers;

import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import models.ColorScheme;
import models.GameState;
import models.MusicPlayer;
import models.Player;
import models.SceneCallback.LaunchGameCallback;
import models.SceneCallback.LaunchLobbyFinderCallback;
import models.SceneCallback.LaunchOptionsMenuCallback;
import models.SceneCallback.LaunchShapePickerCallback;
import models.SceneCallback.ReturnToCallback;
import services.LobbyService;

public class CreateLobby {
    @FXML private Button createBtn;
    @FXML private ImageView icon;
    @FXML private ImageView gearIV;
    @FXML private Button joinBtn;
    @FXML private TextField lobbyTextField;
    @FXML private ImageView marker;
    @FXML private Button markerBtn;
    @FXML private Pane markerPane;
    @FXML private TextField playerName;
    @FXML private Button returnBtn;
    @FXML private ScrollPane root;
    @FXML private MenuButton timeMenu;
    @FXML private Label title;

    private LaunchGameCallback launchGameCB;
    private LaunchOptionsMenuCallback launchOptionsMenuCB;
    private LaunchShapePickerCallback launchShapePickerCB;
    private MusicPlayer music;
    private Player player;
    private Subscription playerSubscription;
    private ReturnToCallback returnToCB;
    
    /** Absolute location of the image assets directory. */
    private final String IMAGES_DIRECTORY = "/assets/images/";

    public CreateLobby(){
        this.music = new MusicPlayer();
        this.launchGameCB = null;
        this.launchOptionsMenuCB = null;
        this.launchShapePickerCB = null;
        this.player = null;
        this.playerSubscription = null;
    }

    /** Sets the default state of the view's interactive elements. */
    @FXML void initialize(){
        //Load external style sheets
        root.getStylesheets().add(getClass().getResource("/styles/color-theme.css").toExternalForm());
        root.getStylesheets().add(getClass().getResource("/styles/create-lobby.css").toExternalForm());

        this.createBtn.disableProperty().bind(lobbyTextField.textProperty().isEmpty());

        //Color the options button to fit the color scheme.
        ColorScheme.adjustImageColor(gearIV, ColorScheme.TEXT_ON_SECONDARY.getColor());

        this.setMarker();
    }

    @FXML private void onCreateLobbyAction(ActionEvent e){
        try {
            LobbyService.getInstance().createLobby(
                lobbyTextField.getText(), 
                new Consumer<GameState>(){
                    @Override
                    public void accept(GameState gameState) {
                        Platform.runLater(new Runnable(){
                            @Override
                            public void run() {
                                CreateLobby.this.launchGameCB.launchGame(gameState);
                            }
                        });
                    }
                }
            );
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @FXML private void onMarkerAction(ActionEvent e){
        this.launchShapePickerCB.launchShapePicker(player);
    }

    @FXML private void onOptions(ActionEvent e){
        this.launchOptionsMenuCB.launchOptionsMenu("");
    }

    @FXML private void onReturnAction(ActionEvent event) {
        if (music.getShouldPlaySFX()){
            MusicPlayer music2 = new MusicPlayer();
            music2.playSFX(MusicPlayer.Track.exitMenu);
        }
        returnToCB.returnTo();
    }

    public ScrollPane getRoot(){
        return this.root;
    }

    public void setLaunchGameCB(LaunchGameCallback launchGameCB){
        this.launchGameCB = launchGameCB;
    }

    public void setLaunchOptionsMenuCB(LaunchOptionsMenuCallback launchOptionsMenuCB){
        this.launchOptionsMenuCB = launchOptionsMenuCB;
    }

    public void setLaunchShapePickerCB(LaunchShapePickerCallback launchShaperPickerCB){
        this.launchShapePickerCB = launchShaperPickerCB;
    }

    public void setPlayer(Player player){
        this.player = player;

        if(playerSubscription != null){ playerSubscription.cancel(); }

        this.player.subscribe(new Subscriber<Player.Patch>(){
            @Override public void onComplete() { }
            @Override public void onError(Throwable throwable) { }

            @Override
            public void onSubscribe(Subscription subscription) {
                playerSubscription = subscription;
                playerSubscription.request(1);
            }

            @Override
            public void onNext(Player.Patch item) {
                if(item.getColor() != null || item.getShape() != null){
                    setMarker();
                }
                playerSubscription.request(1);
            }
        });
        this.setMarker();
    }

    public void setReturnToCB(ReturnToCallback returnToCB){
        this.returnToCB = returnToCB;
    }
    
    private void setMarker(){
        if(this.player != null && this.markerPane != null){
            StringBuilder sb = new StringBuilder();
            sb.append("-fx-background-image: url('");
            sb.append(IMAGES_DIRECTORY);
            sb.append(player.getShape().getFilename());
            sb.append("');");
            markerPane.setStyle(sb.toString());
            ColorScheme.adjustImageColor(markerPane, player.getColor());
        }
    }
}

