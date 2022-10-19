package game;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class RulesSceneController implements Initializable{

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

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        
    }

}
