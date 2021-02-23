package models.SceneCallback;

import java.util.UUID;

import models.GameState;
import models.TTTScene;

public interface LaunchOptionsMenuCallback {
    public void launchOptionsMenu(UUID playerId, TTTScene returnTo, GameState gameState);
}
