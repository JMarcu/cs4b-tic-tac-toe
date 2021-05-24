package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import org.javatuples.Pair;

public class Lobby {

    protected boolean aiLobby;
    private UUID id;
    private String name;
    private Pair<Player, Player> players;
    protected HashMap<UUID, Boolean> spectators;
    private GameState.Status status;

    public Lobby(String name){
        this(UUID.randomUUID(), name, new Pair<Player, Player>(null, null), GameState.Status.NEW);
    }

    public Lobby(UUID id, String name, Pair<Player, Player> players, GameState.Status status){
        this.id = id;
        this.name = name;
        this.players = players;
        this.spectators = new HashMap<UUID, Boolean>();
        this.status = status;
    }

    public void addSpectator(UUID playerId){
        this.spectators.put(playerId, true);
    }

    public void removeSpectator(UUID playerId){
        if(this.spectators.containsKey(playerId)){
            this.spectators.put(playerId, false);
        }
    }

    public UUID getId(){ return this.id; }
    public String getName(){ return this.name; }
    public Pair<Player, Player> getPlayers(){ return this.players; }
    public ArrayList<UUID> getSpectators() { return new ArrayList<UUID>(spectators.keySet()); }
    public GameState.Status getStatus(){ return this.status; }
    public boolean isAiLobby(){ return aiLobby; }

    public int getPlayerCount(){
        int playerCount = 0;
        if(players.getValue0() != null){
            playerCount++;
        }
        if(players.getValue1() != null){
            playerCount++;
        }
        return playerCount;
    }

    public int getSpectatorCount(){
        return this.spectators.size();
    }

    public String getStatusString(){
        return status.toString();
    }
}
