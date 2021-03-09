// package models;
// import java.util.ArrayList;
// import java.util.UUID;

// import javax.swing.tree.DefaultMutableTreeNode;
// import javax.swing.tree.DefaultTreeModel;
// import javax.swing.tree.TreeNode;

// import javafx.scene.paint.Color;
// import org.javatuples.Pair;
// import org.javatuples.Triplet;
// import org.junit.Ignore;

// import java.util.ArrayList;

// public class Ai extends Player{

//     /*==========================================================================================================
//      * CLASS VARIABLES
//      *==========================================================================================================*/

//      /** 
//      * Constructs a new Ai object by using the super class constructor.
//      * @param color The color of the Ai's marker.
//      * @param id    The Ai's uuid.
//      * @param name  The name of the Ai.
//      * @param shape The shape of the Ai's marker.
//      */
//      public Ai(Color color, UUID id, String name, MarkerShape shape) {
//         super(color, id, name, shape);
//     }

//     /*==========================================================================================================
//      * ACCESSORS & MUTATORS
//      *==========================================================================================================*/

//     /**
//      * Returns whether the player is an Ai.
//      * @return True if the player is an Ai, false if they are not.
//      */
//     @Override
//     public boolean getIsAI() {return true;}

//     /**
//      * Returns whether there are any available cells on the board.
//      * @param  board A model of the playable board by using player objects.
//      * @return True if there are any available cells, false if all cells are taken.
//      */
//     static boolean isMovesLeft(Player[][] board){
//         for(int i = 0; i < 3; i ++){
//             for(int j = 0; j < 3; j++){
//                 if(board[i][j] == null)
//                     return true;
//             }
//         }
//         return false;
//     }

//     /**
//      * Returns Returns a value based on who is winning.
//      * @param  board A model of the playable board by using player objects.
//      * @return Returns a value based on who is winning.
//      */
//     static int evaluate(Player[][] board){

//         //Checks rows for a victory.
//         for(int i = 0; i < 3; i++){

//             if (board[i][0] == board[i][1] && board[i][1] == board[i][2])
//             {
//                 if (board[i][0] == ai)
//                 return +10;
//                 else if (board[i][0] == opponentPlayer)
//                 return -10;
//             }
//         } 

//         //Checks columns for a victory.
//         for(int i = 0; i < 3; i++){

//             if (board[0][i] == board[1][i] && board[1][i] == board[2][i])
//             {
//                 if (board[0][i] == ai)
//                 return +10;
//                 else if (board[0][i] == opponentPlayer)
//                 return -10;
//             }
//         } 

//         //Checking for Diagonals for X or O victory.
//         if (board[0][0] == board[1][1] && board[1][1] == board[2][2])
//         {
//             if (board[0][0] == ai)
//                 return +10;
//             else if (board[0][0] == opponentPlayer)
//                 return -10;
//         }

//         if (board[0][2] == board[1][1] && board[1][1] == board[2][0])
//         {
//             if (board[0][2] == ai)
//                 return +10;
//             else if (board[0][2] == opponentPlayer)
//                 return -10;
//         }

//         //Returns zero if no winning condition was met.
//         return 0;
//     }

//     /** */
//     @SuppressWarnings("unchecked")
//     static int miniMax(DefaultTreeModel tree, boolean isMax){
//         DefaultMutableTreeNode root = (DefaultMutableTreeNode)tree.getRoot();
//         Triplet<Integer, Integer, Integer> rootData = (Triplet<Integer, Integer, Integer>)root.getUserObject();
//         if(root.isLeaf()){
//             return rootData.getValue2();
//         } else{            
//             int childIndex = 1;
//             boolean prune = false;
//             while(!prune && childIndex < root.getChildCount()){
//                 rootData.setAt2(0);
//                 DefaultTreeModel childTree = new DefaultTreeModel(root.getChildAt(childIndex));
//                 int childWeight = miniMax(childTree, !isMax);
//                 if((isMax && childWeight > 0) || (!isMax && childWeight < 0)){
//                     rootData.setAt2(childWeight);
//                     prune = true;
//                     root.removeAllChildren();
//                     root.add((DefaultMutableTreeNode)childTree.getRoot());
//                 }
//             }
//             return rootData.getValue2();
//         }
//     }

//     /**
//      * This method will determine the best play that the AI can make with the current state of the board.
//      * Returns the best pair of x and y values for the AI player to play.
//      * @param gameState The current state  of the game.
//      * @return the best pair of x and y values for the AI player to play.
//      */
//     public Pair<Integer, Integer> generateMove(GameState gameState){
        
//         int bestVal = Integer.MIN_VALUE; //

//         int x = 0;
//         int y = 0;

//         for(int i = 0; i < 3; i ++){
//             for(int j = 0; j < 3; j ++){
                
//                 //checks if the cell is empty.
//                 if(gameState.getCell(i, j) == null){

//                     //making a move.
//                     gameState.getCell(i, j) = ;

//                     //evaluting the played move.
//                     int moveVal = miniMax(board, 0, false, gameState);

//                     //Undoing move.
//                     board[i][j] = null;

//                     // If the value of the current move is
//                     // more than the best value, then update
//                     // best.
//                     if (moveVal > bestVal)
//                     {
//                         x=i;
//                         y=j;

//                         bestVal = moveVal;
//                     }
//                 }
//             }
//         }  

//         Pair<Integer, Integer> bestMove = new Pair<Integer,Integer>(x, y); //The xy pair for the best cell placement for the Ai.
//         return bestMove;
//     }
// }
