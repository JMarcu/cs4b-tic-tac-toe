package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.function.UnaryOperator;

import javax.lang.model.type.NullType;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import models.GameMode;
import models.MarkerShape;
import models.Player;
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
    @FXML
    private ChoiceBox<String> gameModeCB;
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
    @FXML
    private TextField secondaryOptionTF;

    public MainMenu(){
        this.playerOne = new Player("", UUID.randomUUID(), "Player 1", MarkerShape.X);
        this.playerTwo = new Player("", UUID.randomUUID(), "Player 2", MarkerShape.O);
        this.secondaryOption = -1;
        this.singlePlayer = true;
        this.subscriptions = new ArrayList<Subscription>();
    }

    @FXML
    void initialize() {
        this.aiBtn.setSelected(true);
        this.bindPlayers(this.playerOneShapeIV, this.playerOne);
        this.bindPlayers(this.playerTwoShapeIV, this.playerTwo);
        this.gameModeCB.setItems(GameMode.toObservableArray());
        this.gameModeCB.setValue(GameMode.FREE_PLAY.toString());
        final UnaryOperator<TextFormatter.Change> numberValidator = change -> {
            if(change.getText().matches("\\d+")){
                return change;
            } else{
                change.setText("");
                change.setRange(change.getRangeStart(), change.getRangeStart());
                return change;
            }
        };
        this.secondaryOptionTF.setDisable(true);
        this.secondaryOptionTF.setTextFormatter(new TextFormatter<String>(numberValidator));    
    }

    @FXML
    void onGameModeAction(ActionEvent event) {
        this.gameMode = GameMode.fromString(this.gameModeCB.getValue());
        switch(this.gameMode){
            case BEST_OF_X:
            case BULLET:
                this.secondaryOption = 0;
                this.secondaryOptionTF.setDisable(false);
                break;
            case FREE_PLAY:
            case MULTI:
            case RECURSIVE:
            default:
                this.secondaryOption = -1;
                this.secondaryOptionTF.setText("");
                this.secondaryOptionTF.setDisable(true);
                break;
        }
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
        this.shapePickerCB.launchShapePicker(this.playerOne);
    }

    @FXML
    void onPlayerTwoKeyTyped(ActionEvent event) {
        this.playerOne.setName(this.playerTwoNameTF.getText());
    }

    @FXML
    void onPlayerTwoShapeAction(ActionEvent event) {
        this.shapePickerCB.launchShapePicker(this.playerTwo);
    }

    @FXML
    void onSecondaryOptionKeyTyped(KeyEvent event) {
        final String text = this.secondaryOptionTF.getText();
        this.secondaryOption = text == "" ? 0 : Integer.valueOf(text);
    }

    public void setLaunchGameCB(LaunchGameCallback launchGameCB){this.launchGameCB = launchGameCB;}
    public void setOptionsMenuCB(LaunchOptionsMenuCallback optionsMenuCB){this.optionsMenuCB = optionsMenuCB;}
    public void setShapePickerCB(LaunchShapePickerCallback shapePickerCB){this.shapePickerCB = shapePickerCB;}

    private void bindPlayers(ImageView iv, Player player){
        player.subscribe(new Subscriber<NullType>(){
			@Override
			public void onSubscribe(Subscription subscription) {
                subscriptions.add(subscription);
            }

			@Override
			public void onNext(NullType item) {
                final String newUrl = ASSETS_DIRECTORY.concat(player.getShape().getFilename());
                if(!iv.getImage().getUrl().equals(newUrl)){
                    final Image newImage = new Image(newUrl);
                    iv.setImage(newImage);
                }
			}

			@Override
			public void onError(Throwable throwable) { }

			@Override
			public void onComplete() { }
        });
    }

    public Player TEMPORARY_GET_PLAYER_FOR_TESTING(){return this.playerOne;}
}