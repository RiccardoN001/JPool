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
import javafx.scene.image.ImageView;
// import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class Controller implements Initializable {

    //MENUSCENE COMPONENTS
    @FXML
    private Button playButton;
    @FXML
    private Button rulesButton;

    // GAMESCENE COMPONENTS
    @FXML
    private ImageView cue;
    @FXML
    private Circle cueBallPreview;
    @FXML
    private Line guidelineToBall;
    @FXML
    private Line guidelineToPocket;
    @FXML
    private Button menuButtonFromGame;

    // RULESSCENE COMPONENTS
    @FXML
    private Button menuButtonFromRules;

    @FXML
    public void handlePlayButton(ActionEvent event) throws Exception {
        
        Stage stage;
        Parent root;

        if(event.getSource()==playButton) {
            stage = (Stage) playButton.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("GameScene.fxml"));
        } else {
            stage = (Stage)  menuButtonFromGame.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("MenuScene.fxml"));
        }

        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    public void handleRulesButton(ActionEvent event) throws Exception {
        
        Stage stage;
        Parent root;

        if(event.getSource()==rulesButton) {
            stage = (Stage)  rulesButton.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("RulesScene.fxml"));
        } else {
            stage = (Stage)  menuButtonFromRules.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("MenuScene.fxml"));
        }

        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

}
