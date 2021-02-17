package models.SceneCallback;

import models.GameState;
import models.Player;
import models.TTTScene;

public interface LaunchShapePickerCallback {
    public void launchShapePicker(Player player, TTTScene returnTo, GameState gameState);
}