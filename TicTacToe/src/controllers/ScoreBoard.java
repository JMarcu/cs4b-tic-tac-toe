package controllers;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
// import javafx.scene.control.Button;
// import javafx.scene.input.MouseEvent;
import models.Player;
import java.time.Duration;
import java.time.Instant;

public class ScoreBoard {

    private Player[] topPlayer = new Player[4];
    private char[] WDL = {' ',' ',' ',' '};
    private Duration[] topTime = new Duration[4];
    private String display;

    @FXML 
    private Text scoreDisplay;

    // @FXML 
    // private Button Reset = new Button("Reset");
    // @FXML 
    // private Button Return = new Button("Return");
    // @FXML 
    // protected void ResetButtonClicked(MouseEvent event) {
    //     //Reset the ScoreBoard
    // }
    // @FXML 
    // protected void ReturnButtonClicked(MouseEvent event) {
    //     //Return to the Main Menu
    // }

    public ScoreBoard(){
        display = String.format("\t\t\t\tScore Board\n\n1. %15s   %c   %10d seconds\n2. %15s   %c   %10d seconds\n3. %15s   %c   %10d seconds\n4. %15s   %c   %10f seconds\n", topPlayer[0].getName(), WDL[0], topTime[0], topPlayer[1].getName(), WDL[1], topTime[1], topPlayer[2].getName(), WDL[2], topTime[2], topPlayer[3].getName(), WDL[3], topTime[3]);
        scoreDisplay.setText(display);
        scoreDisplay.setX(50);
        scoreDisplay.setY(50);
            //set to center
    }
  
    public void addPlayer(Player topCandidate, char gameStat, Instant startTime, Instant endTime){
        Duration gameTime = Duration.between(startTime, endTime);
        
        for(int i=0; i<4; i++){
            if(WDL[i] == ' '){
                topPlayer[i] = topCandidate;
                WDL[i] = gameStat;
                topTime[i] = gameTime;
            }
            if((WDL[i] == 'W' && gameStat == 'W' && topTime[i].compareTo(gameTime)<0)
                || (WDL[i] == 'D' && gameStat == 'D' && topTime[i].compareTo(gameTime)<0)
                || (WDL[i] == 'L' && gameStat == 'L' && topTime[i].compareTo(gameTime)>0)
                || (WDL[i] == 'L' && (gameStat == 'D' || gameStat == 'W'))
                || (WDL[i] == 'D' && gameStat == 'W')
                ){
                for(int j=i+1; j<4; j++){
                    topPlayer[j] = topPlayer[j-1];
                    WDL[j] = WDL[j-1];
                    topTime[j] = topTime[j-1];
                }
                topPlayer[i] = topCandidate;
                WDL[i] = gameStat;
                topTime[i] = gameTime;
            }
        }
    }

}
