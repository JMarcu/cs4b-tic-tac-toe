package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

import javax.lang.model.type.NullType;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import models.Color;
import models.GameMode;
import models.MarkerShape;
import models.Player;
import models.TTTScene;
import models.SceneCallback.LaunchGameCallback;
import models.SceneCallback.LaunchOptionsMenuCallback;
import models.SceneCallback.LaunchShapePickerCallback;

public class MainMenu {
    private GameMode gameMode;
    private LaunchGameCallback launchGameCB;
    private LaunchOptionsMenuCallback optionsMenuCB;
    private Player playerOne;
    private Player playerTwo;
    private int secondaryOption;
    private LaunchShapePickerCallback shapePickerCB;
    private boolean singlePlayer;
    private ArrayList<Subscription> subscriptions;

    private final String ASSETS_DIRECTORY = "/assets/images/";

    @FXML
    private ToggleButton aiBtn;
    // @FXML
    // private ChoiceBox<String> gameModeCB;
    @FXML
    private TextField playerOneNameTF;
    @FXML
    private ImageView playerOneShapeIV;
    @FXML
    private TextField playerTwoNameTF;
    @FXML
    private ImageView playerTwoShapeIV;
    @FXML
    private URL location;
    @FXML
    private ResourceBundle resources;
    // @FXML
    // private TextField secondaryOptionTF;

    public MainMenu(){
        this.playerOne = new Player(Color.BLACK, UUID.randomUUID(), "Player 1", MarkerShape.X);
        this.playerTwo = new Player(Color.BLACK, UUID.randomUUID(), "Player 2", MarkerShape.O);
        this.secondaryOption = -1;
        this.singlePlayer = true;
        this.subscriptions = new ArrayList<Subscription>();
    }

    @FXML
    void initialize() {
        this.aiBtn.setSelected(true);
        // this.gameModeCB.setItems(GameMode.toObservableArray());
        // this.gameModeCB.setValue(GameMode.FREE_PLAY.toString());
        // final UnaryOperator<TextFormatter.Change> numberValidator = change -> {
        //     if(change.getText().matches("\\d+")){
        //         return change;
        //     } else{
        //         change.setText("");
        //         change.setRange(change.getRangeStart(), change.getRangeStart());
        //         return change;
        //     }
        // };
        // this.secondaryOptionTF.setDisable(true);
        // this.secondaryOptionTF.setTextFormatter(new TextFormatter<String>(numberValidator));
        this.setPlayers(playerOne, playerTwo);
    }

    @FXML
    void onGameModeAction(ActionEvent event) {
        // this.gameMode = GameMode.fromString(this.gameModeCB.getValue());
        // switch(this.gameMode){
        //     case BEST_OF_X:
        //     case BULLET:
        //         this.secondaryOption = 0;
        //         this.secondaryOptionTF.setDisable(false);
        //         break;
        //     case FREE_PLAY:
        //     case MULTI:
        //     case RECURSIVE:
        //     default:
        //         this.secondaryOption = -1;
        //         this.secondaryOptionTF.setText("");
        //         this.secondaryOptionTF.setDisable(true);
        //         break;
        // }
    }

    @FXML
    void onOptions(ActionEvent event) {
        this.optionsMenuCB.launchOptionsMenu(this.playerOne.getUuid());
    }

    @FXML
    void onPlayAction(ActionEvent event) {
        System.out.println(this.launchGameCB);
        this.launchGameCB.launchGame(
            this.singlePlayer, 
            this.gameMode, 
            this.playerOne, 
            this.playerTwo, 
            this.secondaryOption
        );
    }

    @FXML
    void onPlayerOneKeyTyped(KeyEvent event) {
        this.playerOne.setName(this.playerOneNameTF.getText());
    }

    @FXML
    void onPlayerOneShapeAction(ActionEvent event) {
        this.shapePickerCB.launchShapePicker(this.playerOne, TTTScene.MAIN_MENU, null);
    }

    @FXML
    void onPlayerTwoKeyTyped(KeyEvent event) {
        this.playerTwo.setName(this.playerTwoNameTF.getText());
    }

    @FXML
    void onPlayerTwoShapeAction(ActionEvent event) {
        this.shapePickerCB.launchShapePicker(this.playerTwo, TTTScene.MAIN_MENU, null);
    }

    @FXML
    void onSecondaryOptionKeyTyped(KeyEvent event) {
        // final String text = this.secondaryOptionTF.getText();
        // this.secondaryOption = text == "" ? 0 : Integer.valueOf(text);
    }

    public void setLaunchGameCB(LaunchGameCallback launchGameCB){this.launchGameCB = launchGameCB;}
    public void setOptionsMenuCB(LaunchOptionsMenuCallback optionsMenuCB){this.optionsMenuCB = optionsMenuCB;}
    public void setPlayers(Player playerOne, Player playerTwo){
        this.subscriptions.forEach((Subscription subscription) -> {
            subscription.cancel();
        });
        this.subscriptions = new ArrayList<Subscription>();
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.bindPlayers(playerOneShapeIV, playerOne);
        this.bindPlayers(playerTwoShapeIV, playerTwo);
        this.setMarker(playerOneShapeIV, playerOne);
        this.setMarker(playerTwoShapeIV, playerTwo);
    }
    public void setShapePickerCB(LaunchShapePickerCallback shapePickerCB){this.shapePickerCB = shapePickerCB;}

    private void bindPlayers(ImageView iv, Player player){
        player.subscribe(new Subscriber<NullType>(){
			@Override
			public void onSubscribe(Subscription subscription) {
                subscriptions.add(subscription);
            }

			@Override
			public void onNext(NullType item) {
                setMarker(iv, player);
			}

			@Override
			public void onError(Throwable throwable) { }

			@Override
			public void onComplete() { }
        });
    }

    private void setMarker(ImageView iv, Player player){
        if(iv.getImage() != null && player != null){
            final String newUrl = ASSETS_DIRECTORY.concat(player.getShape().getFilename());
            if(!iv.getImage().getUrl().equals(newUrl)){
                final Image newImage = new Image(newUrl);
                iv.setImage(newImage);
            }
            Color.adjustImageColor(iv, player.getColor());
        }
    }
}