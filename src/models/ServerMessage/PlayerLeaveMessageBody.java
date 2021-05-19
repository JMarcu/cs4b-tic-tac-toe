package models.ServerMessage;

import java.util.UUID;

public class PlayerLeaveMessageBody {
    private UUID lobbyId;
    private UUID playerId;
    private int position;

    public PlayerLeaveMessageBody(UUID lobbyId, UUID playerId, int position){
        this.lobbyId = lobbyId;
        this.playerId = playerId;
        this.position = position;
    }

    public UUID getLobbyId(){ return lobbyId; }
    public UUID getPlayerId(){ return this.playerId; }
    public int getPosition(){ return this.position; }
}
