package models.ServerMessage;

import java.util.UUID;

public class ConnectionMessageBody {
    public enum  Type {
        JOIN, LEAVE
    }
    
    private String jwt;
    private UUID lobbyId;
    private UUID playerId;
    private Type type;

    public ConnectionMessageBody(String jwt, UUID lobbyId, UUID playerId, Type type){
        this.jwt = jwt;
        this.lobbyId = lobbyId;
        this.playerId = playerId;
        this.type = type;
    }

    public String getJwt(){ return jwt; }
    public UUID getLobbyId(){ return lobbyId; }
    public UUID getPlayerId(){ return playerId; }
    public Type getType(){ return type; }
}
