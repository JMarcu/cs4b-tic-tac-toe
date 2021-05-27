package controllers;

import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import models.Player;
import models.SceneCallback.LaunchLobbyFinderCallback;
import models.SceneCallback.LaunchMainMenuCallback;
import models.SceneCallback.LaunchRegisterCallback;
import services.AuthService;

public class Login {

    @FXML private Label errorLabel;
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private Button loginBtn;
    @FXML private Button registerBtn;
    @FXML private Button returnBtn;
    @FXML private ScrollPane root;

    private Consumer<Boolean> injectOnlineCB;
    private Consumer<Player> injectPlayerCB;
    private LaunchLobbyFinderCallback launchLobbyFinderCB;
    private LaunchRegisterCallback launchRegisterCB;
    private LaunchMainMenuCallback launchMainMenuCB;

    @FXML void initialize(){
        root.getStylesheets().add(getClass().getResource("/styles/color-theme.css").toExternalForm());
        root.getStylesheets().add(getClass().getResource("/styles/login.css").toExternalForm());
        
        loginBtn.disableProperty().bind(usernameField.textProperty().isEmpty().or(passwordField.textProperty().isEmpty()));
        errorLabel.setTextFill(Color.RED);
        errorLabel.setVisible(false);
    }

    public ScrollPane getRoot(){ return this.root; }

    public void setInjectOnlineCB(Consumer<Boolean> injectOnlineCB){
        this.injectOnlineCB = injectOnlineCB;
    }

    public void setInjectPlayerCB(Consumer<Player> injectPlayerCB){
        this.injectPlayerCB = injectPlayerCB;
    }

    public void setLaunchLobbyFinderCB(LaunchLobbyFinderCallback launchLobbyFinderCB){
        this.launchLobbyFinderCB = launchLobbyFinderCB;
    }

    public void setLaunchMainMenuCB(LaunchMainMenuCallback launchMainMenuCB){
        this.launchMainMenuCB = launchMainMenuCB;
    }

    public void setLaunchRegisterCB(LaunchRegisterCallback launchRegisterCB){
        this.launchRegisterCB = launchRegisterCB;
    }

    public void setLaunchRegisterCallback(LaunchRegisterCallback launchRegisterCB){ 
        this.launchRegisterCB = launchRegisterCB;
    }

    @FXML protected void onLoginClicked(ActionEvent e){
        try {
            AuthService.getInstance().login(
                this.usernameField.getText(), 
                this.passwordField.getText(),
                new Consumer<Player>(){
                    @Override
                    public void accept(Player player) {
                        Platform.runLater(new Runnable(){
                            @Override
                            public void run() {
                                if(player != null){
                                    Login.this.injectOnlineCB.accept(true);
                                    Login.this.injectPlayerCB.accept(player);
                                    Login.this.launchLobbyFinderCB.launchLobbyFinder();
                                }
                                errorLabel.setVisible(true);
                            }
                        });
                    }
                } 

            );
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @FXML protected void onPlayOfflineAction(ActionEvent e){
        Login.this.injectOnlineCB.accept(false);
        Login.this.launchMainMenuCB.launchMainMenu();
    }

    @FXML protected void onRegisterClicked(ActionEvent e){
        launchRegisterCB.launchRegister();
    }

}
