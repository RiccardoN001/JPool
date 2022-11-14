package game.controller;

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
    private Button playSettingsButton;

    @FXML
    private Button rulesButton;

    @FXML
    private Button exitButton;

    @FXML
    void handlePlaySettingsButton(ActionEvent event) throws Exception {
        Stage stage;
        Scene scene;
        Parent root;
        stage = (Stage) playSettingsButton.getScene().getWindow();
        root = FXMLLoader.load(Main.class.getResource("view/Settings.fxml"));
        scene = new Scene(root);
        scene.getStylesheets().addAll(Main.class.getResource("view/style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void handleRulesButton(ActionEvent event) throws Exception {
        Stage stage;
        Scene scene;
        Parent root;
        stage = (Stage) rulesButton.getScene().getWindow();
        root = FXMLLoader.load(Main.class.getResource("view/HTP.fxml"));
        scene = new Scene(root);
        scene.getStylesheets().addAll(Main.class.getResource("view/style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void handleExitButton(ActionEvent event) throws Exception {
        Stage stage;
        stage = (Stage) rulesButton.getScene().getWindow();
        stage.close();
    }

}
