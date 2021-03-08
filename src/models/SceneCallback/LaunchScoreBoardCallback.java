package models.SceneCallback;

import java.util.UUID;
import models.TTTScene;
import models.GameState;

public interface LaunchScoreBoardCallback {
    public void launchScoreBoard(UUID playerID, TTTScene returnTo, GameState gameState);
}