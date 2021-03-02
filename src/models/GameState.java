package models;

import java.util.ArrayList;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;

import javax.lang.model.type.NullType;

import org.javatuples.Pair;

public class GameState implements Publisher<NullType>  {
    Player[][]           grid;
    GameMode             gameMode;
    Pair<Player, Player> players;
    int                  secondaryOption;
    boolean              singlePlayer;
    private ArrayList<Subscriber<? super NullType>> subscribers;
    
    public Player winner;
    public boolean isDraw;

    public GameState(){
        this(GameMode.FREE_PLAY, new Pair<Player, Player>(null, null), true, 0);
    }

    public GameState(GameMode gameMode, Pair<Player, Player> players, Boolean singlePlayer){
        this(gameMode, players, singlePlayer, 0);
    }

    public GameState(GameMode gameMode, Pair<Player, Player> players, Boolean singlePlayer, int secondaryOption){
        this.gameMode = gameMode;
        this.players = players;
        this.singlePlayer = singlePlayer;
        this.secondaryOption = secondaryOption;

        this.grid = new Player[3][3];
        this.subscribers = new ArrayList<Subscriber<? super NullType>>();
    }
    
    //GETTERS
    public GameMode             getGameMode()        { return this.gameMode; }
    public Pair<Player, Player> getPlayers()         { return this.players; }
    public boolean              getSinglePlayer()    { return this.singlePlayer; }
    public int                  getSecondaryOption() { return this.secondaryOption; }

    public Player getCellPlayer(int x, int y) {
        if(x < 0 || x > 2 || y < 0 || y > 2){
            throw new IllegalArgumentException("Cell coordinates must be greater than or equal to 0 and less than 3.");
        } else{
            return this.grid[x][y];
        }
    }

    public void setCellPlayer(int x, int y, Player player) {
        if(x < 0 || x > 2 || y < 0 || y > 2){
            throw new IllegalArgumentException("Cell coordinates must be greater than or equal to 0 and less than 3.");
        } else if(this.grid[x][y] != null){
            throw new IllegalArgumentException("Cell is already claimed.");
        } else if(player.equals(players.getValue0())){
            this.grid[x][y] = players.getValue0();
            this.notifySubscribers();
        } else if(player.equals(players.getValue1())){
            this.grid[x][y] = players.getValue1();
            this.notifySubscribers();
        } else{
            throw new IllegalArgumentException("Player is not a participant in this game.");
        }
    }

    @Override
    public void subscribe(Subscriber<? super NullType> subscriber) {
        this.subscribers.add(subscriber);
    }

    private void notifySubscribers(){
        final ArrayList<Subscriber<? super NullType>> toRemove = new ArrayList<>();
        this.subscribers.forEach((Subscriber<? super NullType> subscriber) -> {
            if(subscriber != null){
                subscriber.onNext(null);;
            } else{
                toRemove.add(subscriber);
            }
        });
        toRemove.forEach((Subscriber<? super NullType> subscriber) -> {
            this.subscribers.remove(subscriber);
        });
    }
}
