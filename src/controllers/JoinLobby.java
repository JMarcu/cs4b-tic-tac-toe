package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.AnchorPane;

public class JoinLobby {
    @FXML private Button returnBtn;
    @FXML private Label title;
    @FXML private MenuButton timeMenu;
    @FXML private Button refreshBtn;
    @FXML private ListView<String> lobbyView;
    @FXML private AnchorPane root;
}
