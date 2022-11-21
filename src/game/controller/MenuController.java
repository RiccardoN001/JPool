package game.controller;

// BRIEF CLASS DESCRIPTION
// Controls Menu page (Menu.fxml)

import game.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MenuController {

    @FXML
    private Button startgameButton;
    @FXML
    private Button htpButton;
    @FXML
    private Button exitButton;

    @FXML
    void handleStartGameButton(ActionEvent event) throws Exception {
        Stage stage;
        Scene scene;
        Parent root;
        stage = (Stage) startgameButton.getScene().getWindow();
        root = FXMLLoader.load(Main.class.getResource("view/Settings.fxml"));
        scene = new Scene(root);
        scene.getStylesheets().addAll(Main.class.getResource("view/style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void handleHTPButton(ActionEvent event) throws Exception {
        Stage stage;
        Scene scene;
        Parent root;
        stage = (Stage) htpButton.getScene().getWindow();
        root = FXMLLoader.load(Main.class.getResource("view/HTP.fxml"));
        scene = new Scene(root);
        scene.getStylesheets().addAll(Main.class.getResource("view/style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void handleExitButton(ActionEvent event) throws Exception {
        Stage stage;
        stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
        stage.setOnCloseRequest(e -> System.exit(0));
    }

}
