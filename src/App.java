import controllers.CreateLobby;
import controllers.GameBoard;
import controllers.MainMenu;
import controllers.OptionsController;
import controllers.ScoreBoard;
import controllers.ShapeColorController;
import controllers.SplashScreen;
import controllers.SplashScreen.SplashType;
import controllers.GameHistoryController;
import interfaces.CallBackable;
import controllers.JoinLobby;
import controllers.Login;
import controllers.Register;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.function.Consumer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Vector;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import models.Game;
import models.GameForTable;
import models.Ai;
import models.GameState;
import models.MarkerShape;
import models.Player;
import models.SceneCallback.LaunchCreateLobbyCallback;
import models.SceneCallback.LaunchGameCallback;
import models.SceneCallback.LaunchMainMenuCallback;
import models.SceneCallback.LaunchOptionsMenuCallback;
import models.SceneCallback.LaunchScoreBoardCallback;
import models.SceneCallback.LaunchShapePickerCallback;
import models.SceneCallback.LaunchLobbyFinderCallback;
import models.SceneCallback.LaunchLoginCallback;
import models.SceneCallback.LaunchRegisterCallback;
import models.SceneCallback.ReturnToCallback;
import models.SceneCallback.LaunchGameHistoryCallback;
import services.AuthService;
import services.GameStateService;
import services.LobbyService;
import models.TTTScene;
import models.MusicPlayer.Track;
import models.MusicPlayer;

public class App extends Application implements LaunchGameCallback, LaunchMainMenuCallback, LaunchOptionsMenuCallback,
        LaunchShapePickerCallback, LaunchGameHistoryCallback, LaunchLobbyFinderCallback, LaunchLoginCallback, 
        LaunchRegisterCallback, LaunchCreateLobbyCallback {

    private FXMLLoader   createLobbyFXML;
    private Subscription gameStateSubscription;
    private Subscription gameStatePatchSubscription;
    private FXMLLoader   gameBoardFXML;
    private FXMLLoader   joinLobbyFXML;
    private FXMLLoader   loginFXML;
    private FXMLLoader   mainMenuFXML;
    private FXMLLoader   markerPickerFXML;
    private MusicPlayer  music;
    private boolean      online;
    private FXMLLoader   optionsMenuFXML;
    private Player       playerOne;
    private Player       playerTwo;
    private FXMLLoader   registerFXML;
    private StackPane    rootPane;
    //private FXMLLoader   scoreboardFXML;
    private FXMLLoader   gameHistoryFXML;  
    private FXMLLoader   splashScreenFXML;
    private final long FADE_DURATION = 200;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            AuthService.getInstance().start();
            LobbyService.getInstance().start();

            AuthService.getInstance().setInjectPlayerCB(new Consumer<Player>(){
                @Override
                public void accept(Player player) {
                    Platform.runLater(new Runnable(){
                        @Override
                        public void run() {
                            App.this.playerOne.setMarker(player.getShape(), player.getColor());
                            App.this.playerOne.setName(player.getName());
                        }
                    });
                }
            });

            music = new MusicPlayer();            
            playerOne = new Player(Color.BLACK, UUID.randomUUID(), "Player 1", MarkerShape.X);
            playerTwo = new Ai(Color.BLACK, "Player 2", MarkerShape.O);
            rootPane = new StackPane();

            Font.loadFont(App.class.getResource("/assets/fonts/Pixeboy.ttf").toExternalForm(), 10);

            createLobbyFXML = new FXMLLoader(getClass().getResource("/views/CreateLobby.fxml"));
            gameBoardFXML = new FXMLLoader(getClass().getResource("/views/game-board.fxml"));
            joinLobbyFXML = new FXMLLoader(getClass().getResource("/views/join-lobby.fxml"));
            loginFXML = new FXMLLoader(getClass().getResource("/views/Login.fxml"));
            mainMenuFXML = new FXMLLoader(getClass().getResource("/views/main-menu.fxml"));
            markerPickerFXML = new FXMLLoader(getClass().getResource("/views/ShapeColorPicker.fxml"));
            optionsMenuFXML = new FXMLLoader(getClass().getResource("/views/OptionsMenu.fxml"));
            registerFXML = new FXMLLoader(getClass().getResource("/views/Register.fxml"));
            //scoreboardFXML = new FXMLLoader(getClass().getResource("/views/Scoreboard.fxml"));
            gameHistoryFXML = new FXMLLoader(getClass().getResource("/views/GameHistoryTable.fxml"));
            splashScreenFXML = new FXMLLoader(getClass().getResource("/views/SplashScreen.fxml"));

            createLobbyFXML.load();
            gameBoardFXML.load();
            joinLobbyFXML.load();
            loginFXML.load();
            mainMenuFXML.load();
            markerPickerFXML.load();
            optionsMenuFXML.load();
            registerFXML.load();
            //scoreboardFXML.load();
            gameHistoryFXML.load();
            splashScreenFXML.load();

            primaryStage.setTitle("Tic Tac Toe");
            primaryStage.setScene(new Scene(rootPane));

            GameStateService.getInstance().subscribe(new Subscriber<GameState>(){
                @Override public void onSubscribe(Subscription subscription) { 
                    gameStateSubscription = subscription;
                    gameStateSubscription.request(1);
                }
                @Override public void onNext(GameState gameState) { 
                    if(gameState != null){
                        subscribeToGameState(gameState);
                    }
                    gameStateSubscription.request(1);
                };
                @Override public void onError(Throwable throwable) { }
                @Override public void onComplete() { }
            });

            launchMainMenu();

            SplashScreen splashScreen = splashScreenFXML.getController();
            splashScreen.setSplashType(SplashType.TITLE);
            splashScreen.setCloseCallback(new CallBackable(){
                @Override
                public void callback() {
                    launchLogin();
                }
            });
            openMenu(splashScreen.getRoot());

            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void launchCreateLobby(){
        try{
            MusicPlayer musicSFX = new MusicPlayer();
            musicSFX.playSFX(MusicPlayer.Track.openMenu);

            CreateLobby createLobby = createLobbyFXML.getController();

            createLobby.setReturnToCB(new ReturnToCallback(){
                @Override
                public void returnTo() {launchLobbyFinder();}
            });

            createLobby.setLaunchGameCB(this);
            createLobby.setLaunchOptionsMenuCB(this);
            createLobby.setLaunchShapePickerCB(this);
            createLobby.setPlayer(this.playerOne);

            launchScene(createLobbyFXML.getRoot());
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void launchGame() {
        try {
            GameState gameState = GameStateService.getInstance().getGameState();
            music.playMusic(Track.waiting);
            playerOne = gameState.getPlayers().getValue0();
            playerTwo = gameState.getPlayers().getValue1();

            GameBoard gameBoard = gameBoardFXML.getController();
            gameBoard.setShapePickerCB(this);
            gameBoard.setOptionsMenuCB(this);
            //gameBoard.setScoreBoardCB(this);
            gameBoard.setGameHistoryCB(this);
            
            launchScene(gameBoard.getRoot());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void launchLogin(){
        try{
            Login login = loginFXML.getController();
            login.setInjectOnlineCB(new Consumer<Boolean>(){
                @Override
                public void accept(Boolean online) {
                    App.this.online = online;
                }
            });
            login.setInjectPlayerCB(new Consumer<Player>(){
                @Override
                public void accept(Player player) {
                    App.this.playerOne = player;
                }
            });
            login.setLaunchLobbyFinderCB(this);
            login.setLaunchMainMenuCB(this);
            login.setLaunchRegisterCallback(this);
            launchScene(loginFXML.getRoot());
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
    @Override
    public void launchLobbyFinder(){
        try{
            MusicPlayer musicSFX = new MusicPlayer();
            musicSFX.playSFX(MusicPlayer.Track.openMenu);

            JoinLobby joinLobby = joinLobbyFXML.getController();

            joinLobby.setReturnToCB(new ReturnToCallback(){
                @Override
                public void returnTo() {launchMainMenu();}
            });

            joinLobby.setLaunchCreateLobbyCB(this);
            joinLobby.setLaunchGameCB(this);
            joinLobby.setLaunchMainMenuCB(this);
            joinLobby.setLaunchOptionsMenuCB(this);
            joinLobby.setGameHistoryCB(this);
            joinLobby.loadLobbies();

            launchScene(joinLobby.getRoot());
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void launchMainMenu() {
        music.playMusic(Track.title);

        MainMenu mainMenu = mainMenuFXML.getController();
        mainMenu.setLaunchGameCB(this);
        mainMenu.setLaunchLobbyFinderCB(this);
        mainMenu.setOnline(online);
        mainMenu.setOptionsMenuCB(this);
        mainMenu.setPlayers(playerOne, playerTwo);
        mainMenu.setShapePickerCB(this);

        if(online){
            try {
                LobbyService.getInstance().leaveLobby();
                this.playerOne = AuthService.getInstance().getPlayer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(rootPane.getChildren().size()  > 0){
            launchScene(mainMenu.getRoot());
        } else{
            rootPane.getChildren().add(mainMenu.getRoot());
        }
    }

    @Override
    public void launchOptionsMenu(String caller) {
        try{
            if (music.getShouldPlaySFX()){
                MusicPlayer music2 = new MusicPlayer();
                music2.playSFX(MusicPlayer.Track.openMenu);
            }

            OptionsController optionsMenu = optionsMenuFXML.getController();
            optionsMenu.acceptCaller(caller, music);
            optionsMenu.acceptOnline(online);
            optionsMenu.acceptPlayer(this.playerOne);
            optionsMenu.setLoginCB(this);
            optionsMenu.setMainMenuCB(this);
            optionsMenu.setReturnToCB(() -> {closeMenu(optionsMenu.getRoot());});
            openMenu(optionsMenu.getRoot());
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void launchRegister(){
        try{
            Register register = registerFXML.getController();
            register.setReturnToCB(new ReturnToCallback(){
                @Override
                public void returnTo() {launchLogin();}
            });
            register.setInjectOnlineCB(new Consumer<Boolean>(){
                @Override
                public void accept(Boolean online){
                    App.this.online = online;
                }
            });
            register.setInjectPlayerCB(new Consumer<Player>(){
                @Override
                public void accept(Player player) {
                    App.this.playerOne = player;
                }
            });
            register.setLaunchLobbyFinderCB(this);
            System.out.println("Launch Register");
            launchScene(registerFXML.getRoot());
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void launchGameHistory(List<Game> gameHistory){
        try{

            //test code
            

            //end test code
            List<GameForTable> gamesForTable = new ArrayList<GameForTable>();

            for (int i = 0; i < gameHistory.size(); i++){
                gamesForTable.add(new GameForTable(gameHistory.get(i)));
            }



            //ScoreBoard scoreboard = scoreboardFXML.getController();
            GameHistoryController gameHistoryTable = gameHistoryFXML.getController();
            if(gameHistory.size()==0) {
                gameHistoryTable.set();
                //GameState none = new GameState();
                //scoreboard.addPlayer(none);
            }
            //for(int i=0; i<gameHistory.size(); i++)
            //    scoreboard.addPlayer(gameHistory.get(i));
            //scoreboard.setReturnCB(() -> {closeMenu(scoreboard.getRoot());});
            //openMenu(scoreboard.getRoot());
            //for(int i=0; i<gameHistory.size(); i++)
            //    gameHistoryTable.addGame(gameHistory.get(i));

            gameHistoryTable.addGames(gamesForTable);
            gameHistoryTable.setReturnCB(() -> {closeMenu(gameHistoryTable.getRoot());});
            openMenu(gameHistoryTable.getRoot());

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void launchShapePicker(Player player) {
        try{
            if (music.getShouldPlaySFX()){
                MusicPlayer music2 = new MusicPlayer();
                music2.playSFX(MusicPlayer.Track.openMenu);
            }

            ShapeColorController markerMenu = markerPickerFXML.getController();
            markerMenu.acceptPlayer(player, music);
            markerMenu.setReturnCB(() -> {closeMenu(markerMenu.getRoot());});
            openMenu(markerMenu.getRoot());
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void closeMenu(Node menuRoot){
        final FadeTransition transition = new FadeTransition(Duration.millis(FADE_DURATION));
        transition.setNode(menuRoot);
        transition.setFromValue(1);
        transition.setToValue(0);
        transition.setOnFinished(onFinished -> {
            rootPane.getChildren().remove(menuRoot);
        });
        transition.play();
    }

    private void launchScene(Node sceneRoot){
        rootPane.getChildren().add(sceneRoot);
        final FadeTransition transition = new FadeTransition(Duration.millis(FADE_DURATION));
        transition.setNode(sceneRoot);
        transition.setFromValue(0);
        transition.setToValue(1);
        transition.setOnFinished(onFinished -> {
            while(rootPane.getChildren().size() > 1){
                rootPane.getChildren().remove(0);
            }
        });
        transition.play();
    }

    private void openMenu(Node menuRoot){
        rootPane.getChildren().add(menuRoot);
        final FadeTransition transition = new FadeTransition(Duration.millis(FADE_DURATION));
        transition.setNode(menuRoot);
        transition.setFromValue(0);
        transition.setToValue(1);
        transition.play();
    }

    private void onGameStatePatch(GameState.Patch patch){
        try{
            if(patch.getStatus() != null){
                SplashScreen splashScreen = splashScreenFXML.getController();
                splashScreen.setCloseCallback(null);
                splashScreen.setPlayAgainCallback(new CallBackable(){
                    @Override
                    public void callback() {
                        if(GameStateService.getInstance().getGameState().getOnline()){
                            try {
                                LobbyService.getInstance().playAgain();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else{
                            GameStateService.getInstance().setGameState(
                                new GameState(
                                    GameStateService.getInstance().getGameState().getGameMode(), 
                                    GameStateService.getInstance().getGameState().getPlayers(),
                                    GameStateService.getInstance().getGameState().getSinglePlayer(),
                                    GameStateService.getInstance().getGameState().getSecondaryOption(),
                                    false
                                )
                            );
                        }
                        closeMenu(splashScreen.getRoot());
                    }
                });
                splashScreen.setReturnToMenuCallback(new CallBackable(){
                    @Override
                    public void callback() {
                        if(GameStateService.getInstance().getGameState().getOnline()){
                            try {
                                LobbyService.getInstance().leaveLobby();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        launchMainMenu();
                    }
                });

                if(patch.getStatus() == GameState.Status.DRAW){
                    if (music.getShouldPlaySFX()){
                        music.playSFX(Track.tie);
                    }
                    splashScreen.setSplashType(SplashType.DRAW);
                    Platform.runLater(new Runnable(){
                        @Override
                        public void run() {
                            openMenu(splashScreen.getRoot());
                        }
                    });
                } else if(patch.getWinner() != null){
                    GameState gameState = GameStateService.getInstance().getGameState();
                    Player player;
                    if(gameState.getOnline()){
                        Player self = AuthService.getInstance().getPlayer();
                        if(
                            gameState.getPlayers().getValue0().getUuid().equals(self.getUuid()) ||
                            gameState.getPlayers().getValue1().getUuid().equals(self.getUuid())
                        ){
                            player = self;
                        } else{
                            player = patch.getWinner();
                        }
                    } else if(gameState.getSinglePlayer()){
                        player = gameState.getPlayers().getValue0();
                    } else{
                        final UUID playerOneId = gameState.getPlayers().getValue0().getUuid();
                        player = playerOneId.equals(patch.getWinner().getUuid())
                            ? gameState.getPlayers().getValue0()
                            : gameState.getPlayers().getValue1();
                    }
                    splashScreen.setPlayer(player);

                    if(player.getUuid().equals(patch.getWinner().getUuid())){
                        music.playMusic(Track.win);
                        splashScreen.setSplashType(SplashType.WIN);
                    } else{
                        if (music.getShouldPlaySFX()){
                            music.playSFX(Track.lose);
                        }
                        splashScreen.setSplashType(SplashType.LOSE);
                    }
                    Platform.runLater(new Runnable(){
                        @Override
                        public void run() {
                            openMenu(splashScreen.getRoot());
                        }
                    });
                }
            }
        } catch(Exception ex){
            System.err.println("Error during onGameStatePatch (App.java)");
            ex.printStackTrace();
        }
        gameStatePatchSubscription.request(1);
    }
    
    private void subscribeToGameState(GameState gameState){
        if(gameStatePatchSubscription != null){
            gameStatePatchSubscription.cancel();
        }
        gameState.subscribe(new Subscriber<GameState.Patch>(){
            @Override public void onSubscribe(Subscription subscription) { 
                gameStatePatchSubscription = subscription; 
                subscription.request(1);
            }
            @Override public void onNext(GameState.Patch item) { onGameStatePatch(item); };
            @Override public void onError(Throwable throwable) { }
            @Override public void onComplete() { }
        });
    }

}