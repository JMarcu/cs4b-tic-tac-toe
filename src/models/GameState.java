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

    public Player setCellPlayer(int x, int y, Player player) {
        if(x < 0 || x > 2 || y < 0 || y > 2){
            throw new IllegalArgumentException("Cell coordinates must be greater than or equal to 0 and less than 3.");
        } else if(this.grid[x][y] != null){
            throw new IllegalArgumentException("Cell is already claimed.");
        } else if(player.equals(players.getValue0())){
            return makeMove(x, y, players.getValue0());
        } else if(player.equals(players.getValue1())){
            return makeMove(x, y, players.getValue1());
        } else{
            throw new IllegalArgumentException("Player is not a participant in this game.");
        }
    }

    @Override
    public void subscribe(Subscriber<? super NullType> subscriber) {
        this.subscribers.add(subscriber);
    }

    private Player checkRow(int x){
        return grid[x][0] != null && grid[x][0].equals(grid[x][1]) && grid[x][1].equals(grid[x][2])
            ? grid[x][0]
            : null;
    }

    private Player makeMove(int playToX, int playToY, Player player){
        this.grid[playToX][playToY] = player;
        
        int x = 0;
        boolean keepSearching = true;
        Player winner = null;
        while(keepSearching){
            if(x < 3){
                winner = checkRow(x);
                keepSearching = winner == null;
                x++;
            } else {
                keepSearching = false;

                if(
                    grid[0][0] != null && grid[0][0].equals(grid[1][1]) && grid[1][1].equals(grid[2][2]) ||
                    grid[0][2] != null && grid[0][2].equals(grid[1][1]) && grid[1][1].equals(grid[2][0])
                ){
                    winner = grid[1][1];
                }
            }
        }

        if(winner != null){
            this.winner = winner;
        }

        this.notifySubscribers();
        return winner;
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
