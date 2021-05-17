package controllers;

import javafx.scene.control.TextField;

import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import models.Player;
import models.SceneCallback.InjectPlayerCallback;
import models.SceneCallback.LaunchLoginCallback;
import models.SceneCallback.LaunchMainMenuCallback;
import models.SceneCallback.LaunchRegisterCallback;
import models.SceneCallback.ReturnToCallback;
import services.AuthService;
import services.AuthService.RegistrationResult;

public class Login {

    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private Button loginBtn;
    @FXML private Button registerBtn;
    @FXML private Button returnBtn;
    @FXML private Pane root;

    private InjectPlayerCallback injectPlayerCB;
    private LaunchLoginCallback launchLoginCB;
    private LaunchRegisterCallback launchRegisterCB;
    private LaunchMainMenuCallback launchMainMenuCB;
    private ReturnToCallback returnToCB;

    @FXML void initialize(){
        root.getStylesheets().add(getClass().getResource("/styles/color-theme.css").toExternalForm());
        root.getStylesheets().add(getClass().getResource("/styles/login.css").toExternalForm());
        
        loginBtn.disableProperty().bind(usernameField.textProperty().isEmpty().or(passwordField.textProperty().isEmpty()));
    }

    public void setInjectPlayerCB(InjectPlayerCallback injectPlayerCB){
        this.injectPlayerCB = injectPlayerCB;
    }

    public void setLaunchRegisterCB(LaunchRegisterCallback launchRegisterCB){
        this.launchRegisterCB = launchRegisterCB;
    }

    public void setLaunchMainMenuCB(LaunchMainMenuCallback launchMainMenuCB){
        this.launchMainMenuCB = launchMainMenuCB;
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
                        System.out.println("Inside Consumer: " + player);
                        Platform.runLater(new Runnable(){
                            @Override
                            public void run() {
                                System.out.println("Inside Runnable");
                                if(player != null){
                                    Login.this.injectPlayerCB.injectPlayer(player);
                                    Login.this.launchMainMenuCB.launchMainMenu();
                                }
                            }
                        });
                    }
                } 

            );
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    @FXML protected void onRegisterClicked(ActionEvent e){
        launchRegisterCB.launchRegister();
    }

    public Pane getRoot(){ return this.root; }

    public void setLaunchLoginCallback(LaunchLoginCallback launchLoginCB){ this.launchLoginCB = launchLoginCB;}

    public void setReturnToCB(ReturnToCallback returnToCB){
        this.returnToCB = returnToCB;
    }
}
