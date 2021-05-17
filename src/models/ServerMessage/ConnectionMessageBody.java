package models.ServerMessage;

import java.util.UUID;

public class ConnectionMessageBody {
    public enum  Type {
        JOIN, LEAVE
    }
    
    private UUID lobbyId;
    private UUID playerId;
    private Type type;

    public ConnectionMessageBody(UUID lobbyId, UUID playerId, Type type){
        this.lobbyId = lobbyId;
        this.playerId = playerId;
        this.type = type;
    }

    public UUID getLobbyId(){ return lobbyId; }
    public UUID getPlayerId(){ return playerId; }
    public Type getType(){ return type; }
}
