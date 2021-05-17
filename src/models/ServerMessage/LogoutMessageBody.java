package models.ServerMessage;

import java.util.UUID;

public class LogoutMessageBody {
    private UUID playerId;
    private String refreshToken;

    public LogoutMessageBody(String refreshToken, UUID playerId){
        this.playerId = playerId;
        this.refreshToken = refreshToken;
    }

    public UUID getPlayerId(){return playerId; }
    public String getRefreshToken(){ return refreshToken; }
}