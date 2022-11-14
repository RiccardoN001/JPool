package game.controller;

import game.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class HTPController{

    @FXML
    private Button menuButtonFromRules;

    @FXML
    void handleMenuButton(ActionEvent event) throws Exception {  
        Stage stage;
        Scene scene;
        Parent root;
        stage = (Stage) menuButtonFromRules.getScene().getWindow();
        root = FXMLLoader.load(Main.class.getResource("view/Menu.fxml"));
        scene = new Scene(root);
        scene.getStylesheets().addAll(Main.class.getResource("view/style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

}