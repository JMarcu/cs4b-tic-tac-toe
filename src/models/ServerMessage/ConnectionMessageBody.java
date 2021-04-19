package models.ServerMessage;

public class ConnectionMessageBody {
    public enum  Type {
        JOIN, LEAVE
    }
    
    private String lobbyId;
    private String playerId;
    private Type type;

    public ConnectionMessageBody(String lobbyId, String playerId, Type type){
        this.lobbyId = lobbyId;
        this.playerId = playerId;
        this.type = type;
    }

    public String getLobbyId(){ return lobbyId; }
    public String getPlayerId(){ return playerId; }
    public Type getType(){ return type; }
}
