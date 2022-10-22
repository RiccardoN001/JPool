package game.controller;

import game.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MenuSceneController {

    @FXML
    private Button playSettingsButton;

    @FXML
    private Button rulesButton;

    @FXML
    void handlePlaySettingsButton(ActionEvent event) throws Exception {
        Stage stage;
        Scene scene;
        Parent root;
        stage = (Stage) playSettingsButton.getScene().getWindow();
        root = FXMLLoader.load(Main.class.getResource("view/SettingsScene.fxml"));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void handleRulesButton(ActionEvent event) throws Exception {
        Stage stage;
        Scene scene;
        Parent root;
        stage = (Stage) rulesButton.getScene().getWindow();
        root = FXMLLoader.load(Main.class.getResource("view/RulesScene.fxml"));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
