package models;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import javafx.util.Pair;

public class Game {
    
    private long start;
    private long end;
    private List<UUID> spectators; // takes PlayerIds instead of Players, so Integer
    private List<Pair<Integer,Integer>> moves;
    private UUID winner;
    private Integer gameId;
    private Pair<UUID,UUID> players;
    private UUID creator; // new
    private List<Long> moveTimes; // new

    public Game(){ // probably change what it takes in
    }

    public Game(Pair<UUID,UUID> people, Integer gameNumber, UUID gameCreator){ // probably change what it takes in
        start = System.currentTimeMillis();
        end = -1;
        players = people;
        winner = UUID.fromString("");
        gameId = gameNumber;
        moves = new ArrayList<Pair<Integer,Integer>>();
        spectators = new ArrayList<UUID>();
        creator = gameCreator;
        moveTimes = new ArrayList<Long>();
    }

    public Game(long gameStart, long gameEnd, List<UUID> gameSpectators, List<Pair<Integer,Integer>> gameMoves, UUID gameWinner, Integer gameNumber, Pair<UUID,UUID> people, UUID gameCreator, List<Long> gameMoveTimes){ // probably change what it takes in
        start = gameStart;
        end = gameEnd;
        players = people;
        winner = gameWinner;
        gameId = gameNumber;
        moves = gameMoves;
        spectators = gameSpectators;
        creator = gameCreator;
        moveTimes = gameMoveTimes;
    }
    public void setStart(long gameStart){
        start = gameStart;
    }

    public void setGameId(int gameNum)
    {
        gameId = gameNum;
    }
    public void addSpecator(UUID spectator){
        spectators.add(spectator);
    }

    public void setEnd(){
        end = System.currentTimeMillis();
    }

    public void setEndManually(long gameEnd){
        end = gameEnd;
    }

    public void setAllSpectators(List<UUID> gameSpectators){
        spectators = gameSpectators;
    }

    public void addMove(Pair<Integer,Integer> move){
        moves.add(move);
    }

    public void setAllMoves(List<Pair<Integer,Integer>> allMoves){
        moves = allMoves;
    }

    public void setWinner(UUID winnerId){
        winner = winnerId;
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

    public List<Pair<Integer,Integer>> getMoves(){
        return moves;
    }

    public Pair<UUID,UUID> getPlayers(){
        return players; // first player, second player
    }

    public void setPlayers(Pair<UUID,UUID> people){
        players = people;
    }

    public List<UUID> getSpectators(){
        return spectators;
    }

    public List<Long> getMoveTimes(){
        return moveTimes;
    }

    public void setMoveTimes(List<Long> gameMoveTimes){
        moveTimes = gameMoveTimes;
    }

    public UUID getCreator(){
        return creator;
    }

    public void setCreator(UUID gameCreator){
        creator = gameCreator;
    }
}
