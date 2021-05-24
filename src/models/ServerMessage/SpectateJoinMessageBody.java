package models.ServerMessage;

import java.util.UUID;

public class SpectateJoinMessageBody {
    UUID lobbyId;
    UUID playerId;

    public SpectateJoinMessageBody(UUID lobbyId, UUID playerId){
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
