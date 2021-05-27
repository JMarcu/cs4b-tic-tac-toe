package models.ServerMessage;

import java.util.UUID;

public class RequestPlayerMessageBody {

    private UUID playerId;

    public RequestPlayerMessageBody(UUID playerId){
        this.playerId = playerId;
    }

    public UUID getPlayerId(){ return playerId; }
}