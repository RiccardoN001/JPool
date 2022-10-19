package game;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class GameSceneController implements Initializable {

    private Group gameGroup;
    static Ball ball[];

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
    public void addGroup(){
        ball[0] = new Ball (346, 375, "src/game/Resources/CueBallExt.png", 0, 0);
        // TRIANGLE ROW 1
        ball[1] = new Ball (769, 375, "src/game/Resources/Ball1Ext.png", 1, 1);
        // TRIANGLE ROW 2
        ball[11] = new Ball (793, 363, "src/game/Resources/Ball11Ext.png", 2, 11);
        ball[6] = new Ball (793, 388, "src/game/Resources/Ball6Ext.png", 1, 6);
        // TRIANGLE ROW 3
        ball[14] = new Ball (817, 350, "src/game/Resources/Ball14Ext.png", 2, 14);
        ball[8] = new Ball (817, 375, "src/game/Resources/Ball8Ext.png", 3, 8);
        ball[10] = new Ball (817, 400, "src/game/Resources/Ball10Ext.png", 2, 10);
        // TRIANGLE ROW 4
        ball[13] = new Ball (841, 338, "src/game/Resources/Ball13Ext.png", 2, 13);
        ball[15] = new Ball (841, 363, "src/game/Resources/Ball15Ext.png", 2, 15);
        ball[2] = new Ball (841, 388, "src/game/Resources/Ball2Ext.png", 1, 2);
        ball[5] = new Ball (841, 413, "src/game/Resources/Ball5Ext.png", 1, 5); 
        // TRIANGLE ROW 5
        ball[4] = new Ball (865, 325, "src/game/Resources/Ball4Ext.png", 1, 4);
        ball[12] = new Ball (865, 350, "src/game/Resources/Ball12Ext.png", 2, 12);
        ball[3] = new Ball (865, 375, "src/game/Resources/Ball3Ext.png", 1, 3);
        ball[9] = new Ball (865, 400, "src/game/Resources/Ball9Ext.png", 2, 9);
        ball[7] = new Ball (865, 425, "src/game/Resources/Ball7Ext.png", 1, 7);
        for(int i = 0; i < 16; i++) {
            gameGroup.getChildren().add(ball[i].DrawBall());
        }
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
