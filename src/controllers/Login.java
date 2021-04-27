package controllers;

import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import models.SceneCallback.LaunchLobbyCallback;
import services.AuthService;

public class Login {

    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private Button loginBtn;
    @FXML private Button registerBtn;
    @FXML private Button returnBtn;
    @FXML private Pane root;

    public void onLoginAction(){
        try {
            AuthService.getInstance().login(
                "some username",
                "'some password'",
                new Consumer<Boolean>(){
                    @Override
                    public void accept(Boolean success) {
                        MainMenu.this.onLoginResult(success);
                    }
                }
            );
            
            
            //Do something before the server responds to your attempt to log in.
            //For example, show a loading screen.
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void onLoginResult(boolean success){
        if(success){
            //Do what you want to do if it works.
            System.out.println("Yay!");
        } else{
            //Dow what you want to do if it fails.
            System.out.println("Boo!");
        }
    }
}
