package models.SceneCallback;

import java.util.Vector;
import models.TTTScene;
import models.GameState;

public interface LaunchScoreBoardCallback {
    public void launchScoreBoard(TTTScene returnTo, Vector<GameState> gameHistory);
}