package models.ServerMessage;

import java.io.Serializable;
import java.util.UUID;

import models.PlayerData;

public class PlayerPropertiesMessageBody implements Serializable {
    private static final long serialVersionUID = 1L;

    private PlayerData patch;
    private UUID playerId;

    public PlayerPropertiesMessageBody(PlayerData patch, UUID playerId){
        this.patch = patch;
        this.playerId = playerId;
    }

    public PlayerData getPatch(){ return this.patch; }
    public UUID getPlayerId(){ return this.playerId; }
}
