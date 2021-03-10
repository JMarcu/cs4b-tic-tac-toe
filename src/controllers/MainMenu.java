package controllers;

import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.UUID;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import models.Ai;
import models.ColorScheme;
import models.GameMode;
import models.GameState;
import models.MarkerShape;
import models.Player;
import models.SceneCallback.LaunchGameCallback;
import models.SceneCallback.LaunchOptionsMenuCallback;
import models.SceneCallback.LaunchShapePickerCallback;
import org.javatuples.Pair;

/**
 * Controls the main menu view.
 * @author James Marcu
 */
public class MainMenu {
    /*==========================================================================================================
     * CLASS VARIABLES
     *==========================================================================================================*/

    /** Stores user selection of game mode. */
    private GameMode gameMode;

    /** Interface invoked to tell the scene controller to launch a game. */
    private LaunchGameCallback launchGameCB;

    /** Interface invoked to tell the scene controller to open the options menu. */
    private LaunchOptionsMenuCallback optionsMenuCB;
    
    /** Player object describing the first player. */
    private Player playerOne;

    /** Subscription to player one updates. */
    private Subscription playerOneSubscription;

    /** Player object describing the second player. */
    private Player playerTwo;

    /** Subscription to player two updates. */
    private Subscription playerTwoSubscription;

    /** 
     * Some game modes have a configurable quantity associated with them. For instance, {@link GameMode.BULLET} 
     * allows players to set the timer duration. This field stores the input value for such game mode properties.
     */
    private int secondaryOption;

    /** Interface invoked to tell the scene controller to open the marker menu. */
    private LaunchShapePickerCallback shapePickerCB;

    /** Stores whether or not the player has selected a single player game. */
    private boolean singlePlayer;

    /** Absolute location of the image assets directory. */
    private final String IMAGES_DIRECTORY = "/assets/images/";

    /*==========================================================================================================
     * VIEW ELEMENTS
     *==========================================================================================================*/

    /** ToggleButton for selecting to vs the AI. */
    @FXML private ToggleButton aiBtn;

    /** ImageView displaying a gear for the options menu button. */
    @FXML private ImageView gearIV;

    /** ToggleButton for selecting to vs a second human opponent. */
    @FXML private ToggleButton humanBtn;

    // @FXML private ChoiceBox<String> gameModeCB;
    
    /** TextField for displaying and editing the first player's name. */
    @FXML private TextField playerOneNameTF;

    /** Pane for displaying player one's marker. */
    @FXML private Pane playerOneMarkerPane;
    
    /** ImageView displaying the first player's marker. */
    // @FXML private ImageView playerOneShapeIV;
    
    /** TextField for displaying and editing the second player's name. */
    @FXML private TextField playerTwoNameTF;

    /** Pane for displaying player two's marker. */
    @FXML private Pane playerTwoMarkerPane;

    /** Root pane of the view. */
    @FXML private ScrollPane root;

    // @FXML private TextField secondaryOptionTF;

    /*==========================================================================================================
     * LIFECYCLE
     *==========================================================================================================*/

    /** 
     * Constructs a default instance of MainMenu.
     *  - Player one will be named "Player 1" and have a black {@link MarkerShape.X} marker.
     *  - Player two will be named "Player 2" and have a black {@link MarkerShape.O} marker.
     *  - Single player will be set to true.
     *  - Game mode will be set to {@link GameMode.FREE_PLAY}.
     */
    public MainMenu(){
        this.playerOne = new Player(Color.BLACK, UUID.randomUUID(), "Player 1", MarkerShape.X);
        this.playerTwo = new Player(Color.BLACK, UUID.randomUUID(), "Player 2", MarkerShape.O);
        this.secondaryOption = -1;
        this.singlePlayer = true;
    }

    /** Sets the default state of the view's interactive elements. */
    @FXML 
    void initialize() {
        //Load external style sheets.
        root.getStylesheets().add(getClass().getResource("/styles/color-theme.css").toExternalForm());
        root.getStylesheets().add(getClass().getResource("/styles/main-menu.css").toExternalForm());

        //Bind the default player objects to the associated view elements.
        setPlayers(playerOne, playerTwo);

        //Set the state of the single player toggle buttons.
        aiBtn.setSelected(singlePlayer);
        humanBtn.setSelected(!singlePlayer);

        //Color the options button to fit the color scheme.
        ColorScheme.adjustImageColor(gearIV, ColorScheme.TEXT_ON_SECONDARY.getColor());
        
        //An initial animation must be run to set certain style values.
        animateSinglePlayerButtons();
        
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
    }

    /*==========================================================================================================
     * ACCESSORS & MUTATORS
     *==========================================================================================================*/

    /** 
     * Gets the root element of the MainMenu view.
     * @returns The root element of the MainMenu view.
     */
    public ScrollPane getRoot(){ return this.root; }

    /** Sets the callback to be invoked when the MainMenu wishes to launch a new game. */
    public void setLaunchGameCB(LaunchGameCallback launchGameCB){
        this.launchGameCB = launchGameCB;
    }

    /** Sets the callback to be invoked when the MainMenu wants to open the options menu. */
    public void setOptionsMenuCB(LaunchOptionsMenuCallback optionsMenuCB){
        this.optionsMenuCB = optionsMenuCB;
    }

    /** Sets and binds the two players in the game. */
    public void setPlayers(Player playerOne, Player playerTwo){
        //Set the players.
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;

        //Cancel any subscriptions to previous players.
        if(playerOneSubscription != null){ playerOneSubscription.cancel(); }
        if(playerTwoSubscription != null){ playerTwoSubscription.cancel(); }

        //Initialize the player marker views.
        this.setMarker(playerOneMarkerPane, playerOne);
        this.setMarker(playerTwoMarkerPane, playerTwo);

        //Subscribe to player updates. We want to update the marker views whenever the 
        //players change their markers in another menu.
        this.playerOne.subscribe(new Subscriber<Player.Patch>(){
            @Override public void onComplete() { }
            @Override public void onError(Throwable throwable) { }

            @Override
            public void onSubscribe(Subscription subscription) {
                playerOneSubscription = subscription;
                playerOneSubscription.request(1);
            }

            @Override
            public void onNext(Player.Patch item) {
                if(item.getColor() != null || item.getShape() != null){
                    setMarker(playerOneMarkerPane, playerOne);
                }
                playerOneSubscription.request(1);
            }
        });

        this.playerTwo.subscribe(new Subscriber<Player.Patch>(){
            @Override public void onComplete() { }
            @Override public void onError(Throwable throwable) { }

            @Override
            public void onSubscribe(Subscription subscription) {
                playerTwoSubscription = subscription;
                playerTwoSubscription.request(1);
            }

            @Override
            public void onNext(Player.Patch item) {
                if(item.getColor() != null || item.getShape() != null){
                    setMarker(playerTwoMarkerPane, playerTwo);
                }
                playerTwoSubscription.request(1);
            }
        });
    }

    /** Sets the callback to be invoked when the MainMenu wants to launch a marker picker menu. */
    public void setShapePickerCB(LaunchShapePickerCallback shapePickerCB){
        this.shapePickerCB = shapePickerCB;
    }

    /*==========================================================================================================
     * EVENT HANDLERS
     *==========================================================================================================*/

    /** Set {@link singlePlayer} to true when the user clicks the AI button. */
    @FXML
    private void onAiAction(){
        if(!singlePlayer){
            playerTwo = new Ai(playerTwo);
            singlePlayer = true;
            animateSinglePlayerButtons();
        }
    }

    // @FXML
    // void onGameModeAction(ActionEvent event) {
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
    // }


    /** Set {@link singlePlayer} to false when the user clicks {@link humanBtn}. */
    @FXML
    private void onHumanAction(){
        if(singlePlayer){
            playerTwo = ((Ai)playerTwo).toPlayer();
            singlePlayer = false;
            animateSinglePlayerButtons();
        }
    }

    /** Invoke the {@link optionsMenuCB} when the user hits the options menu button. */
    @FXML
    void onOptions(ActionEvent event) {
        optionsMenuCB.launchOptionsMenu("MainMenu");
    }

    /** 
     * Invoke the {@link launchGameCB} when the user hits the play button, feeding it the user's chosen game state.
     * The controller assumes it will be unloaded and releases certain state objects from memory, so likely will not
     * funciton fully if it persists after invoking this method.
     */
    @FXML
    void onPlayAction(ActionEvent event) {
        if(playerOneSubscription != null){ playerOneSubscription.cancel(); }
        if(playerTwoSubscription != null){ playerTwoSubscription.cancel(); }
        launchGameCB.launchGame(new GameState(gameMode, new Pair<Player, Player>(playerOne, playerTwo), singlePlayer, secondaryOption));
    }

    /** Update player one's name when the user types in ${@link playerOneNameTF}.  */
    @FXML
    void onPlayerOneKeyTyped(KeyEvent event) {
        playerOne.setName(playerOneNameTF.getText());
    }

    /** Invoke the {@link shapePickerCB} when the user clicks  */
    @FXML
    void onPlayerOneShapeAction(ActionEvent event) {
        shapePickerCB.launchShapePicker(playerOne);
    }

    /** Binds KeyEvents from the TextField to playerTwo's name. */
    @FXML
    void onPlayerTwoKeyTyped(KeyEvent event) {
        playerTwo.setName(playerTwoNameTF.getText());
    }

    /** Invoke the {@link shapePickerCB} when the user clicks  */
    @FXML
    void onPlayerTwoShapeAction(ActionEvent event) {
        shapePickerCB.launchShapePicker(playerTwo);
    }

    // @FXML
    // void onSecondaryOptionKeyTyped(KeyEvent event) {
        // final String text = this.secondaryOptionTF.getText();
        // this.secondaryOption = text == "" ? 0 : Integer.valueOf(text);
    // }

    /*==========================================================================================================
     * VIEW MANIPULATORS
     *==========================================================================================================*/

    /** Transitions the background colors of the vsHuman/vsAI buttons whenever they are toggled. */
    private void animateSinglePlayerButtons(){
        ToggleButton darkBtn = this.singlePlayer ? this.aiBtn : this.humanBtn;
        ToggleButton lightBtn = this.singlePlayer ? this.humanBtn : this.aiBtn;

        Color darkColor = ColorScheme.SECONDARY.getColor();
        Color lightColor = ColorScheme.SECONDARY_LIGHT.getColor();

        final Animation lightToDark = new Transition(){
            {
                setCycleDuration(Duration.millis(150));
                setInterpolator(Interpolator.EASE_OUT);
            }

            @Override
            protected void interpolate(double frac) {
                darkBtn.setBackground(new Background(new BackgroundFill(
                    darkColor.interpolate(lightColor, frac), 
                    new CornerRadii(4), 
                    Insets.EMPTY
                )));
            }
        };

        final Animation darkToLight = new Transition(){
            {
                setCycleDuration(Duration.millis(150));
                setInterpolator(Interpolator.EASE_OUT);
            }

            @Override
            protected void interpolate(double frac) {
                lightBtn.setBackground(new Background(new BackgroundFill(
                    lightColor.interpolate(darkColor, frac), 
                    new CornerRadii(4), 
                    Insets.EMPTY
                )));
            }
        };

        lightToDark.play();
        darkToLight.play();
    }

    /** 
     * Updates the background image of the provided pane to display the player's marker.
     * @param pane The pane element whose background should display the marker.
     * @param player The player whose marker should be displayed.
     */
    private void setMarker(Pane pane, Player player){
        StringBuilder sb = new StringBuilder();
        sb.append("-fx-background-image: url('");
        sb.append(IMAGES_DIRECTORY);
        sb.append(player.getShape().getFilename());
        sb.append("');");
        pane.setStyle(sb.toString());
        ColorScheme.adjustImageColor(pane, player.getColor());
    }
}