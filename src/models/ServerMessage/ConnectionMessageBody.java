package models.ServerMessage;

import java.io.Serializable;
import java.util.UUID;

public class ConnectionMessageBody implements Serializable {
    public enum Type {
        JOIN, LEAVE
    }

    private static final long serialVersionUID = 1L;

    private UUID lobbyId;
    private UUID playerId;
    private Type type;

    public ConnectionMessageBody(UUID lobbyId, UUID playerId, Type type){
        this.lobbyId = lobbyId;
        this.playerId = playerId;
        this.type = type;
    }

    public UUID getLobbyId(){ return this.lobbyId; }
    public UUID getPlayerId(){ return this.playerId; }
    public Type getType(){ return this.type; }
}