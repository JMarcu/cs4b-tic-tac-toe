package models;

import java.util.ArrayList;
import java.util.UUID;
import javax.swing.tree.DefaultMutableTreeNode;
import javafx.scene.paint.Color;
import org.javatuples.Pair;
import org.javatuples.Triplet;

/**
 * Represents a player with built-in logic for playing moves, rather than one controlled by a human.
 * @author Brendon
 * @author James Marcu
 */
public class Ai extends Player{

    /*==========================================================================================================
     * LIFECYCLE
     *==========================================================================================================*/

    /** Default constructor. Named "Player 2", with a black 'O' marker. */
    public Ai(){
        this(Color.BLACK, "Player 2", MarkerShape.O);
    }

     /** 
     * Parameterized constructor.
     * @param color The color of the AI's marker.
     * @param name  The name displayed for the AI.
     * @param shape The shape of the AI's marker.
     */
    public Ai(Color color, String name, MarkerShape shape) {
        super(color, UUID.randomUUID(), name, shape);
        this.isAi = true;
    }

    /** 
     * Construct a new AI from a player object. The AI and the player will share a name, and will have the same
     * marker (i.e. it's color and shape). They will not share an ID.
     * @param player The player whose properties should be copied over.
     */
    public Ai(Player player){
        this(player.getColor(), player.getName(), player.getShape());
    }

    /** 
     * Constructs a new player object using the properties of the current AI. The player will be initialized with
     * the same name and mark (i.e. color & shape) as the AI, as well as the same ID. This means that the AI and
     * the created player object will be considered equivalent by program logic.
     */
    public Player toPlayer(){
        return new Player(getColor(), UUID.randomUUID(), getName(), getShape());
    }

    /*==========================================================================================================
     * GAME LOGIC
     *==========================================================================================================*/

    /**
     * Computes the AI's move given a particular game state.
     * @param gameState Describes the current board state, and any rules governing the game.
     * @returns A tuple describing the AI's desired play in the for [x, y]. The first argument will be the
     * x-coordinate of the play, and the second argument will be the y-coordinate.
     */
    public Pair<Integer, Integer> generateMove(GameState gameState){
        //The root of the decision tree will be a dummy node. The children of this node are the plays the AI will be
        //choosing from amongst.
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(new Triplet<Integer, Integer, Integer>(null, null, null));

        //Compile a list of all cells on the board that are empty.
        ArrayList<Pair<Integer, Integer>> emptyCells = new ArrayList<Pair<Integer, Integer>>();
        for(int i = 0; i < gameState.getGridSize(); i++){
            for(int j = 0; j < gameState.getGridSize(); j++){
                if(gameState.getCell(i, j) == null){
                    emptyCells.add(new Pair<Integer, Integer>(Integer.valueOf(i), Integer.valueOf(j)));
                }
            }
        }

        //Decide which move to make. The returned value takes the form [x-coordinate, y-coordinate, weight].
        Triplet<Integer, Integer, Integer> bestMove = findBestMove(
            root,
            emptyCells,
            gameState.getVictoryArr(),
            gameState.getGridSize()
        );

        //The third value in the tuple is the weight of the move in the decision tree. Remove this and return only the
        //move's [x, y] coodinate pair.
        return bestMove.removeFrom2();
    }
    
    /**
     * For a given set of available plays in a given board state, select the best possible play.
     * @param node The node in the decision tree representing the most recent move.
     * @param emptyCells A list of empty cells on the board from which we are selecting the best possible play.
     * @param victoryArr A victory array representing the current progress towards victory on the board.
     * @param gridSize The board's dimensions.
     * @return A tuple representing the best possible play from among the options. The tuple's first and second values
     * represent (respectively) the play's x- and y-coordinates on the grid, while the tuple's third value is the weight
     * of the play.
     */
    @SuppressWarnings("unchecked")
    private Triplet<Integer, Integer, Integer> findBestMove(DefaultMutableTreeNode node, ArrayList<Pair<Integer, Integer>> emptyCells, ArrayList<Integer> victoryArr, int gridSize){
        /* At even depths, we are choosing a play from amongst the node's children for the AI to make. We want to minimize
        the weight of the selected play, since the AI is always player two and victories for player 2 are assigned a weight
        of -10. For odd depths the opposite is true; we maximize to select a simulated play for player one. */
        boolean isMax = (node.getPath().length % 2) == 0;

        //The default value of the best move is set to +100 or -100, so that all legal plays will be higher/lower than
        //the default value's weight (with respect to whether we are maximizing or minimizing).
        Triplet<Integer, Integer, Integer> bestMove = new Triplet<Integer, Integer, Integer>(null, null, isMax ? -100 : 100);

        //We plan to iterate through every possible play (i.e. the list of empty cells), but this boolean allows us
        //to break out early if we wish to prune the remaining options.
        boolean prune = false;

        //Iterate over every empty cell on the board, as a list of possible plays.
        int cellIndex = 0;
        while(cellIndex < emptyCells.size() && !prune){
            //Create a pair object to represent the move, and then house that pair inside a new tree node as a
            //child of the current node.
            Pair<Integer, Integer> cell = emptyCells.get(cellIndex);
            DefaultMutableTreeNode child = new DefaultMutableTreeNode();
            node.add(child);

            
            //Create a deep clone of the victory array so that the current node's descendants do not modify each
            //other's victory arrays when applying moves to their own copies.
            ArrayList<Integer> vArrClone = new ArrayList<Integer>();
            for(Integer i : victoryArr){
                vArrClone.add(Integer.valueOf(i.intValue()));
            }

            /* Check whether the current cell is a winning play. This method updates the victory array to account
            for the move. The isMax variable is being used as a proxy for isPlayerOne, since we know that if we 
            are maximizing then we are choosing a play for player one, while if we are minimising we are
            choosing a play for player two (i.e. the AI). */
            boolean isVictory = GameState.checkVictoryArr(vArrClone, cell, isMax);
            
            if(isVictory){
                /* If the game has ended in a victory, then we know the 'current' player is the winner. That will
                be player one if we are maximizing and player two (ie the AI) if we are minimizing). Victories for
                player one are given a weight of +10, while victories for player two are assigned -10. */ 
                int weight = isMax ? 10 : -10;
                child.setUserObject(cell.add(Integer.valueOf(weight)));

                /* If one of the children of the current node results in a victory, then we can prune the other 
                children since if the player is playing optimally they will never not select victory. In practice,
                this means breaking out of the loop iterating over possible plays (ie empty cells) early. */
                prune = true;
            } else if(emptyCells.size() == 1){
                /* If the game did not end in a victory, but there was only one legal play, then every cell has
                 now been filled and the game is a draw. Draws are assigned a weight of zero. */
                child.setUserObject(cell.add(Integer.valueOf(0)));
            } else{
                //If the game is neither a victory nor a draw, then the next player will need to choose a move.

                /* Create cloned list of empty cells, less the one that was just 'played'. These are the options
                the other player will have to choose from. The copy can be shallow, since tuples are immutable. */
                ArrayList<Pair<Integer, Integer>> emptyCellsClone = new ArrayList<Pair<Integer, Integer>>();
                for(Pair<Integer, Integer> emptyCell : emptyCells){
                    if(emptyCell != cell){
                        emptyCellsClone.add(emptyCell);
                    }
                }

                /* Set the child node's value as the [x, y] coordinate pair of the play we just selected, with an
                undefined weight. Non-leaf nodes have no weight until they inherit one from a descendent. */
                child.setUserObject(cell.add((Integer)null));

                //Start the move selection algorithm over with the play we just selected as the new parent node.
                findBestMove(child, emptyCellsClone, vArrClone, gridSize);
            }

            /* Once we have a weight for the play, determine whether it is a better or worse play than our current
            best known play */
            Triplet<Integer, Integer, Integer> childData = (Triplet<Integer, Integer, Integer>)child.getUserObject();
            if((isMax && childData.getValue2() > bestMove.getValue2()) || (!isMax && childData.getValue2() < bestMove.getValue2())){
                bestMove = childData;
            }

            //Go to the next empty cell and check it too.
            cellIndex++;
        }

        //Update the current node's weight to be equal to the weight of the best play among its children, then return it.
        Triplet<Integer, Integer, Integer> currentData = (Triplet<Integer, Integer, Integer>)node.getUserObject();
        node.setUserObject(currentData.setAt2(bestMove.getValue2()));
        return bestMove;
    }
}
