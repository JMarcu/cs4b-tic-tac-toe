import controllers.GameBoard;
import controllers.MainMenu;
import controllers.OptionsController;
import controllers.ScoreBoard;
import controllers.ShapeColorController;
import controllers.SplashScreen;
import controllers.SplashScreen.SplashType;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.UUID;
import java.util.Vector;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Ai;
import models.GameState;
import models.MarkerShape;
import models.Player;
import models.SceneCallback.LaunchGameCallback;
import models.SceneCallback.LaunchMainMenuCallback;
import models.SceneCallback.LaunchOptionsMenuCallback;
import models.SceneCallback.LaunchScoreBoardCallback;
import models.SceneCallback.LaunchShapePickerCallback;
import models.SceneCallback.ReturnToCallback;
import models.TTTScene;
import models.MusicPlayer.Track;
import models.MusicPlayer;

public class App extends Application implements LaunchGameCallback, LaunchMainMenuCallback, LaunchOptionsMenuCallback, LaunchShapePickerCallback, LaunchScoreBoardCallback {

    private FXMLLoader   gameBoardFXML;
    private Subscription gameStateSubscription;
    private FXMLLoader   scoreboardFXML; 
    private FXMLLoader   mainMenuFXML;
    private FXMLLoader   markerPickerFXML;
    private MusicPlayer  music;
    private FXMLLoader   optionsMenuFXML;
    private Player       playerOne;
    private Player       playerTwo;
    private StackPane    rootPane;
    private FXMLLoader   splashScreenFXML;
    private final long FADE_DURATION = 200;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            music = new MusicPlayer();            
            playerOne = new Player(Color.BLACK, UUID.randomUUID(), "Player 1", MarkerShape.X);
            playerTwo = new Ai(Color.BLACK, "Player 2", MarkerShape.O);
            rootPane = new StackPane();

            Font.loadFont(App.class.getResource("/assets/fonts/Pixeboy.ttf").toExternalForm(), 10);

            gameBoardFXML = new FXMLLoader(getClass().getResource("/views/game-board.fxml"));
            mainMenuFXML = new FXMLLoader(getClass().getResource("/views/main-menu.fxml"));
            markerPickerFXML = new FXMLLoader(getClass().getResource("/views/ShapeColorPicker.fxml"));
            optionsMenuFXML = new FXMLLoader(getClass().getResource("/views/OptionsMenu.fxml"));
            scoreboardFXML = new FXMLLoader(getClass().getResource("/views/Scoreboard.fxml"));
            splashScreenFXML = new FXMLLoader(getClass().getResource("/views/SplashScreen.fxml"));

            gameBoardFXML.load();
            mainMenuFXML.load();
            markerPickerFXML.load();
            optionsMenuFXML.load();
            scoreboardFXML.load();
            splashScreenFXML.load();

            primaryStage.setTitle("Tic Tac Toe");
            primaryStage.setScene(new Scene(rootPane));

            launchMainMenu();

            SplashScreen splashScreen = splashScreenFXML.getController();
            splashScreen.setReturnCB(new ReturnToCallback(){
                @Override
                public void returnTo() { closeMenu(splashScreen.getRoot()); }
            });
            splashScreen.setSplashType(SplashType.TITLE);
            openMenu(splashScreen.getRoot());

            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void launchMainMenu() {
        System.out.println("launchMainMenu");
        music.playMusic(Track.title);

        MainMenu mainMenu = mainMenuFXML.getController();
        mainMenu.setLaunchGameCB(this);
        mainMenu.setOptionsMenuCB(this);
        mainMenu.setShapePickerCB(this);
        System.out.println("playerTwo.getIsAi(): " + playerTwo.getIsAI());
        mainMenu.setPlayers(playerOne, playerTwo);

        if(rootPane.getChildren().size()  > 0){
            launchScene(mainMenu.getRoot());
        } else{
            rootPane.getChildren().add(mainMenu.getRoot());
        }
    }

    @Override
    public void launchGame(GameState gameState) {
        try {
            System.out.println("launchGame");
            music.playMusic(Track.waiting);
            playerOne = gameState.getPlayers().getValue0();
            playerTwo = gameState.getPlayers().getValue1();
            System.out.println("playerTwo.getIsAi(): " + playerTwo.getIsAI());

            GameBoard gameBoard = gameBoardFXML.getController();
            gameBoard.setGameState(gameState);
            gameBoard.setShapePickerCB(this);
            gameBoard.setOptionsMenuCB(this);
            gameBoard.setScoreBoardCB(this);

            if(gameStateSubscription != null){
                gameStateSubscription.cancel();
            }
            gameState.subscribe(new Subscriber<GameState.Patch>(){
                @Override public void onSubscribe(Subscription subscription) { 
                    gameStateSubscription = subscription; 
                    subscription.request(1);
                }
                @Override public void onNext(GameState.Patch item) {    onGameStatePatch(item);};
                @Override public void onError(Throwable throwable) { }
                @Override public void onComplete() { }
            });
            
            launchScene(gameBoard.getRoot());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void launchOptionsMenu(String caller) {
        try{
            if (music.getShouldPlaySFX()){
                MusicPlayer music2 = new MusicPlayer();
                music2.playSFX(MusicPlayer.Track.openMenu);
            }

            OptionsController optionsMenu = optionsMenuFXML.getController();
            optionsMenu.acceptCaller(caller, music);
            optionsMenu.setMainMenuCB(this);
            optionsMenu.setReturnToCB(() -> {closeMenu(optionsMenu.getRoot());});
            openMenu(optionsMenu.getRoot());
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void launchShapePicker(Player player) {
        try{
            if (music.getShouldPlaySFX()){
                MusicPlayer music2 = new MusicPlayer();
                music2.playSFX(MusicPlayer.Track.openMenu);
            }

            ShapeColorController markerMenu = markerPickerFXML.getController();
            markerMenu.acceptPlayer(player, music);
            markerMenu.setReturnCB(() -> {closeMenu(markerMenu.getRoot());});
            openMenu(markerMenu.getRoot());
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void launchScoreBoard(UUID topCandidate, TTTScene returnTo, Vector<GameState> gameState){
        try{
            ScoreBoard scoreboard = scoreboardFXML.getController();
            if(gameState.size()==0) scoreboard.set();
            for(int i=0; i<gameState.size(); i++)
                scoreboard.addPlayer(gameState.get(i));
            scoreboard.setReturnCB(() -> {closeMenu(scoreboard.getRoot());});
            openMenu(scoreboard.getRoot());
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void closeMenu(Node menuRoot){
        final FadeTransition transition = new FadeTransition(Duration.millis(FADE_DURATION));
        transition.setNode(menuRoot);
        transition.setFromValue(1);
        transition.setToValue(0);
        transition.setOnFinished(onFinished -> {
            rootPane.getChildren().remove(menuRoot);
        });
        transition.play();
    }

    private void launchScene(Node sceneRoot){
        rootPane.getChildren().add(sceneRoot);
        final FadeTransition transition = new FadeTransition(Duration.millis(FADE_DURATION));
        transition.setNode(sceneRoot);
        transition.setFromValue(0);
        transition.setToValue(1);
        transition.setOnFinished(onFinished -> {
            while(rootPane.getChildren().size() > 1){
                rootPane.getChildren().remove(0);
            }
        });
        transition.play();
    }

    private void openMenu(Node menuRoot){
        rootPane.getChildren().add(menuRoot);
        final FadeTransition transition = new FadeTransition(Duration.millis(FADE_DURATION));
        transition.setNode(menuRoot);
        transition.setFromValue(0);
        transition.setToValue(1);
        transition.play();
    }

    private void onGameStatePatch(GameState.Patch patch){
        if(patch.getStatus() == GameState.Status.DRAW){
            SplashScreen splashScreen = splashScreenFXML.getController();
            splashScreen.setReturnCB(new ReturnToCallback(){
                @Override
                public void returnTo() {
                    launchMainMenu();
                }
            });
            splashScreen.setSplashType(SplashType.DRAW);
            openMenu(splashScreen.getRoot());
        } else if(patch.getWinner() != null){
            System.out.println("winner: " + patch.getWinner());
            SplashScreen splashScreen = splashScreenFXML.getController();
            splashScreen.setReturnCB(new ReturnToCallback(){
                @Override
                public void returnTo() {
                    launchMainMenu();
                }
            });
            splashScreen.setSplashType( 
                patch.getWinner().getIsAI()
                    ? SplashType.LOSE
                    : SplashType.WIN
            );
            openMenu(splashScreen.getRoot());
        }
        gameStateSubscription.request(1);
    }
}