package models.ServerMessage;

import java.util.UUID;
import org.javatuples.Pair;

public class MoveMessageBody {
    private String jwt;
    private UUID lobbyId;
    private Pair<Integer, Integer> move;

    public MoveMessageBody(String jwt, UUID lobbyId, Pair<Integer, Integer> move){
        this.jwt = jwt;
        this.lobbyId = lobbyId;
        this.move = move;
    }

    public String getJWT(){ return jwt; }
    public UUID getLobbyId(){ return lobbyId; }
    public Pair<Integer, Integer> getMove(){ return move; }
}
