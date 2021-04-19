package models.ServerMessage;

import models.GameState;

public class ConnectionSuccessMessageBody {
    private GameState gameState;

    public ConnectionSuccessMessageBody(){
        this(new GameState());
    }

    public ConnectionSuccessMessageBody(GameState gameState){
        this.gameState = gameState;
    }

    public GameState getGameState(){ return gameState; }
}
