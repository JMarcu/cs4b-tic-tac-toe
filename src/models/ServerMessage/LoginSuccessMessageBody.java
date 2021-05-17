package models.ServerMessage;

import models.Player;

public class LoginSuccessMessageBody {
    private String jwt;
    private Player player;
    private String refreshToken;
    
    public LoginSuccessMessageBody(String jwt, String refreshToken, Player player){
        this.jwt = jwt;
        this.player = player;
        this.refreshToken = refreshToken;
    }

    public String getJWT(){ return jwt; }
    public Player getPlayer(){ return player; }
    public String getRefreshToken(){ return refreshToken; }
}
