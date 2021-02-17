import java.util.UUID;

import controllers.GameBoard;
import controllers.MainMenu;
import controllers.OptionsController;
import controllers.ShapeColorController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Color;
import models.GameMode;
import models.MarkerShape;
import models.Player;
import models.SceneCallback.LaunchGameCallback;
import models.SceneCallback.LaunchMainMenuCallback;
import models.SceneCallback.LaunchOptionsMenuCallback;
import models.SceneCallback.LaunchShapePickerCallback;
import models.SceneCallback.LaunchScoreBoardCallback;

public class App extends Application implements LaunchGameCallback, LaunchMainMenuCallback, LaunchOptionsMenuCallback, LaunchShapePickerCallback, LaunchScoreBoardCallback {

    private FXMLLoader gameBoardFXML;
    private Scene      gameBoardScene;
    private FXMLLoader mainMenuFXML;
    private Scene      mainMenuScene;
    private FXMLLoader markerPickerFXML;
    private Scene      markerPickerScene;
    private FXMLLoader optionsMenuFXML;
    private Scene      optionsMenuScene;
    private Player     playerOne;
    private Player     playerTwo;
    private Stage      primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            gameBoardFXML = new FXMLLoader(getClass().getResource("/views/game-board.fxml"));
            mainMenuFXML = new FXMLLoader(getClass().getResource("/views/main-menu.fxml"));
            markerPickerFXML = new FXMLLoader(getClass().getResource("/views/ShapeColorPicker.fxml"));
            optionsMenuFXML = new FXMLLoader(getClass().getResource("/views/OptionsMenu.fxml"));

            gameBoardScene = new Scene(gameBoardFXML.load());
            mainMenuScene = new Scene(mainMenuFXML.load());
            markerPickerScene = new Scene(markerPickerFXML.load());
            optionsMenuScene = new Scene(optionsMenuFXML.load());

            playerOne = new Player(Color.BLACK, UUID.randomUUID(), "Player 1", MarkerShape.X);
            playerTwo = new Player(Color.BLACK, UUID.randomUUID(), "Player 2", MarkerShape.O);

            this.primaryStage = primaryStage;
            primaryStage.setTitle("Tic Tac Toe");
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

        primaryStage.setScene(mainMenuScene);
    }

    @Override
    public void launchGame(boolean singlePlayer, GameMode gameMode, Player playerOne, Player playerTwo, int secondaryOption) {
    
        try {
            GameBoard gameBoard = gameBoardFXML.getController();
            gameBoard.loadData(singlePlayer, gameMode, playerOne, playerTwo, secondaryOption);
            gameBoard.setShapePickerCB(this);
            gameBoard.setOptionsMenuCB(this);

            primaryStage.setScene(gameBoardScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void launchOptionsMenu(UUID playerId) {
        try{
            OptionsController optionsMenu = optionsMenuFXML.getController();
            primaryStage.setScene(optionsMenuScene);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void launchShapePicker(Player player) {

        try{
            ShapeColorController markerMenu = markerPickerFXML.getController();
            markerMenu.acceptPlayer(player);
            primaryStage.setScene(markerPickerScene);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void launchScoreBoard(UUID playerId){
        System.out.println("Launch Score Board");
    }
}