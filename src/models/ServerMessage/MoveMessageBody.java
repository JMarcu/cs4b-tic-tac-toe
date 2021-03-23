package models.ServerMessage;

import java.io.Serializable;
import java.util.UUID;

import org.javatuples.Pair;

public class MoveMessageBody implements Serializable {
    private static final long serialVersionUID = 1L;

    private Pair<Integer, Integer> cell;
    private UUID playerId;

    public MoveMessageBody(Pair<Integer, Integer> cell, UUID playerId){
        this.cell = cell;
        this.playerId = playerId;
    }

    public Pair<Integer, Integer> getCell(){ return this.cell; }
    public UUID getPlayerId(){ return this.playerId; }
}
