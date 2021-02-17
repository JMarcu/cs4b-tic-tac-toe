import java.util.UUID;

import controllers.GameBoard;
import controllers.MainMenu;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.GameMode;
import models.MarkerShape;
import models.Player;
import models.SceneCallback.LaunchGameCallback;
import models.SceneCallback.LaunchOptionsMenuCallback;
import models.SceneCallback.LaunchShapePickerCallback;
import models.SceneCallback.LaunchScoreBoardCallback;

public class App extends Application implements LaunchGameCallback, LaunchOptionsMenuCallback, LaunchShapePickerCallback, LaunchScoreBoardCallback {
    
    private Stage windowStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/views/main-menu.fxml"));
            Parent root = (Parent) FXMLLoader.load();
            Scene scene = new Scene(root);
            MainMenu mainMenu = FXMLLoader.getController();
            mainMenu.setLaunchGameCB(this);
            mainMenu.setOptionsMenuCB(this);
            mainMenu.setShapePickerCB(this);

            primaryStage.setTitle("Tic Tac Toe");
            primaryStage.setScene(scene);
            primaryStage.show();
            
            windowStage = primaryStage;

            //mainMenu.TEMPORARY_GET_PLAYER_FOR_TESTING().setShape(MarkerShape.CHECKMARK);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    @Override
    public void launchGame(boolean singlePlayer, GameMode gameMode, Player playerOne, Player playerTwo, int secondaryOption) {
    
        try {
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/views/game-board.fxml"));
            Parent root = (Parent) FXMLLoader.load();
            Scene scene = new Scene(root);
            GameBoard gameBoard = FXMLLoader.getController();

            gameBoard.setShapePickerCB(this);
            gameBoard.setOptionsMenuCB(this);

            gameBoard.loadData(singlePlayer, gameMode, playerOne, playerTwo, secondaryOption);
            windowStage.setScene(scene);

            final StringBuilder sb = new StringBuilder();
            sb.append(gameMode);

            windowStage.setTitle(sb.toString());
            windowStage.show();

            gameBoard.TEMPORARY_GET_PLAYER_FOR_TESTING().setShape(MarkerShape.X);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void launchOptionsMenu(UUID playerId) {
        System.out.println("Launch Options Menu");
    }

    @Override
    public void launchShapePicker(Player player) {
        System.out.println("Launch Shape Picker");
    }

    @Override
    public void launchScoreBoard(UUID playerId){
        System.out.println("Launch Score Board");
    }
}


// System.out.println("App.launchgame");
        // final StringBuilder sb = new StringBuilder();
        // sb.append("LaunchGame(");
        // sb.append(singlePlayer);
        // sb.append(", ");
        // sb.append(gameMode);
        // sb.append(", ");
        // sb.append(playerOne.getName());
        // sb.append(", ");
        // sb.append(playerTwo.getName());
        // sb.append(", ");
        // sb.append(secondaryOption);
        // sb.append(")");
        // System.out.println(sb);