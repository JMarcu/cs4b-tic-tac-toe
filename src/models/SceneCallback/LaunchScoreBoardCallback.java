package models.SceneCallback;

import java.util.UUID;
import java.util.Vector;
import models.TTTScene;
import models.GameState;

public interface LaunchScoreBoardCallback {
    public void launchScoreBoard(UUID playerID, TTTScene returnTo, Vector<GameState> gameHistory);
}