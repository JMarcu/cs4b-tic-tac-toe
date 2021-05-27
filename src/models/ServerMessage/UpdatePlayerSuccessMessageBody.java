package models.ServerMessage;

import models.Player;

public class UpdatePlayerSuccessMessageBody {
    String jwt;
    Player player;

    public UpdatePlayerSuccessMessageBody(String jwt, Player player){
        this.jwt = jwt;
        this.player = player;
    }

    public String getJwt() {
        return jwt;
    }

    public Player getPlayer() {
        return player;
    }
}
