package models;
import java.util.ArrayList;
import java.util.UUID;
import javax.swing.tree.DefaultMutableTreeNode;
import javafx.scene.paint.Color;
import org.javatuples.Pair;
import org.javatuples.Triplet;

public class Ai extends Player{

    /*==========================================================================================================
     * CLASS VARIABLES
     *==========================================================================================================*/

     
    private static Player player; //Dummy object that to find best xy values for the Ai object.

    private static Player opponentPlayer; //Dummy object to find best xy values for the Ai object.

     /** 
     * Constructs a new Ai object by using the super class constructor.
     * @param color The color of the Ai's marker.
     * @param id    The Ai's uuid.
     * @param name  The name of the Ai.
     * @param shape The shape of the Ai's marker.
     */
    public Ai(Color color, String name, MarkerShape shape) {
        super(color, UUID.randomUUID(), name, shape);
        this.isAi = true;

        player = new Player(color == null ? Color.BLACK : color, UUID.randomUUID(), "Player 1", MarkerShape.X);
        opponentPlayer = new Player(color == null ? Color.BLACK : color, UUID.randomUUID(), "Player 1", MarkerShape.O);
    }

    public Ai(Player player){
        this(player.getColor(), player.getName(), player.getShape());
    }

    public Player toPlayer(){
        return new Player(getColor(), UUID.randomUUID(), getName(), getShape());
    }

    /*==========================================================================================================
     * ACCESSORS & MUTATORS
     *==========================================================================================================*/

    /**
     * This method will determine the best play that the AI can make with the current state of the board.
     * Returns the best pair of x and y values for the AI player to play.
     * @param gameState The current state  of the game.
     * @return the best pair of x and y values for the AI player to play.
     */
    // @SuppressWarnings("unchecked")
    // public Pair<Integer, Integer> generateMove(GameState gameState){
    //     System.out.println("============================== GENERATE MOVE ==============================");
    //     DefaultMutableTreeNode root = new DefaultMutableTreeNode(new Triplet<Integer, Integer, Integer>(null, null, null));

    //     ArrayList<Pair<Integer, Integer>> emptyCells = new ArrayList<Pair<Integer, Integer>>();
    //     for(int i = 0; i < gameState.getGridSize(); i++){
    //         for(int j = 0; j < gameState.getGridSize(); j++){
    //             if(gameState.getCell(i, j) == null){
    //                 emptyCells.add(new Pair<Integer, Integer>(Integer.valueOf(i), Integer.valueOf(j)));
    //             }
    //         }
    //     }

    //     populateTree(root, emptyCells, gameState.getVictoryArr(), gameState.getGridSize(), true);
    //     System.out.println("done populating tree");

    //     miniMax(root, true, -1);
    //     System.out.println("done miniMax");

    //     Triplet<Integer, Integer, Integer> bestMove = new Triplet<Integer, Integer, Integer>(null, null, -100);
    //     for(int i = 0; i < root.getChildCount(); i++){
    //         DefaultMutableTreeNode child = (DefaultMutableTreeNode) root.getChildAt(i);
    //         Triplet<Integer, Integer, Integer> childData = (Triplet<Integer, Integer, Integer>) child.getUserObject();
    //         if(childData.getValue2() > bestMove.getValue2()){
    //             bestMove = childData;
    //         }
    //     }
        
    //     return bestMove.removeFrom2();
    // }
     
    /** */
    // @SuppressWarnings("unchecked")
    // private void miniMax(DefaultMutableTreeNode node, boolean isMax, int depth){
    //     if(depth == 0){
    //         System.out.println("Move: " + node.getUserObject());
    //         System.out.println("Child Count: " + node.getChildCount());
    //     }
    //     if(!node.isLeaf()){
    //         int bestWeight = isMax ? -100 : 100;
    //         int childIndex = 0;
    //         boolean prune = false;
    //         ArrayList<DefaultMutableTreeNode> children = new ArrayList<DefaultMutableTreeNode>();
    //         do{
    //             DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(childIndex);
    //             miniMax(child, !isMax, depth + 1);
    //             int childWeight = ((Triplet<Integer, Integer, Integer>)child.getUserObject()).getValue2();

    //             if(depth == 0){
    //                 children.add(child);
    //             }

    //             if((isMax && childWeight > bestWeight) || (!isMax && childWeight < bestWeight)){
    //                 bestWeight = childWeight;
    //             }

    //             if((isMax && bestWeight == 10) || (!isMax && bestWeight == -10)){
    //                 prune = true;
    //             } else{
    //                 childIndex++;
    //             }
    //         } while(!prune && childIndex < node.getChildCount());

    //         if(depth == 0){
    //             System.out.print("children: ");
    //             for(DefaultMutableTreeNode child : children){
    //                 System.out.print(child.getUserObject() + " ");
    //             }
    //             System.out.println("");
    //             System.out.println((isMax ? "Maximizing" : "Minimizing") + " our best weight is " + bestWeight);
    //             System.out.println();
    //         }

    //         node.setUserObject(
    //             ((Triplet<Integer, Integer, Integer>)node.getUserObject()).setAt2(bestWeight)
    //         );
    //     }
    // }

    /**
     * The methods creates a tree of all the permutations of the current board state 
     * and sets weight to the leaf nodes.
     * @param tree The tree that represents all of the permutations of the current board state.  
     * @param emptyCells An arraylist of empty cells from the board.
     * @param victoryArr And arraylist that holds number values that determine if a win condition has been met.
    //  */
    // private void populateTree(DefaultMutableTreeNode node, ArrayList<Pair<Integer, Integer>> emptyCells, ArrayList<Integer> victoryArr, int gridSize, boolean isPlayerOne){
    //     for(int cellIndex = 0; cellIndex < emptyCells.size(); cellIndex++){
    //         Pair<Integer, Integer> cell = emptyCells.get(cellIndex);
            
    //         ArrayList<Integer> vArrClone = new ArrayList<Integer>();
    //         for(Integer i : victoryArr){
    //             vArrClone.add(Integer.valueOf(i.intValue()));
    //         }

    //         boolean isVictory = GameState.checkVictoryArr(vArrClone, cell, isPlayerOne);

    //         DefaultMutableTreeNode child = new DefaultMutableTreeNode();
    //         node.add(child);

    //         if(isVictory || node.getChildCount() == 1){
    //             int weight = evaluate(vArrClone, cell, gridSize);
    //             child.setUserObject(cell.add(Integer.valueOf(weight)));
    //         } else{
    //             ArrayList<Pair<Integer, Integer>> emptyCellsClone = new ArrayList<Pair<Integer, Integer>>();
    //             for(Pair<Integer, Integer> emptyCell : emptyCells){
    //                 if(emptyCell != cell){
    //                     emptyCellsClone.add(emptyCell);
    //                 }
    //             }
    //             child.setUserObject(cell.add((Integer)null));

    //             populateTree(child, emptyCellsClone, vArrClone, gridSize, !isPlayerOne);
    //         }
    //     }
    // }

    // private int evaluate(ArrayList<Integer> victoryArr, Pair<Integer, Integer> move, int gridSize){
    //     int i = 0;
    //     boolean keepSearching = true;

    //     while(keepSearching && i < victoryArr.size()){
    //         if(Math.abs(victoryArr.get(i)) >= gridSize){
    //             keepSearching = false;
    //         } else{
    //             i++;
    //         }
    //     }
        
    //     if(i == victoryArr.size()){
    //         return 0;
    //     } else if(victoryArr.get(i) > 0){
    //         return -10;
    //     } else{
    //         return 10;
    //     }
    // }

    /**
     * Returns whether the player is an Ai.
     * @return True if the player is an Ai, false if they are not.
     */
    @Override
    public boolean getIsAI() {return true;}

    /**
     * Returns whether there are any available cells on the board.
     * @param  board A model of the playable board by using player objects.
     * @return True if there are any available cells, false if all cells are taken.
     */
    static boolean isMovesLeft(Player[][] board){
        for(int i = 0; i < 3; i ++){
            for(int j = 0; j < 3; j++){
                if(board[i][j] == null)
                    return true;
            }
        }
        return false;
    }

    /**
     * Returns Returns a value based on who is winning.
     * @param  board A model of the playable board by using player objects.
     * @return Returns a value based on who is winning.
     */
    static int evaluate(Player[][] board){

        //Checks rows for a victory.
        for(int i = 0; i < 3; i++){

            if (board[i][0] == board[i][1] && board[i][1] == board[i][2])
            {
                if (board[i][0] == player)
                return +10;
                else if (board[i][0] == opponentPlayer)
                return -10;
            }
        } 

        //Checks columns for a victory.
        for(int i = 0; i < 3; i++){

            if (board[0][i] == board[1][i] && board[1][i] == board[2][i])
            {
                if (board[0][i] == player)
                return +10;
                else if (board[0][i] == opponentPlayer)
                return -10;
            }
        } 

        //Checking for Diagonals for X or O victory.
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2])
        {
            if (board[0][0] == player)
                return +10;
            else if (board[0][0] == opponentPlayer)
                return -10;
        }

        if (board[0][2] == board[1][1] && board[1][1] == board[2][0])
        {
            if (board[0][2] == player)
                return +10;
            else if (board[0][2] == opponentPlayer)
                return -10;
        }

        //Returns zero if no winning condition was met.
        return 0;
    }

    /**
     * This methods considers all possible choice and determines the best play for the Ai.
     * Returns The value of the board.
     * @param board A model of the playable board by using player objects. 
     * @param depth The depth of the search 
     * @param isMax A boolean that determines if the current object is a maximizer or minimizer
     * @param gameState gameState The current state  of the game.
     * @return The value of the board.
     */
    static int miniMax(Player[][] board, int depth, boolean isMax){

        int score = evaluate(board);

        //If maximizer has won return the score.
        if(score == 10){
            return score;
        }

        //If minimizer has won return the score.
        if(score == -10){
            return score;
        }

        if(isMovesLeft(board) == false){
            return 0;
        }

        // If this maximizer's move
        if (isMax)
        {
            int best = Integer.MIN_VALUE;
    
            // Traverse all cells.
            for (int i = 0; i < 3; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    // Check if cell is empty.
                    if (board[i][j]== null)
                    {
                        // Make the move.
                        board[i][j] = player;

                     //   System.out.println("Max Board[" + i + "]["+ j +"]: " + board[i][j]);
    
                        // Call minimax recursively and choose the maximum value.
                        best = Math.max(best, miniMax(board, depth + 1, !isMax));
    
                        // Undo the move.
                        board[i][j] = null;

                     //   System.out.println("Max Board[" + i + "]["+ j +"]: " + board[i][j]);
                    }
                }
            }
            return best;
        }
 
        // If this minimizer's move.
        else
        {
            int best = Integer.MAX_VALUE;
    
            // Traverse all cells.
            for (int i = 0; i < 3; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    // Check if cell is empty.
                    if (board[i][j] == null)
                    {
                        // Make the move.
                       board[i][j] = opponentPlayer;

                      // System.out.println("Mini Board[" + i + "]["+ j +"]: " + board[i][j]);
    
                        // Call minimax recursively and choose the minimum value.
                        best = Math.min(best, miniMax(board, 
                                        depth + 1, !isMax));
    
                        // Undo the move.
                        board[i][j] = null;

                       // System.out.println("Mini Board[" + i + "]["+ j +"]: " + board[i][j]);
                    }
                }
            }
            return best;
        }
    }

    /**
     * This method will determine the best play that the AI can make with the current state of the board.
     * Returns the best pair of x and y values for the AI player to play.
     * @param gameState The current state  of the game.
     * @return the best pair of x and y values for the AI player to play.
     */
    public Pair<Integer, Integer> generateMove(GameState gameState){

        Player[][] board = (gameState.getGrid().clone();

        int x = 0;
        int y=0;

        int bestVal = Integer.MIN_VALUE; //

        //Pair<Integer, Integer> bestMove = new Pair<Integer,Integer>(-1, -1); //The xy pair for the best cell placement for the Ai.

        for(int i = 0; i < 3; i ++){
            for(int j = 0; j < 3; j ++){
                
                //checks if the cell is empty.
                if(board[i][j] == null){

                   // System.out.println("Board[" + i + "]["+ j +"] was equal to null");

                    //making a move .
                    board[i][j] = player;

                    //evaluting the played move.
                    int moveVal = miniMax(board, 0, false);

                    //Undoing move.
                    board[i][j] = null;

                    // If the value of the current move is
                    // more than the best value, then update
                    // best.
                    if (moveVal > bestVal)
                    {
                        x=i;
                        y=j;

                        bestVal = moveVal;
                    }
                }
            }
        }  

        Pair<Integer, Integer> bestMove = new Pair<Integer,Integer>(x, y); //The xy pair for the best cell placement for the Ai.
        
        System.out.printf("The value of the best Move " + 
                             "is : %d\n\n", bestVal);

                             System.out.println("bestMove.getAt0(): " + bestMove.getValue0());
                        System.out.println("bestMove.getAt1(): " + bestMove.getValue1());
        return bestMove;
    }
}
