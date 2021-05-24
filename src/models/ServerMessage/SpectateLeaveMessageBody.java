package models.ServerMessage;

import java.util.UUID;

public class SpectateLeaveMessageBody {
    UUID lobbyId;
    UUID playerId;

    public SpectateLeaveMessageBody(UUID lobbyId, UUID playerId){
        this.lobbyId = lobbyId;
        this.playerId = playerId;
    }

    public UUID getLobbyId() {
        return lobbyId;
    }

    public UUID getPlayerId() {
        return playerId;
    }
}
