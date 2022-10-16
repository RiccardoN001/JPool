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
import javafx.scene.input.MouseEvent;
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
    public void handlePlayButton(ActionEvent event) throws Exception {
        
        Stage stage;
        Scene scene;
        Parent root;

        if(event.getSource()==playButton) {
            stage = (Stage) playButton.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("GameScene.fxml"));
        } else {
            stage = (Stage)  menuButtonFromGame.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("MenuScene.fxml"));
        }

        scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    public void handleRulesButton(ActionEvent event) throws Exception {
        
        Stage stage;
        Scene scene;
        Parent root;

        if(event.getSource()==rulesButton) {
            stage = (Stage)  rulesButton.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("RulesScene.fxml"));
        } else {
            stage = (Stage)  menuButtonFromRules.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("MenuScene.fxml"));
        }

        scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();

    }

    // GAME MANAGEMENT METHODS

    public void guidedTrajectory(MouseEvent event) {
        if(GameScene.isTurn() && !GameScene.isGameOver() && !GameScene.isGamePause() && GameScene.getPlayer1().isMyTurn()) {
            guidelineToBall.setVisible(true);
            // guidelineToBall.setStroke(Color.WHITE);
            cueBallPreview.setVisible(true);
            // cueBallPreview.setStroke(Color.WHITE);

            double xcb = GameScene.getCueBall().getPosition().getX();
            double ycb = GameScene.getCueBall().getPosition().getY();

            double xm = event.getSceneX();
            double ym = event.getSceneY();

            guidelineToBall.setStartX(xcb);
            guidelineToBall.setStartY(ycb);
            guidelineToBall.setEndX(xm);
            guidelineToBall.setEndY(ym);

            cueBallPreview.setCenterX(xm);
            cueBallPreview.setCenterY(ym);
            cueBallPreview.setRadius(12.5);

            boolean hitBallFound = false;
            for(int i = 0; i < 16; i++) {
                if(collides(cueBallPreview, GameScene.ball[i])) {
                    hitBallFound = true;
                    break; 
                }
            }

            if(hitBallFound) {
                guidelineToBall.setVisible(true);
            } else {
                guidelineToBall.setVisible(false);
            }

            for(int i = 0; i < 16; i++) {
                if(collides(cueBallPreview, GameScene.ball[i])) {
                    
                }
            }

        }
    }

    private boolean collides(Circle circle, Ball ball) {
        double x = circle.getCenterX() - ball.getPosition().getX();
        double y = circle.getCenterY() - ball.getPosition().getY();
        double centersDistance = Math.sqrt(x * x + y * y);

        if (centersDistance - ball.getDiameter() <= 0 && centersDistance - ball.getDiameter() >= -3) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

}
