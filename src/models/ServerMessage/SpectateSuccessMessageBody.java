package models.ServerMessage;

import java.util.UUID;
import models.GameState;

public class SpectateSuccessMessageBody {
    GameState gameState;
    UUID lobbyId;

    public SpectateSuccessMessageBody(GameState gameState, UUID lobbyId){
        this.gameState = gameState;
        this.lobbyId =lobbyId;
    }

    public GameState getGameState() {
        return gameState;
    }

    public UUID getLobbyId() {
        return lobbyId;
    }
}
