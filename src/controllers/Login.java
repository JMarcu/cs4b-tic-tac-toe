package controllers;

import models.Player;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import models.SceneCallback.LaunchLobbyCallback;
import models.SceneCallback.LaunchLoginCallback;
import models.SceneCallback.LaunchMainMenuCallback;
import models.SceneCallback.LaunchRegisterCallback;
import models.SceneCallback.ReturnToCallback;
import services.AuthService;

public class Login {

    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private Button loginBtn;
    @FXML private Button registerBtn;
    @FXML private Button returnBtn;
    @FXML private Pane root;

    private ReturnToCallback returnToCB;
    private LaunchLoginCallback launchLoginCB;
    private LaunchRegisterCallback launchRegisterCB;
    private LaunchMainMenuCallback launchMainMenuCB;

    @FXML void initialize(){
        root.getStylesheets().add(getClass().getResource("/styles/color-theme.css").toExternalForm());
        root.getStylesheets().add(getClass().getResource("/styles/login.css").toExternalForm());

        setLaunchLoginCallback(launchLoginCB);
        setLaunchMainMenuCB(launchMainMenuCB);
        setLaunchRegisterCB(launchRegisterCB);
        setReturnToCB(returnToCB);
    }

    // public void onLoginAction(){
    //     try {
    //         // AuthService.getInstance().login(
    //         //     "some username",
    //         //     "'some password'",
    //         //     new Consumer<Boolean>(){
    //         //         @Override
    //         //         public void accept(Boolean success) {
    //         //             MainMenu.this.onLoginResult(success);
    //         //         }
    //         //     }
    //         // );
            
            
    //         //Do something before the server responds to your attempt to log in.
    //         //For example, show a loading screen.
    //     } catch (Exception e) {
    //         // TODO Auto-generated catch block
    //         e.printStackTrace();
    //     }
    // }

    // private void onLoginResult(boolean success){
    //     if(success){
    //         //Do what you want to do if it works.
    //         System.out.println("Yay!");
    //     } else{
    //         //Dow what you want to do if it fails.
    //         System.out.println("Boo!");
    //     }
    // }

    public void setLaunchRegisterCB(LaunchRegisterCallback launchRegisterCB){
        this.launchRegisterCB = launchRegisterCB;
    }

    public void setLaunchMainMenuCB(LaunchMainMenuCallback launchMainMenuCB){
        this.launchMainMenuCB = launchMainMenuCB;
    }

    @FXML protected void onLoginClicked(ActionEvent e){
        launchMainMenuCB.launchMainMenu();
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
