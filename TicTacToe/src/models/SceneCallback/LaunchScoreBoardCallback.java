package models.SceneCallback;

import java.util.UUID;
//import java.time.Instant;
import models.TTTScene;
import models.GameState;
// import models.Player;

public interface LaunchScoreBoardCallback {
    public void launchScoreBoard(UUID playerID, TTTScene returnTo, GameState gameState);
}