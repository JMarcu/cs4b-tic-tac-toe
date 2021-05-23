package services;

import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;

import models.GameState;

public class GameStateService implements Publisher<GameState>{
    private GameState gameState;
    private transient SubmissionPublisher<GameState> publisher;
    private static GameStateService instance;

    private GameStateService(){
        gameState = null;
        publisher = new SubmissionPublisher<GameState>();
    }

    public static GameStateService getInstance(){
        if(instance == null){
            instance = new GameStateService();
        }
        return instance;
    }

    public GameState getGameState(){
        return this.gameState;
    }

    public void setGameState(GameState gameState) {
        System.out.println("================ SET GAME STATE ================");
        this.gameState = gameState;
        this.publisher.offer(gameState, null);
    }    
    
    @Override
    public void subscribe(Subscriber<? super GameState> subscriber) {
        this.publisher.subscribe(subscriber);
    }
}
