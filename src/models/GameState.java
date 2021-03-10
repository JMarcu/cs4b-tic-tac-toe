package models;

import java.util.ArrayList;
import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.SubmissionPublisher;
import org.javatuples.Pair;
import org.javatuples.Triplet;

/**
 * Models the state of a game of TicTacToe and provides an interface to mutate that state.
 * @author James Marcu
 */
public class GameState implements Publisher<GameState.Patch>  {
    /*==========================================================================================================
     * NESTED OBJECTS
     *==========================================================================================================*/

    /** Describes where in the game is in it's lifecycle. */
    public enum Status {
        /** A freshly created game. No players have taken a turn yet. */
        NEW, 
        /** The game has begun and players have taken turns, but it has not ended in either a draw or victory. */
        IN_PROGRESS,
        /** The game has ended, inconclusively, in a draw between the players. */
        DRAW, 
        /** The game has ended and been won by a player. Check {@link GameState.winner} for a description of the winning player. */
        WON, 
        /** The game has been abandoned. It is no longer being played, despite never finishing in a draw or victory. */
        ABANDONED
    }

    /**
     * Describes an atomic update to the game state. Unchanged fields are left as null. Objects of this type
     * are emitted to subscribers.
     */
    public class Patch {
        /** The player who's turn it is. */
        protected Player currentPlayer;

        /** The players in this game. */
        protected Pair<Player, Player> players;
        /** 
         * A tuple describing a player's move. The first index is a {@link Player} object describing the
         * player who made the move. The second and third indices are the x- and y-coordinates (respectively) 
         * of the grid cell they played to.
         */
        protected Triplet<Player, Integer, Integer> move;

        /** The game's current status. */
        protected Status status;

        /** The winner of the game. */
        protected Player winner;

        /** 
         * The player whose turn it is. Not defined for games with a status other than {@link Status.NEW} or 
         * {@link Status.IN_PROGRESS}. 
         * @return The current player, or null if the current player has not changed since the last patch.
         */
        public Player getCurrentPlayer(){ return currentPlayer; }

        /** 
         * The players who are playing this game. Not defined for games with a status other than {@link Status.NEW} or 
         * {@link Status.IN_PROGRESS}. 
         * @return The players in this game, or null if the current players have not changed since the last patch.
         */
        public Pair<Player, Player> getPlayers(){ return players; }

        /** 
         * Describes a player move onto the board, or null if no move.
         * @return The first index is a {@link Player} object describing the player who made the move. The second
         * and third indices are the x- and y-coordinates (respectively) of the grid cell they played to. Returns
         * null if no move has been played since the last patch.
         */
        public Triplet<Player, Integer, Integer> getMove(){ return move; }

        /**
         * Returns the game's current status.
         * @return The game's current status. Returns null if the status is unchanged since the last patch.
         */
        public Status getStatus(){ return status; }

        /**
         * Returns the winner of the game.
         * @return The winner of the game. Will return null if the winner has not changed since the last patch,
         * or if the winner has been revoked.
         */
        public Player getWinner(){ return winner; }
    }

    /*==========================================================================================================
     * CLASS VARIABLES
     *==========================================================================================================*/

    /** The index of the player whose turn it is in the {@link players} tuple. */
    private int currentPlayerIndex;

    /** A count of how many cells on the board are empty. Used to detect draws. */
    private int emptyCells;

    /** 
     * A model of the board. Always square with dimensions {@link GRID_SIZE}x{@link GRID_SIZE}. Elements are
     * null if the cell has not been played to, or equal to a player object from the {@link players} tuple if that
     * player has made a move to the cell.
     */
    private Player[][] grid;

    /** Flag for the type of game mode being played. */
    private GameMode gameMode;

    /** Stores the players in the game. */
    private Pair<Player, Player> players;

    /** A {@link java.util.concurrent.Flow.Publisher} implementation that handles our subscriptions. */
    private SubmissionPublisher<Patch> publisher;

    /**
     * Some game modes requires additional configuration, and that is stored here. For instance, {@link GameMode.BEST_OF_X}
     * uses this field to track the number of games being played, while {@link GameMode.BULLET} uses it to track the timer's
     * initial value.
     */
    private int secondaryOption;

    /** Whether or not the game is single player. Single player games are played against the AI. */
    private boolean singlePlayer;

    /** The current status of the game in it's lifecycle.  */
    private Status status;
    
    /**
     * Storage used for detecting win states. Each cell keeps a running total of the 'score' in a winnable lane (i.e. row,
     * column, or diagonal). When player one makes a play to the lane, that total increments; when player two makes a play,
     * it decrements. When the absolute value of the total is equal to the grid size, a player has won.
     * 
     * The first x={@link GRID_SIZE} elements correspond to the grid's rows, then
     * the next y={@link GRID_SIZE} elements (i.e. {@link GRID_SIZE} + y) correspond to the grid's columns. The final two
     * elements located at {@link GRID_SIZE} * 2 and ({@link GRID_SIZE} * 2) + 1 correspond to the left-to-right and
     * right-to-left diagonals. 
     */
    private ArrayList<Integer> victoryCounts;

    /** The winner of the game. Equal to null unless the game has been won. */
    private Player winner;

    /** The dimensions fo the grid. */
    private final int GRID_SIZE = 3;

    /** Creates a default game: single player, free play, and with both players set to null. */
    public GameState(){
        this(GameMode.FREE_PLAY, new Pair<Player, Player>(new Player(), new Player()), true, 0);
    }

    /** 
     * Creates a game for modes that do not need to configure a secondary option. 
     * @param gameMode The game mode that the game should operate in.
     * @param players A 2-tuple with the first and second players in the game.
     * @param singlePlayer True if player one should be versing the AI, false if both players are human.
     */
    public GameState(GameMode gameMode, Pair<Player, Player> players, Boolean singlePlayer){
        this(gameMode, players, singlePlayer, 0);
    }

    /** 
     * Creates a game for modes that do have a secondary option. 
     * @param gameMode The game mode that the game should operate in.
     * @param players A 2-tuple with the first and second players in the game.
     * @param singlePlayer True if player one should be versing the AI, false if both players are human.
     * @param secondaryOption The game mode's secondary option value. The meaning of this option is contextual
     * based on the game mode.
     */
    public GameState(GameMode gameMode, Pair<Player, Player> players, Boolean singlePlayer, int secondaryOption){
        this.gameMode = gameMode;
        this.players = players;
        this.singlePlayer = singlePlayer;
        this.secondaryOption = secondaryOption;

        this.currentPlayerIndex = 0;
        this.emptyCells = GRID_SIZE * GRID_SIZE;
        this.grid = new Player[GRID_SIZE][GRID_SIZE];
        this.publisher = new SubmissionPublisher<Patch>(Runnable::run, Flow.defaultBufferSize());
        this.status = Status.NEW;
        this.victoryCounts = new ArrayList<Integer>();
        this.winner = null;

        for(int i = 0; i < (GRID_SIZE * 2) + 2; i++){
            this.victoryCounts.add(0);
        }
    }
    
    /*==========================================================================================================
     * ACCESSORS & MUTATORS
     *==========================================================================================================*/

    /** 
     * Returns the player whose turn it is. 
     * @returns The player whose turn it is.
     */
    public Player getCurrentPlayer() { return (Player) this.players.getValue(this.currentPlayerIndex); }

    /** 
     * Returns the game mode. 
     * @returns The game mode.
     */
    public GameMode getGameMode() { return this.gameMode; }

    /**
     * Returns the size of the grid in this game.
     * @return The grid size.
     */
    public int getGridSize() { return this.GRID_SIZE; }

    /** 
     * Returns the players in the game. 
     * @returns A 2-tuple containing the players in the game.
     */
    public Pair<Player, Player> getPlayers() { return this.players; }
    
    /** 
     * Returns whether or not the game is single player. 
     * @returns True if the game is single player, false otherwise.
     */
    public boolean getSinglePlayer() { return this.singlePlayer; }
    
    /** 
     * Returns the game's secondary option. 
     * @returns The game secondary option. This is zero by default in game mode's without a secondary option.
     */
    public int getSecondaryOption() { return this.secondaryOption; }
    
    /** 
     * Returns the game's lifecycle status. 
     * @returns The game's lifecycle status.
     */
    public Status getStatus() { return this.status; }

    /** 
     * Returns a victoryArray for the current game state. This array is a clone of the internal array and
     * is safe to mutate.
     * @returns A victoryArray for the current game state.
     */
    public ArrayList<Integer> getVictoryArr() { 
        ArrayList<Integer> clone = new ArrayList<Integer>();
        for(Integer i : victoryCounts){
            clone.add(Integer.valueOf(i.intValue()));
        }
        return clone; 
    }
    
    /** 
     * Returns the game's winner. 
     * @returns The game's winner, or null if there is no winner.
     */
    public Player getWinner() { return this.winner; }

    /*==========================================================================================================
     * GAME LOGIC - methods for controlling the flow of the game
     *==========================================================================================================*/
    
    /**
     * Look up who has played to a certain cell.
     * @param x The x-coordinate of the cell.
     * @param y The y-coordinate of the cell.
     * @throws IllegalArgumentException Thrown if either x or y fails to satisfy 0 <= n < GRID_SIZE
     * @return Returns a Player object describing the player who controls cell [x, y], or null if no player has.
     */
    public Player getCell(int x, int y) {
        if(x < 0 || x > 2 || y < 0 || y > 2){
            throw new IllegalArgumentException("Cell coordinates must be greater than or equal to 0 and less than " + this.GRID_SIZE);
        } else{
            return this.grid[x][y];
        }
    }

    /**
     * Have a player play to a given cell. Marks the cell as theirs and checks for victory conditions. If the game 
     * continues, then the current player will be updated.
     * @param x The x-coordinate of the cell.
     * @param y The y-coordinate of the cell.
     * @throws IllegalArgumentException Thrown if: either x or y does not satisfy 0 <= n < {@link GRID_SIZE}, or  
     * the specified cell is not empty.
     */
    public void setCell(int x, int y) {
        if(x < 0 || x > 2 || y < 0 || y > 2){
            throw new IllegalArgumentException("Cell coordinates must be greater than or equal to 0 and less than " + GRID_SIZE);
        } else if(grid[x][y] != null){
            throw new IllegalArgumentException("Cell is already claimed.");
        } else{    
            //Mark the cell as having been played to by the current player.
            grid[x][y] = getCurrentPlayer();

            boolean victory = GameState.checkVictoryArr(
                victoryCounts, 
                new Pair<Integer, Integer>(x, y), 
                currentPlayerIndex == 0,
                GRID_SIZE
            );
            
            if(victory){
                //If the game has ended in a victory, update the game's status and winner properties.
                status = Status.WON;
                winner = getCurrentPlayer();

                //Set this array to null so that it can be garbage collected. It is no longer useful.
                victoryCounts = null;

                //Notifiy subscribers.
                notifySubscribers(new Patch(){
                    {
                        status = Status.WON;
                        move = new Triplet<Player, Integer, Integer>(GameState.this.getCurrentPlayer(), Integer.valueOf(x), Integer.valueOf(y));
                        winner = GameState.this.winner;
                    }
                });
            } else if(emptyCells == 1){
                //Update the status property.
                status = Status.DRAW;

                //Set this array to null so that it can be garbage collected. It is no longer useful.
                victoryCounts = null;

                //Notifiy subscribers.
                notifySubscribers(new Patch(){ 
                    {
                        status = Status.DRAW;
                        move = new Triplet<Player, Integer, Integer>(GameState.this.getCurrentPlayer(), Integer.valueOf(x), Integer.valueOf(y));
                    }
                });
            } else {
                //Decrement the count of empty cells.
                emptyCells--;
                
                //Switch turns to the next player.
                final Player playerWhoMoved = getCurrentPlayer();
                currentPlayerIndex = currentPlayerIndex == 0 ? 1 : 0;
    
                //Update subscribers of the move and that the current player has changed.
                notifySubscribers(new Patch(){
                    {
                        currentPlayer = GameState.this.getCurrentPlayer();
                        move = new Triplet<Player, Integer, Integer>(playerWhoMoved, Integer.valueOf(x), Integer.valueOf(y));
                    }
                });
            }
        }
    }

    /*==========================================================================================================
     * PUBLISHER INTERFACE - the publisher interface is satisfied by wrapping a 
     * {@link java.util.concurrent.SubmissionPublisher} behind the interface, not through direct implementation.
     * This publisher is functionally synchronous since all tasks are run on the current thread.
     *==========================================================================================================*/
    
     /** Subscribe to receive updates whenever the game's state updates. */
    @Override
    public void subscribe(Subscriber<? super Patch> subscriber) {
        this.publisher.subscribe(subscriber);
    }

     /**  Offers the patch to all subscribers. */
    private void notifySubscribers(Patch patch){
        this.publisher.offer(patch, null);
    }

    public static boolean checkVictoryArr(ArrayList<Integer> victoryArr, Pair<Integer, Integer> move, boolean isPlayerOne){
        return GameState.checkVictoryArr(victoryArr, move, isPlayerOne, 3);
    }

    public static boolean checkVictoryArr(ArrayList<Integer> victoryArr, Pair<Integer, Integer> move, boolean isPlayerOne, int gridSize){ 
        
        //The value by which to modify elements in the victoryCounts array.
        final int victoryMod = isPlayerOne ? 1 : -1;

        //Conveniance variables for offsets of the grid size.
        final int gridSizeLessOne = gridSize - 1;
        final int doubleGridSize = 2 * gridSize;

        //Increment/decrement (as appropriate) the row, column, and diagonals that the player's move was in.
        victoryArr.set(move.getValue0(), victoryArr.get(move.getValue0()) + victoryMod);
        victoryArr.set(gridSize + move.getValue1(), victoryArr.get(gridSize + move.getValue1()) + victoryMod);
        if(move.getValue0() == move.getValue1()){
            victoryArr.set(doubleGridSize, victoryArr.get(doubleGridSize) + victoryMod);
        }
        if(move.getValue0() + move.getValue1() == gridSizeLessOne){
            victoryArr.set(doubleGridSize + 1, victoryArr.get(doubleGridSize + 1) + victoryMod);
        }

        //First check for victory in the row, column, and (if applicable) diagonals that contain the cell the player
        //has just made a move to. 
        return 
            Math.abs(victoryArr.get(move.getValue0())) >= gridSize || 
            Math.abs(victoryArr.get(gridSize + move.getValue1())) >= gridSize ||
            (move.getValue0() == move.getValue1() && Math.abs(victoryArr.get(doubleGridSize)) >= gridSize) ||
            (move.getValue0() + move.getValue1() == gridSizeLessOne && Math.abs(victoryArr.get((doubleGridSize + 1))) >= gridSize);
    }
}
