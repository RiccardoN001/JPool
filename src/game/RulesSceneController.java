package game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class RulesSceneController{

    @FXML
    private Button menuButtonFromRules;

    @FXML
    void handleMenuButton(ActionEvent event) throws Exception {  
        Stage stage;
        Scene scene;
        Parent root;
        stage = (Stage) menuButtonFromRules.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("MenuScene.fxml"));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
