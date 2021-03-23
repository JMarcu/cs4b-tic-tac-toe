package models.ServerMessage;

import java.io.Serializable;
import java.util.UUID;

import models.Player;

public class PlayerPropertiesMessageBody implements Serializable {
    private static final long serialVersionUID = 1L;

    private Player.Patch patch;
    private UUID playerId;

    public PlayerPropertiesMessageBody(Player.Patch patch, UUID playerId){
        this.patch = patch;
        this.playerId = playerId;
    }

    public Player.Patch getPatch(){ return this.patch; }
    public UUID getPlayerId(){ return this.playerId; }
}
