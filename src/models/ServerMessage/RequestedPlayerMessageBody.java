package models.ServerMessage;

import models.Player;

public class RequestedPlayerMessageBody {
   
    private Player player;

    public RequestedPlayerMessageBody(Player player) {
        this.player = player;
    }

    public Player getPlayer(){ return player;}
}