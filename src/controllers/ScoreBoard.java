package controllers;

// import java.time.Duration;
// import java.time.Instant;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import models.GameState;
import models.Player;
import models.SceneCallback.ReturnToCallback;
import org.javatuples.Pair;


public class ScoreBoard {

    private int index;
    private String[] topPlayers = new String[5];
    private String[] topGames = new String[5];
    private Pair<Player, Player> players;
    private Player winner;
    //private long[] topTime = new long[5];
    private String display;

    @FXML 
    private Text scoreDisplay = new Text();
    private ReturnToCallback returnCB;

    @FXML
    private AnchorPane root;

    public AnchorPane getRoot(){ return this.root; }


    @FXML 
    protected void ReturnButtonClicked(ActionEvent event) {
        returnCB.returnTo();
    }

    public ScoreBoard(){
        display = String.format("1.\t%30s\t%s\n2.\t%30s\t%s\n3.\t%30s\t%s\n4.\t%30s\t%s\n5.\t%30s\t%s\n", topGames[0], topPlayers[0], topGames[1], topPlayers[1], topGames[2],topPlayers[2], topGames[3], topPlayers[3], topGames[4], topPlayers[4]); 
        scoreDisplay.setText(display);
        scoreDisplay.setX(50);
        scoreDisplay.setY(50);
    }
  
    public void addPlayer(GameState gameState){
        players = gameState.getPlayers();
        if(gameState.getStatus() == GameState.Status.DRAW){
            if(index >= 5){
                for(int j=4; j>0; j--){
                    topPlayers[j] = topPlayers[j-1];
                    topGames[j] = topGames[j-1];
                }
                topPlayers[0] = "Draw!";
                topGames[0] = players.getValue0().getName() + "  VS  " + players.getValue1().getName();
            }

            if(index < 5){
                for(int i=index; i>0; i--){
                    topPlayers[i] = topPlayers[i-1];
                    topGames[i] = topGames[i-1];
                }
                topPlayers[0] = "Draw!"; index++;
                topGames[0] = players.getValue0().getName() + "  VS  " + players.getValue1().getName();
            }
            display = String.format("1.\t%30s\t%s\n2.\t%30s\t%s\n3.\t%30s\t%s\n4.\t%30s\t%s\n5.\t%30s\t%s\n", topGames[0], topPlayers[0], topGames[1], topPlayers[1], topGames[2],topPlayers[2], topGames[3], topPlayers[3], topGames[4], topPlayers[4]); 
        }

        if(gameState.getStatus() == GameState.Status.WON){
            winner = gameState.getWinner();
            if(index >= 5){
                for(int j=4; j>0; j--){
                    topPlayers[j] = topPlayers[j-1];
                    topGames[j] = topGames[j-1];
                }
                topPlayers[0] = winner.getName()+"   Won!";
                topGames[0] = players.getValue0().getName() + "  VS  " + players.getValue1().getName();
            }

            if(index < 5){
                for(int i=index; i>0; i--){
                    topPlayers[i] = topPlayers[i-1];
                    topGames[i] = topGames[i-1];
                }
                topPlayers[0] = winner.getName()+"   Won!"; index++;
                topGames[0] = players.getValue0().getName() + "  VS  " + players.getValue1().getName();
            }
            display = String.format("1.\t%30s\t%s\n2.\t%30s\t%s\n3.\t%30s\t%s\n4.\t%30s\t%s\n5.\t%30s\t%s\n", topGames[0], topPlayers[0], topGames[1], topPlayers[1], topGames[2],topPlayers[2], topGames[3], topPlayers[3], topGames[4], topPlayers[4]); 
        }

        scoreDisplay.setText(display);
    }

    public void addPlayer(GameState.Patch gameState){
        players = gameState.getPlayers();
        if(gameState.getStatus() == GameState.Status.DRAW){
            if(index >= 5){
                for(int j=4; j>0; j--){
                    topPlayers[j] = topPlayers[j-1];
                    topGames[j] = topGames[j-1];
                }
                topPlayers[0] = "Draw!";
                topGames[0] = players.getValue0().getName() + "  VS  " + players.getValue1().getName();
            }

            if(index < 5){
                for(int i=index; i>0; i--){
                    topPlayers[i] = topPlayers[i-1];
                    topGames[i] = topGames[i-1];
                }
                topPlayers[0] = "Draw!"; index++;
                topGames[0] = players.getValue0().getName() + "  VS  " + players.getValue1().getName();
            }
            display = String.format("1.\t%30s\t%s\n2.\t%30s\t%s\n3.\t%30s\t%s\n4.\t%30s\t%s\n5.\t%30s\t%s\n", topGames[0], topPlayers[0], topGames[1], topPlayers[1], topGames[2],topPlayers[2], topGames[3], topPlayers[3], topGames[4], topPlayers[4]); 
        }

        if(gameState.getStatus() == GameState.Status.WON){
            winner = gameState.getWinner();
            if(index >= 5){
                for(int j=4; j>0; j--){
                    topPlayers[j] = topPlayers[j-1];
                    topGames[j] = topGames[j-1];
                }
                topPlayers[0] = winner.getName()+"   Won!";
                topGames[0] = players.getValue0().getName() + "  VS  " + players.getValue1().getName();
            }

            if(index < 5){
                for(int i=index; i>0; i--){
                    topPlayers[i] = topPlayers[i-1];
                    topGames[i] = topGames[i-1];
                }
                topPlayers[0] = winner.getName()+"   Won!"; index++;
                topGames[0] = players.getValue0().getName() + "  VS  " + players.getValue1().getName();
            }
            display = String.format("1.\t%30s\t%s\n2.\t%30s\t%s\n3.\t%30s\t%s\n4.\t%30s\t%s\n5.\t%30s\t%s\n", topGames[0], topPlayers[0], topGames[1], topPlayers[1], topGames[2],topPlayers[2], topGames[3], topPlayers[3], topGames[4], topPlayers[4]); 
        }

        scoreDisplay.setText(display);
    }

    public void setReturnCB(ReturnToCallback returnCB){
        this.returnCB = returnCB;
    }
}
