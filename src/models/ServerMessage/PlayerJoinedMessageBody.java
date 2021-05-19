package models.ServerMessage;

import java.util.UUID;
import models.Player;

public class PlayerJoinedMessageBody {
    private UUID lobbyId;
    private Player player;
    private int position;

    public PlayerJoinedMessageBody(UUID lobbyId, Player player, int position){
        this.lobbyId = lobbyId;
        this.player = player;
        this.position = position;
    }

    public UUID getLobbyId(){ return lobbyId; }
    public Player getPlayer(){ return this.player; }
    public int getPosition(){ return this.position; }
}
