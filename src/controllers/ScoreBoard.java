package controllers;

import java.time.Duration;
import java.time.Instant;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import models.Player;
import models.SceneCallback.ReturnToCallback;

public class ScoreBoard {

    private String[] topPlayer = new String[4];
    private char[] WDL = {' ',' ',' ',' '};
    private long[] topTime = new long[4];
    private String display;

    @FXML 
    private Text scoreDisplay = new Text();
    private ReturnToCallback returnCB;
    private Button Return;

    @FXML
    private AnchorPane root;

    public AnchorPane getRoot(){ return this.root; }
    // private Button Reset;

    // @FXML 
    // protected void ResetButtonClicked(ActionEvent event) {
    //     //Reset the ScoreBoard
    // }
    @FXML 
    protected void ReturnButtonClicked(ActionEvent event) {
        returnCB.returnTo();
    }

    public ScoreBoard(){
        display = String.format("\t\t\tScore Board\n\n1.\t%-30s   %c   %5d : %02d\n2.\t%-30s   %c   %5d : %02d\n3.\t%-30s   %c   %5d : %02d\n4.\t%-30s   %c   %5d : %02d\n", topPlayer[0], WDL[0], topTime[0]/60, topTime[0]%60, topPlayer[1], WDL[1], topTime[1]/60, topTime[1]%60, topPlayer[2], WDL[2], topTime[2]/60, topTime[2]%60, topPlayer[3], WDL[3], topTime[3]/60, topTime[3]%60);
        scoreDisplay.setText(display);
        scoreDisplay.setX(50);
        scoreDisplay.setY(50);
            //set to center
    }
  
    public void addPlayer(Player topCandidate, char gameStat, Instant startTime, Instant endTime){
        Duration gt = Duration.between(startTime, endTime);
        long gameTime = gt.getSeconds();

        for(int i=0; i<4; i++){
            if(WDL[i] == ' '){
                topPlayer[i] = topCandidate.getName();
                WDL[i] = gameStat;
                topTime[i] = gameTime;
                display = String.format("\t\t\tScore Board\n\n1.\t%-30s   %c   %5d : %02d\n2.\t%-30s   %c   %5d : %02d\n3.\t%-30s   %c   %5d : %02d\n4.\t%-30s   %c   %5d : %02d\n", topPlayer[0], WDL[0], topTime[0]/60, topTime[0]%60, topPlayer[1], WDL[1], topTime[1]/60, topTime[1]%60, topPlayer[2], WDL[2], topTime[2]/60, topTime[2]%60, topPlayer[3], WDL[3], topTime[3]/60, topTime[3]%60);
                scoreDisplay.setText(display);
                break;
            }
            if((WDL[i] == 'W' && gameStat == 'W' && topTime[i] < gameTime)
                || (WDL[i] == 'D' && gameStat == 'D' && topTime[i] < gameTime)
                || (WDL[i] == 'L' && gameStat == 'L' && topTime[i] > gameTime)
                || (WDL[i] == 'L' && (gameStat == 'D' || gameStat == 'W'))
                || (WDL[i] == 'D' && gameStat == 'W')
                ){
                for(int j=i+1; j<4; j++){
                    topPlayer[j] = topPlayer[j-1];
                    WDL[j] = WDL[j-1];
                    topTime[j] = topTime[j-1];
                }
                topPlayer[i] = topCandidate.getName();
                WDL[i] = gameStat;
                topTime[i] = gameTime;
                display = String.format("\t\t\tScore Board\n\n1.\t%-30s   %c   %5d : %02d\n2.\t%-30s   %c   %5d : %02d\n3.\t%-30s   %c   %5d : %02d\n4.\t%-30s   %c   %5d : %02d\n", topPlayer[0], WDL[0], topTime[0]/60, topTime[0]%60, topPlayer[1], WDL[1], topTime[1]/60, topTime[1]%60, topPlayer[2], WDL[2], topTime[2]/60, topTime[2]%60, topPlayer[3], WDL[3], topTime[3]/60, topTime[3]%60);
                scoreDisplay.setText(display);
                break;
            }

        }
    }

    public void setReturnCB(ReturnToCallback returnCB){
        this.returnCB = returnCB;
    }
}
