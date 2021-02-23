package models.SceneCallback;

import models.GameMode;
import models.Player;

public interface LaunchGameCallback {
    public void launchGame(
        boolean singlePlayer, 
        GameMode gameMode,
        Player playerOne,
        Player playerTwo,
        int secondaryOption
    );
}
