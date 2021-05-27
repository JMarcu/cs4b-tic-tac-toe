package models.SceneCallback;
import java.util.Vector;
import models.TTTScene;
import models.GameState;

public interface LaunchGameHistoryCallback {
    public void launchScoreBoard(TTTScene returnTo, Vector<GameState> gameHistory); //change
}

