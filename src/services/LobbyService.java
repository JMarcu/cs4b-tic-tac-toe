package services;

public class LobbyService {
    // public void setGameState(GameState gameState){
    //     this.gameState = gameState;

    //     if(this.gameStateSubscription != null){
    //         this.gameStateSubscription.cancel();
    //     }

    //     this.gameState.subscribe(new Subscriber<GameState.Patch>(){
	// 		@Override public void onSubscribe(Subscription subscription) { 
    //             gameStateSubscription = subscription; 
    //             gameStateSubscription.request(1);
    //         }
	// 		@Override public void onNext(GameState.Patch item) { 
    //             onGameStatePatch(item); 
    //             gameStateSubscription.request(1);
    //         }
	// 		@Override public void onError(Throwable throwable) { }
	// 		@Override public void onComplete() { }
    //     });
    // }

    // private void onGameStatePatch(GameState.Patch patch){
    //     if(patch.getMove() != null && !patch.getMove().getValue0().getUuid().equals(this.player.getUuid())){
    //         // try {
    //         //     this.out.writeObject(
    //         //         new Message(
    //         //             new MoveMessageBody(
    //         //                 patch.getMove().removeFrom0(), 
    //         //                 patch.getMove().getValue0().getUuid()
    //         //             ),
    //         //             MessageType.MOVE
    //         //         )
    //         //     );
    //         // } catch (IOException e) {
    //         //     e.printStackTrace();
    //         // }
    //     }
    // }
}
