package models;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import javafx.util.Pair;
import models.Game;

public class GameForTable {
    
    private long start;
    private long end;
    private String spectators; // takes PlayerIds instead of Players, so Integer
    private List<Pair<String,Long>> moves;
    private UUID winner;
    private Integer gameId;
    private UUID playerOne;
    private UUID playerTwo;
    private UUID creator; // new

    public GameForTable(Game game){ // probably change what it takes in
        start = game.getStart();
        end = game.getEnd();
        playerOne = game.getPlayers().getKey();
        playerTwo = game.getPlayers().getValue();
        winner = game.getWinner();
        gameId = game.getGameId();
        spectators = game.getSpectators().toString(); //might need to change
        creator = game.getCreator();

        List<Pair<Integer, Integer>> movePrep = game.getMoves();
        List<Long> moveTimePrep = game.getMoveTimes();
        moves = new ArrayList<Pair<String,Long>>();

        for (int i = 0; i < movePrep.size(); i++)
        {
            String move = movePrep.get(i).getKey().toString(i) + movePrep.get(i).getValue().toString(i);
            moves.add(new Pair(move, moveTimePrep.get(i)));
        }

    }
   
    public UUID getWinner(){
        return winner;
    }

    public Integer getGameId(){
        return gameId;
    }

    public long getStart(){
        return start;
    }

    public long getEnd(){
        return end;
    }

    public List<Pair<String,Long>> getMoves(){
        return moves;
    }

    public UUID getPlayerOne(){
        return playerOne; // first player, second player
    }

    public UUID getPlayerTwo(){
        return playerTwo; // first player, second player
    }

    public String getSpectators(){
        return spectators;
    }

    public UUID getCreator(){
        return creator;
    }
}
