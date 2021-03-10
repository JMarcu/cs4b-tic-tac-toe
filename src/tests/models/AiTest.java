package tests.models;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Stream;
import models.Ai;
import models.GameMode;
import models.GameState;
import models.Player;
import org.javatuples.Pair;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class AiTest {

    static Stream<int[][]> generateMove() {
        //Win the game
        //Prevent a loss
        //Choose to win over preventing a loss
        
        final int[][][] arr = new int[][][] {
            //Win the game when unthreatened
            new int[][] {new int[] {0, 0}, new int[] {1, 1}, new int[] {0, 1}, new int[] {1, 2}, new int[] {2, 1}, new int[] {0, 2}},

            //Prevent the opponent from winning
            new int[][] {new int[] {1, 1}, new int[] {0, 0}, new int[] {1, 2}, new int[] {1, 0}, new int[] {2, 0}, new int[] {0, 2}},

            //Choose to win over preventing the opponent from winning
            new int[][] {new int[] {1, 2}, new int[] {0, 2}, new int[] {2, 0}, new int[] {0, 0}, new int[] {1, 1}, new int[] {0, 1}},
        };
        return Stream.of(arr);
    }

    @ParameterizedTest
    @MethodSource
    void generateMove(int[][] moves){
        Ai ai = new Ai();
        GameState gameState = new GameState();
        
        for(int i = 0; i < moves.length - 1; i++){
            gameState.setCell(moves[i][0], moves[i][1]);
        }

        Pair<Integer, Integer> aiMove = ai.generateMove(gameState);
        int[] correctMove = moves[moves.length - 1];
        assertEquals(aiMove.getValue0(), correctMove[0]);
        assertEquals(aiMove.getValue1(), correctMove[1]);
    }

    @RepeatedTest(25)
    void generateMoveRandomized(){
        Ai ai = new Ai();
        Player playerOne = new Player();
        GameState gameState = new GameState(
            GameMode.FREE_PLAY, 
            new Pair<Player, Player>(playerOne, ai),
            true
        );
        Random random = new Random();

        ArrayList<Pair<Integer, Integer>> emptyCells = new ArrayList<Pair<Integer, Integer>>();
        for(int i = 0; i < gameState.getGridSize(); i++){
            for(int j = 0; j < gameState.getGridSize(); j++){
                emptyCells.add(new Pair<Integer, Integer>(i, j));
            }
        }

        do{
            if(gameState.getCurrentPlayer().equals(ai)){
                Pair<Integer, Integer> aiMove = ai.generateMove(gameState);
                emptyCells.remove(aiMove);
                gameState.setCell(aiMove.getValue0(), aiMove.getValue1());
            } else{
                int playerMoveIndex = random.nextInt(emptyCells.size());
                Pair<Integer, Integer> playerMove = emptyCells.get(playerMoveIndex);
                emptyCells.remove(playerMoveIndex);
                gameState.setCell(playerMove.getValue0(), playerMove.getValue1());
            } 
        }while(gameState.getStatus() == GameState.Status.IN_PROGRESS);
        System.out.println("status: " + gameState.getStatus());
        if(gameState.getWinner() != null){
            assertEquals(gameState.getWinner(), ai);
        } else{
            assertEquals(gameState.getStatus(), GameState.Status.DRAW);
        }

        assertNotEquals(gameState.getWinner(), playerOne);
    }
}
