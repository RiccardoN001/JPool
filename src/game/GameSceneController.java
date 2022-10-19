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
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class GameSceneController implements Initializable {

    //MENUSCENE COMPONENTS
    @FXML
    private Button playButton;
    @FXML
    private Button rulesButton;
    // GAMESCENE COMPONENTS (STATIC)
    @FXML
    private ImageView cue;
    @FXML
    private Circle cueBallPreview;
    @FXML
    private Line guidelineToBall;
    @FXML
    private Line guidelineFromBall;
    @FXML
    private Line guidelineFromCue;
    @FXML
    private Slider powerSlider;
    @FXML
    private Button menuButtonFromGame;
    // RULESSCENE COMPONENTS
    @FXML
    private Button menuButtonFromRules;

    // SCENE MANAGEMENT METHODS
    @FXML
    public void handleMenuButton(ActionEvent event) throws Exception {
        Stage stage;
        Scene scene;
        Parent root;
        stage = (Stage) menuButtonFromGame.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("MenuScene.fxml"));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

}
