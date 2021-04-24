package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Lobby implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private UUID id;
    private String name;
    private ArrayList<Player> players;
    private GameState.Status status;

    public Lobby(String name){
        this(UUID.randomUUID(), name, new ArrayList<Player>(), GameState.Status.NEW);
    }

    public Lobby(UUID id, String name, ArrayList<Player> players, GameState.Status status){
        this.id = id;
        this.name = name;
        this.players = players;
        this.status = status;
    }

    public UUID getId(){ return this.id; }
    public String getName(){ return this.name; }
    public List<Player> getPlayers(){ return Collections.unmodifiableList(this.players); }
    public GameState.Status getStatus(){ return this.status; }
}
