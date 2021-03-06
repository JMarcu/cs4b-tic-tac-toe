import java.util.UUID;

import controllers.GameBoard;
import controllers.MainMenu;
import controllers.OptionsController;
import controllers.ScoreBoard;
import controllers.ShapeColorController;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.GameState;
import models.MarkerShape;
import models.Player;
import models.SceneCallback.LaunchGameCallback;
import models.SceneCallback.LaunchMainMenuCallback;
import models.SceneCallback.LaunchOptionsMenuCallback;
import models.SceneCallback.LaunchScoreBoardCallback;
import models.SceneCallback.LaunchShapePickerCallback;
import models.TTTScene;

public class App extends Application implements LaunchGameCallback, LaunchMainMenuCallback, LaunchOptionsMenuCallback, LaunchShapePickerCallback, LaunchScoreBoardCallback {

    private FXMLLoader gameBoardFXML;
    private Scene      gameBoardScene;
    private FXMLLoader scoreboardFXML;
    private Scene      scoreboardScene; 
    private FXMLLoader mainMenuFXML;
    private Scene      mainMenuScene;
    private FXMLLoader markerPickerFXML;
    private Scene      markerPickerScene;
    private FXMLLoader optionsMenuFXML;
    private Scene      optionsMenuScene;
    private Player     playerOne;
    private Player     playerTwo;
    private Stage      primaryStage;
    private StackPane  rootPane;

    private final long FADE_DURATION = 200;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            rootPane = new StackPane();

            gameBoardFXML = new FXMLLoader(getClass().getResource("/views/game-board.fxml"));
            mainMenuFXML = new FXMLLoader(getClass().getResource("/views/main-menu.fxml"));
            markerPickerFXML = new FXMLLoader(getClass().getResource("/views/ShapeColorPicker.fxml"));
            optionsMenuFXML = new FXMLLoader(getClass().getResource("/views/OptionsMenu.fxml"));
            scoreboardFXML = new FXMLLoader(getClass().getResource("/views/Scoreboard.fxml"));

            gameBoardScene = new Scene(gameBoardFXML.load());
            mainMenuScene = new Scene(mainMenuFXML.load());
            markerPickerScene = new Scene(markerPickerFXML.load());
            optionsMenuScene = new Scene(optionsMenuFXML.load());
            scoreboardScene = new Scene(scoreboardFXML.load());

            playerOne = new Player(Color.BLACK, UUID.randomUUID(), "Player 1", MarkerShape.X);
            playerTwo = new Player(Color.BLACK, UUID.randomUUID(), "Player 2", MarkerShape.O);

            this.primaryStage = primaryStage;
            primaryStage.setTitle("Tic Tac Toe");
            primaryStage.setScene(new Scene(rootPane));
            launchMainMenu();
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void launchMainMenu() {
        MainMenu mainMenu = mainMenuFXML.getController();
        mainMenu.setLaunchGameCB(this);
        mainMenu.setOptionsMenuCB(this);
        mainMenu.setShapePickerCB(this);
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
            GameBoard gameBoard = gameBoardFXML.getController();
            gameBoard.setGameState(gameState);
            gameBoard.setShapePickerCB(this);
            gameBoard.setOptionsMenuCB(this);
            gameBoard.setScoreBoardCB(this);

            launchScene(gameBoard.getRoot());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void launchOptionsMenu() {
        try{
            OptionsController optionsMenu = optionsMenuFXML.getController();
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
            ShapeColorController markerMenu = markerPickerFXML.getController();
            markerMenu.acceptPlayer(player);
            markerMenu.setReturnCB(() -> {closeMenu(markerMenu.getRoot());});
            openMenu(markerMenu.getRoot());
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void launchScoreBoard(UUID topCandidate, TTTScene returnTo, GameState gameState){
        try{
            ScoreBoard scoreboard = scoreboardFXML.getController();
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
        final int elementsToRemove = rootPane.getChildren().size();
        rootPane.getChildren().add(sceneRoot);
        final FadeTransition transition = new FadeTransition(Duration.millis(FADE_DURATION));
        transition.setNode(sceneRoot);
        transition.setFromValue(0);
        transition.setToValue(1);
        transition.setOnFinished(onFinished -> {
            for(int i = 0; i < elementsToRemove; i++){
                rootPane.getChildren().remove(i);
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

    private void loadScene(TTTScene scene, GameState gameState){
        switch(scene){
            case GAME_BOARD: 
                if(gameState != null){
                    System.out.println("return to game board: " + gameState);
                    launchGame(gameState);
                }
                break;
            case MAIN_MENU:
                this.launchMainMenu();
                break;
            default:
        }
    }
}