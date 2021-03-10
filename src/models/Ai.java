package models;
import java.util.ArrayList;
import java.util.UUID;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import javafx.scene.paint.Color;
import org.javatuples.Pair;
import org.javatuples.Triplet;

public class Ai extends Player{

    /*==========================================================================================================
     * CLASS VARIABLES
     *==========================================================================================================*/

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
    @SuppressWarnings("unchecked")
    public Pair<Integer, Integer> generateMove(GameState gameState){
        System.out.println("============================== GENERATE MOVE ==============================");
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(new Triplet<Integer, Integer, Integer>(null, null, null));

        ArrayList<Pair<Integer, Integer>> emptyCells = new ArrayList<Pair<Integer, Integer>>();
        for(int i = 0; i < gameState.getGridSize(); i++){
            for(int j = 0; j < gameState.getGridSize(); j++){
                if(gameState.getCell(i, j) == null){
                    emptyCells.add(new Pair<Integer, Integer>(Integer.valueOf(i), Integer.valueOf(j)));
                }
            }
        }

        System.out.println("emptyCells: " + emptyCells);

        populateTree(root, emptyCells, gameState.getVictoryArr(), gameState.getGridSize(), false);
        System.out.println("done populating tree");

        miniMax(root, true);
        System.out.println("done miniMax");

        Triplet<Integer, Integer, Integer> bestMove = new Triplet<Integer, Integer, Integer>(null, null, 100);
        for(int i = 0; i < root.getChildCount(); i++){
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) root.getChildAt(i);
            Triplet<Integer, Integer, Integer> childData = (Triplet<Integer, Integer, Integer>) child.getUserObject();
            if(childData.getValue2() != null && childData.getValue2() < bestMove.getValue2()){
                bestMove = childData;
            }
            System.out.println("child " + i + ": " + child);
        }

        System.out.println("Best Move: " + bestMove.removeFrom2());
        
        return bestMove.removeFrom2();
    }
     
    /** */
    @SuppressWarnings("unchecked")
    private void miniMax(DefaultMutableTreeNode node, boolean isMax){
        isMax = (node.getPath().length % 2) == 0;

        if(!node.isLeaf()){
            int bestWeight = isMax ? -100 : 100;
            int childIndex = 0;
            boolean prune = false;

            do{
                DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(childIndex);
                miniMax(child, !isMax);
                int childWeight = ((Triplet<Integer, Integer, Integer>)child.getUserObject()).getValue2();

                if((isMax && childWeight > bestWeight) || (!isMax && childWeight < bestWeight)){
                    bestWeight = childWeight;
                }

                // if((isMax && bestWeight == 10) || (!isMax && bestWeight == -10)){
                //     prune = true;
                // } else{
                    childIndex++;
                // }
            } while(!prune && childIndex < node.getChildCount());


            Triplet<Integer, Integer, Integer> nodeData = (Triplet<Integer, Integer, Integer>)node.getUserObject();
            if(node.getPath().length == 2 && nodeData.getValue0() == 1 && nodeData.getValue1() == 0){
                System.out.println("isMax: " + isMax);
                System.out.println(((Triplet<Integer, Integer, Integer>)node.getUserObject()).setAt2(bestWeight));
                for(int i = 0; i < node.getChildCount(); i++){
                    System.out.println("    child: " + node.getChildAt(i));
                }
            }

            node.setUserObject(
                ((Triplet<Integer, Integer, Integer>)node.getUserObject()).setAt2(bestWeight)
            );
        }
    }

    /**
     * The methods creates a tree of all the permutations of the current board state 
     * and sets weight to the leaf nodes.
     * @param tree The tree that represents all of the permutations of the current board state.  
     * @param emptyCells An arraylist of empty cells from the board.
     * @param victoryArr And arraylist that holds number values that determine if a win condition has been met.
     */
    private void populateTree(DefaultMutableTreeNode node, ArrayList<Pair<Integer, Integer>> emptyCells, ArrayList<Integer> victoryArr, int gridSize, boolean isPlayerOne){
        int cellIndex = 0;
        boolean shortCircuit = false;

        Triplet<Integer, Integer, Integer> nodeData = (Triplet<Integer, Integer, Integer>)node.getUserObject();
        if(node.getPath().length == 2 && nodeData.getValue0() == 1 && nodeData.getValue1() == 0){
            System.out.println("Node: " + node);
        }

        while(cellIndex < emptyCells.size() && !shortCircuit){
            Pair<Integer, Integer> cell = emptyCells.get(cellIndex);
            if(node.getPath().length == 1){
                System.out.println("Cell: " + cell);
            }
            
            ArrayList<Integer> vArrClone = new ArrayList<Integer>();
            for(Integer i : victoryArr){
                vArrClone.add(Integer.valueOf(i.intValue()));
            }

            boolean isVictory = GameState.checkVictoryArr(vArrClone, cell, isPlayerOne);

            DefaultMutableTreeNode child = new DefaultMutableTreeNode();
            node.add(child);

            if(node.getPath().length == 2 && nodeData.getValue0() == 1 && nodeData.getValue1() == 0){
                System.out.println("isVictory: " + isVictory);
            }

            if(node.getPath().length == 1){
                System.out.println("Cell isVictory: " + isVictory);
            }

            if(isVictory || emptyCells.size() == 1){
                int weight = evaluate(vArrClone, cell, gridSize);
                child.setUserObject(cell.add(Integer.valueOf(weight)));
                shortCircuit = isVictory;

                // Triplet<Integer, Integer, Integer> data = (Triplet<Integer, Integer, Integer>)child.getUserObject();
                // if(data.getValue0() == 1 && data.getValue1() == 2){
                //     System.out.println("Is Leaf");
                //     for(TreeNode n : child.getPath()){
                //         System.out.print(((DefaultMutableTreeNode)n).getUserObject() + " ");
                //     }
                //     System.out.println();
                //     System.out.println(vArrClone);
                //     System.out.println(child.getUserObject());
                // }

            } else{
                ArrayList<Pair<Integer, Integer>> emptyCellsClone = new ArrayList<Pair<Integer, Integer>>();
                
                for(Pair<Integer, Integer> emptyCell : emptyCells){
                    if(emptyCell != cell){
                        emptyCellsClone.add(emptyCell);
                    }
                }
                child.setUserObject(cell.add((Integer)null));

                populateTree(child, emptyCellsClone, vArrClone, gridSize, !isPlayerOne);
            }

            if(node.getPath().length == 1){
                System.out.println("Cell child: " + child);
            }

            if(node.getPath().length == 2 && nodeData.getValue0() == 1 && nodeData.getValue1() == 0){
                System.out.println("node child: " + child);
            }


            cellIndex++;
        }
    }

    private int evaluate(ArrayList<Integer> victoryArr, Pair<Integer, Integer> move, int gridSize){
        int i = 0;
        boolean keepSearching = true;

        while(keepSearching && i < victoryArr.size()){
            if(Math.abs(victoryArr.get(i)) >= gridSize){
                keepSearching = false;
            } else{
                i++;
            }
        }
        
        if(i == victoryArr.size()){
            return 0;
        } else if(victoryArr.get(i) < 0){
            return -10;
        } else{
            return 10;
        }
    }
}
