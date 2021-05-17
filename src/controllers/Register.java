package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import models.SceneCallback.LaunchRegisterCallback;
import models.SceneCallback.ReturnToCallback;

public class Register {
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private Button registerBtn;
    @FXML private Button returnBtn;
    @FXML private Pane root;

    private ReturnToCallback returnToCB;
    private LaunchRegisterCallback launchRegisterCB;

    @FXML protected void returnButtonClicked(ActionEvent e){
        returnToCB.returnTo();
    }

    @FXML protected void registerButtonClicked(ActionEvent e){
        //if the username is taken
            //return error message
        //else
        returnToCB.returnTo();
    }

    public Pane getRoot(){ return this.root; }

    public void setLaunchRegisterCallback(LaunchRegisterCallback launchRegisterCB){ this.launchRegisterCB = launchRegisterCB;}

    public void setReturnToCB(ReturnToCallback returnToCB){this.returnToCB = returnToCB;}
}