package models.ServerMessage;

import models.Player;
import services.AuthService.RegistrationResult;

public class RegistrationResultMessageBody {
    private String jwt;
    private Player player;
    private String refreshToken;
    private RegistrationResult result;

    public RegistrationResultMessageBody(RegistrationResult result){
        this.jwt = null;
        this.player = null;
        this.refreshToken = null;
        this.result = result;
    }

    public RegistrationResultMessageBody(RegistrationResult result, Player player, String jwt, String refreshToken){
        this.jwt = null;
        this.player = player;
        this.refreshToken = refreshToken;
        this.result = result;
    }

    public String getJwt() {
        return jwt;
    }
    
    public Player getPlayer(){ return player; }

    public String getRefreshToken() {
        return refreshToken;
    }

    public RegistrationResult getResult(){ return result; }
}
