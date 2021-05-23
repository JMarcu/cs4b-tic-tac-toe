package models.ServerMessage;

import models.GameState;

public class NewGameMessageBody {
    GameState gameState;

    public NewGameMessageBody(GameState gameState){
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }
}
