package tests.models;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.stream.Stream;

import models.GameMode;
import models.GameState;
import models.GameState.Patch;
import models.Player;
import org.javatuples.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

public class GameStateTest {
    @ParameterizedTest
    @EnumSource(GameMode.class)
    void getGameMode(GameMode gameMode) {
        GameState gameState = new GameState(
            gameMode,
            new Pair<Player, Player>(new Player(), new Player()),
            false
        );
        assertEquals(gameState.getGameMode(), gameMode);
    }

    @Test
    void getPlayers() {
        Player playerOne = new Player();
        Player playerTwo = new Player();
        GameState gameState = new GameState(
            GameMode.FREE_PLAY,
            new Pair<Player, Player>(playerOne, playerTwo),
            false
        );
        Pair<Player, Player> players = gameState.getPlayers();
        assertEquals(players.getValue0(), playerOne);
        assertEquals(players.getValue1(), playerTwo);
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void getSinglePlayer(boolean isSinglePlayer) {
        GameState gameState = new GameState(
            GameMode.FREE_PLAY,
            new Pair<Player, Player>(new Player(), new Player()),
            isSinglePlayer
        );
        assertEquals(gameState.getSinglePlayer(), isSinglePlayer);
    }

    @ParameterizedTest
    @ValueSource(ints = { -1, 0, 1})
    void getSecondaryOption(int secondaryOption) {
        GameState gameState = new GameState(
            GameMode.FREE_PLAY,
            new Pair<Player, Player>(new Player(), new Player()),
            false,
            secondaryOption
        );
        assertEquals(gameState.getSecondaryOption(), secondaryOption);
    }

    @ParameterizedTest
    @CsvSource({
        "-1,  0", "0,  -1", "-1, -1",
        "3,   0", "0,   3", "3,   3"
    })
    void throwsOobCellCoords(int x, int y) {
        GameState gameState = new GameState();

        assertThrows(IllegalArgumentException.class, () -> {
            gameState.setCell(x, y);
        });
    }

    @ParameterizedTest
    @CsvSource({
        "0, 0, 0, 1", 
        "0, 2, 1, 0", 
        "1, 1, 1, 2",
        "2, 0, 2, 1", 
        "0, 0, 2, 2"
    })
    void getSetCells(int x1, int y1, int x2, int y2){
        GameState gameState = new GameState();

        assertDoesNotThrow(() -> {
            gameState.setCell(x1, y1);
        });

        assertEquals(
            gameState.getCell(x1, y1), 
            gameState.getPlayers().getValue0()
        );

        assertDoesNotThrow(() -> {
            gameState.setCell(x2, y2);
        });

        assertEquals(
            gameState.getCell(x2, y2), 
            gameState.getPlayers().getValue1()
        );
    }

    @Test
    void throwsAlreadyClaimedCell(){
        GameState gameState = new GameState();
        gameState.setCell(0, 0);
        assertThrows(IllegalArgumentException.class, () -> {
            gameState.setCell(0, 0);
        });
    }

    @Test
    void getCurrentPlayer(){
        Player playerOne = new Player();
        Player playerTwo = new Player();
        GameState gameState = new GameState(
            GameMode.FREE_PLAY,
            new Pair<Player, Player>(playerOne, playerTwo),
            false
        );

        assertEquals(gameState.getCurrentPlayer(), playerOne);
        
        gameState.setCell(0, 0);
        assertEquals(gameState.getCurrentPlayer(), playerTwo);

        gameState.setCell(1, 1);
        assertEquals(gameState.getCurrentPlayer(), playerOne);
    }

    static Stream<int[][]> detectVictory() {
        final int[][][] arr = new int[][][] {
            new int[][] {new int[] {2, 2}, new int[] {0, 0}, new int[] {1, 0}, new int[] {0, 1}, new int[] {1, 1}, new int[] {0, 2}},
            new int[][] {new int[] {2, 2}, new int[] {1, 0}, new int[] {0, 0}, new int[] {1, 1}, new int[] {0, 1}, new int[] {1, 2}},
            new int[][] {new int[] {1, 1}, new int[] {2, 0}, new int[] {0, 0}, new int[] {2, 1}, new int[] {0, 1}, new int[] {2, 2}},
            new int[][] {new int[] {2, 2}, new int[] {0, 0}, new int[] {0, 1}, new int[] {1, 0}, new int[] {0, 2}, new int[] {2, 0}},
            new int[][] {new int[] {2, 2}, new int[] {0, 1}, new int[] {0, 0}, new int[] {1, 1}, new int[] {0, 2}, new int[] {2, 1}},
            new int[][] {new int[] {1, 1}, new int[] {0, 2}, new int[] {0, 0}, new int[] {1, 2}, new int[] {0, 1}, new int[] {2, 2}},
            new int[][] {new int[] {1, 0}, new int[] {0, 0}, new int[] {0, 1}, new int[] {1, 1}, new int[] {0, 2}, new int[] {2, 2}},
            new int[][] {new int[] {0, 1}, new int[] {0, 2}, new int[] {1, 0}, new int[] {1, 1}, new int[] {1, 2}, new int[] {2, 0}}
        };
        return Stream.of(arr);
    }

    @ParameterizedTest
    @MethodSource
    void detectVictory(int[][] moves){
        Player playerOne = new Player();
        Player playerTwo = new Player();
        GameState playerOneVictory = new GameState(
            GameMode.FREE_PLAY,
            new Pair<Player, Player>(playerOne, playerTwo),
            false
        );

        //The first move in the array is a garbage move used to offset the winner.
        //We ignore it here to let player one win.
        for(int i = 1; i < moves.length; i++){
            playerOneVictory.setCell(moves[i][0], moves[i][1]);
        }
        assertEquals(playerOneVictory.getStatus(), GameState.Status.WON);
        assertEquals(playerOneVictory.getWinner(), playerOne);

        GameState playerTwoVictory = new GameState(
            GameMode.FREE_PLAY,
            new Pair<Player, Player>(playerOne, playerTwo),
            false
        );

        //In this game, player one does use the first move in the array.
        //This offsets who plays which move and results in a player two victory.
        playerTwoVictory.setCell(moves[0][0], moves[0][1]);
        for(int i = 1; i < moves.length; i++){
            playerTwoVictory.setCell(moves[i][0], moves[i][1]);
        }
        assertEquals(playerTwoVictory.getStatus(), GameState.Status.WON);
        assertEquals(playerTwoVictory.getWinner(), playerTwo);
    }

    @Test
    void detectDraw(){
        GameState gameState = new GameState();
        gameState.setCell(0, 0);
        gameState.setCell(0, 1);
        gameState.setCell(0, 2);
        gameState.setCell(2, 0);
        gameState.setCell(2, 1);
        gameState.setCell(2, 2);
        gameState.setCell(1, 1);
        gameState.setCell(1, 0);
        gameState.setCell(1, 2);
        assertEquals(gameState.getStatus(), GameState.Status.DRAW);
        assertNull(gameState.getWinner());
    }

    @Test
    void subscribe(){
        Player playerOne = new Player();
        Player playerTwo = new Player();
        GameState gameState = new GameState(
            GameMode.FREE_PLAY,
            new Pair<Player, Player>(playerOne, playerTwo),
            false
        );
        gameState.subscribe(new Subscriber<GameState.Patch>(){
            int assertCase = 0;
            
            @Override public void onSubscribe(Subscription subscription) { }
            @Override public void onComplete() { }
            @Override public void onError(Throwable throwable) { 
                fail("Received an onError invocation from the publisher: " + throwable.getMessage());
            }
            @Override
            public void onNext(Patch item) {
                switch(assertCase){
                    case 0:
                        assertEquals(item.getCurrentPlayer(), playerTwo);
                        assertEquals(item.getMove().getValue0(), playerOne);
                        assertEquals(item.getMove().getValue1(), 0);
                        assertEquals(item.getMove().getValue2(), 0);
                        assertNull(item.getStatus());
                        assertNull(item.getWinner());
                        assertCase++;
                        gameState.setCell(1, 0);
                        break;
                    case 1:
                        assertEquals(item.getCurrentPlayer(), playerOne);
                        assertEquals(item.getMove().getValue0(), playerTwo);
                        assertEquals(item.getMove().getValue1(), 1);
                        assertEquals(item.getMove().getValue2(), 0);
                        assertNull(item.getStatus());
                        assertNull(item.getWinner());
                        assertCase++;
                        gameState.setCell(0, 1);
                        break;
                    case 2:
                        assertEquals(item.getCurrentPlayer(), playerTwo);
                        assertEquals(item.getMove().getValue0(), playerOne);
                        assertEquals(item.getMove().getValue1(), 0);
                        assertEquals(item.getMove().getValue2(), 1);
                        assertNull(item.getStatus());
                        assertNull(item.getWinner());
                        assertCase++;
                        gameState.setCell(1, 1);
                        break;
                    case 3:
                        assertEquals(item.getCurrentPlayer(), playerOne);
                        assertEquals(item.getMove().getValue0(), playerTwo);
                        assertEquals(item.getMove().getValue1(), 1);
                        assertEquals(item.getMove().getValue2(), 1);
                        assertNull(item.getStatus());
                        assertNull(item.getWinner());
                        assertCase++;
                        gameState.setCell(0, 2);
                        break;
                    case 4:
                        assertNull(item.getCurrentPlayer());
                        assertEquals(item.getMove().getValue0(), playerOne);
                        assertEquals(item.getMove().getValue1(), 0);
                        assertEquals(item.getMove().getValue2(), 2);
                        assertEquals(item.getStatus(), GameState.Status.WON);
                        assertEquals(item.getWinner(), playerOne);
                        break;
                }
            }
        });
        gameState.setCell(0, 0);
    }
}
