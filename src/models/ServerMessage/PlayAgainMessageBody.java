package models.ServerMessage;

import java.util.UUID;

public class PlayAgainMessageBody {
    String jwt;
    UUID lobbyId;

    public PlayAgainMessageBody(UUID lobbyId, String jwt){
        this.lobbyId = lobbyId;
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }

    public UUID getLobbyId() {
        return lobbyId;
    }
}
