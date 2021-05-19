package models.ServerMessage;

import java.util.UUID;
import models.GameState;

public class ConnectionSuccessMessageBody {
    private GameState gameState;
    private UUID lobbyId;
    private ConnectionMessageBody.Type type;

    public ConnectionSuccessMessageBody(GameState gameState, UUID lobbyId, ConnectionMessageBody.Type type){
        this.gameState = gameState;
        this.lobbyId = lobbyId;
        this.type = type;
    }

    public GameState getGameState(){ return gameState; }
    public UUID getLobbyId(){ return lobbyId; }
    public ConnectionMessageBody.Type getType(){ return type; }
}
