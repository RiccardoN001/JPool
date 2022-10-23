package game.controller;

import game.Main;
import game.model.Ball;
import game.model.Constants;
import game.model.Player;
import game.model.Vector;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration; // non java.time.Duration !!!

public class GameSceneController {

    // GAMESCENE COMPONENTS
    // STATIC
    @FXML
    private Pane pane;
    @FXML
    private MenuButton menuBar;
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
    // DYNAMIC
    private Ball ball[] = new Ball[16];
    private ImageView[] solidScoreBall = new ImageView[7];
    private ImageView[] stripedScoreBall = new ImageView[7];
    // ANIMATIONS
    private Timeline timeline;

    // GAMESCENE VARIABLES (RULES IMPLEMENTATION)
    private Player player1, player2;
    private boolean turn;
    private boolean gamePause;
    private boolean gameOver;
    private Labeled player1label;
    private Labeled player2Label;
    private Labeled label;


    // SCENE MANAGEMENT METHODS
    @FXML
    public void handleMenuButton(ActionEvent event) throws Exception {
        Stage stage;
        Scene scene;
        Parent root;
        stage = (Stage) menuBar.getScene().getWindow();
        root = FXMLLoader.load(Main.class.getResource("view/MenuScene.fxml"));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // GAME MANAGEMENT METHODS
    @FXML
    public void initialize() { // controller 1(fxml) 2(initialize -> can access fxml injections)
        
        // GAMEBALLS INITIALIZATION (SPLIT)
        // HEAD SPOT
        ball[0] = new Ball(Constants.HEAD_SPOT_X, Constants.HEAD_SPOT_Y, "src/game/Resources/CueBallExt.png", 0, 0);
        // TRIANGLE ROW 1
        ball[1] = new Ball(Constants.FOOT_SPOT_X, Constants.FOOT_SPOT_Y, "src/game/Resources/Ball1Ext.png", 1, 1);
        // TRIANGLE ROW 2
        ball[11] = new Ball(Constants.TRIANGLE_ROW2_X, Constants.TRIANGLE_COL4_Y, "src/game/Resources/Ball11Ext.png", 2, 11);
        ball[6] = new Ball(Constants.TRIANGLE_ROW2_X, Constants.TRIANGLE_COL6_Y, "src/game/Resources/Ball6Ext.png", 1, 6);
        // TRIANGLE ROW 3 Constants.TRIANGLE_COL1_Y
        ball[14] = new Ball(Constants.TRIANGLE_ROW3_X, Constants.TRIANGLE_COL3_Y, "src/game/Resources/Ball14Ext.png", 2, 14);
        ball[8] = new Ball(Constants.TRIANGLE_ROW3_X, Constants.TRIANGLE_COL5_Y, "src/game/Resources/Ball8Ext.png", 3, 8);
        ball[10] = new Ball(Constants.TRIANGLE_ROW3_X, Constants.TRIANGLE_COL7_Y, "src/game/Resources/Ball10Ext.png", 2, 10);
        // TRIANGLE ROW 4
        ball[13] = new Ball(Constants.TRIANGLE_ROW4_X, Constants.TRIANGLE_COL2_Y, "src/game/Resources/Ball13Ext.png", 2, 13);
        ball[15] = new Ball(Constants.TRIANGLE_ROW4_X, Constants.TRIANGLE_COL4_Y, "src/game/Resources/Ball15Ext.png", 2, 15);
        ball[2] = new Ball(Constants.TRIANGLE_ROW4_X, Constants.TRIANGLE_COL6_Y, "src/game/Resources/Ball2Ext.png", 1, 2);
        ball[5] = new Ball(Constants.TRIANGLE_ROW4_X, Constants.TRIANGLE_COL8_Y, "src/game/Resources/Ball5Ext.png", 1, 5); 
        // TRIANGLE ROW 5
        ball[4] = new Ball(Constants.TRIANGLE_ROW5_X, Constants.TRIANGLE_COL1_Y, "src/game/Resources/Ball4Ext.png", 1, 4);
        ball[12] = new Ball(Constants.TRIANGLE_ROW5_X, Constants.TRIANGLE_COL3_Y, "src/game/Resources/Ball12Ext.png", 2, 12);
        ball[3] = new Ball(Constants.TRIANGLE_ROW5_X, Constants.TRIANGLE_COL5_Y, "src/game/Resources/Ball3Ext.png", 1, 3);
        ball[9] = new Ball(Constants.TRIANGLE_ROW5_X, Constants.TRIANGLE_COL7_Y, "src/game/Resources/Ball9Ext.png", 2, 9);
        ball[7] = new Ball(Constants.TRIANGLE_ROW5_X, Constants.TRIANGLE_COL9_Y, "src/game/Resources/Ball7Ext.png", 1, 7);
        // ADD BALLS
        for(int i = 0; i < 16; i++) {
            pane.getChildren().add(ball[i].DrawBall());
        }

        // SCOREBALLS INITIALIZATION
        for(int i = 0; i < 7; i++) {
            solidScoreBall[i] = new ImageView(new Image("file:src/game/Resources/Ball" + String.valueOf(i + 1) + ".png"));
            solidScoreBall[i].setFitHeight(30);
            solidScoreBall[i].setFitWidth(30);
            solidScoreBall[i].setLayoutX(254 + 40*i);
            solidScoreBall[i].setLayoutY(157);
            stripedScoreBall[i] = new ImageView(new Image("file:src/game/Resources/Ball" + String.valueOf(i + 9) + ".png"));
            stripedScoreBall[i].setFitHeight(30);
            stripedScoreBall[i].setFitWidth(30);
            stripedScoreBall[i].setLayoutX(879 + 40*i);
            stripedScoreBall[i].setLayoutY(157);
            pane.getChildren().addAll(solidScoreBall[i], stripedScoreBall[i]);
        }

    }

    public void guidedTrajectory(MouseEvent event) {
        if(isTurn() && !isGameOver() && !isGamePause() && player1.isMyTurn()) {//la guidedTrajectory serve sicuramente ad entrambi i giocatori, quindi servono che calcoli
            guidelineToBall.setVisible(true);
            guidelineToBall.setStroke(Color.WHITE);
            cueBallPreview.setVisible(true);
            cueBallPreview.setStroke(Color.WHITE);

            double xcb = ball[0].getPosition().getX();
            double ycb = ball[0].getPosition().getY();

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
                if(collides(cueBallPreview, ball[i])) {
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
                if(collides(cueBallPreview, ball[i])) {
                    double cueBallVelocity = 50;
                    double angle = Math.atan((ym-ycb)/(xm-xcb));
                    if(xm < xcb) {
                        cueBallVelocity = -cueBallVelocity;
                    }
                    
                    Vector position = new Vector(cueBallPreview.getCenterX(), cueBallPreview.getCenterY());
                    Vector velocity = new Vector(cueBallVelocity * Math.cos(angle), cueBallVelocity * Math.sin(angle));

                    // a and b roles !!!

                    Vector normalToB = position.sub(ball[i].getPosition());
                    normalToB.normalize();
                    normalToB.multiply(velocity.scalar(normalToB));

                    Vector normalToA = ball[i].getPosition().sub(position);
                    normalToA.normalize();
                    normalToA.multiply(ball[i].getVelocity().scalar(normalToA));

                    Vector bCollisionVector = ball[i].getVelocity().sub(normalToB);
                    Vector finalVelocity = bCollisionVector.add(normalToA);

                    double x = ball[i].getSphere().getLayoutX();
                    double y = ball[i].getSphere().getLayoutY();

                    guidelineFromBall.setStartX(x);
                    guidelineFromBall.setStartY(y);

                    guidelineFromBall.setEndX(x + finalVelocity.getX());
                    guidelineFromBall.setEndY(y + finalVelocity.getX());

                    guidelineFromCue.setStartX(xcb);
                    guidelineFromCue.setStartY(ycb);

                    guidelineFromCue.setEndX(y);
                    guidelineFromCue.setEndY(y);

                }
            }

            // da rivedere
            cue.setVisible(true);
            cue.setLayoutX (xcb - (346 + 36));
            cue.setLayoutY (ycb - 14); //Questi 3 perché non li avevi messi?Li hai già dichiarati sopra?
            double ang = Math.toDegrees(Math.atan((ym-ycb)/(xm-xcb)));
            if(xm <= xcb) {
                ang = 180 - ang;
                ang = - ang;
            }
            cue.setRotate(ang);
            double mid_x = cue.getLayoutX () + cue.getFitWidth () / 2;
            double mid_y = cue.getLayoutY () + cue.getFitHeight () / 2;
            double dist = (xcb - mid_x);
            double now_y = Math.sin (Math.toRadians (-ang)) * dist + mid_y;
            double now_x = mid_x + (dist - dist * Math.cos (Math.toRadians (ang)));
            double pos_x = now_x - cue.getFitWidth () / 2;
            double pos_y = now_y - cue.getFitHeight () / 2 + 4;
            cue.setLayoutX (pos_x);
            cue.setLayoutY (pos_y);

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

    public void released(MouseEvent event) { 
        if (isTurn() && !isGameOver() && !isGamePause() && player1.isMyTurn()) {
            double x = event.getSceneX (); //Returns horizontal position of the event relative to the origin of the Scene that contains the MouseEvent's source.
            double y = event.getSceneY (); //Returns Vertical position of the event relative to the origin of the Scene that contains the MouseEvent's source.
            double xp = x; //update variabile d'ambiente
            double yp = y;
        }
    }

    public void showVelocity() {

    }

    






    // ANIMATION MANAGEMENT METHODS
    public void startGame() {
        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame keyFrame = new KeyFrame (
                Duration.seconds(0.015),
                event -> {

                });
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    public void stopGame() {
        timeline.stop ();
        gamePause = true;
    }

    public void startFromPause() {
        gamePause = false;
        timeline.play ();
    }

    public void startNewGame() {
        stopGame ();
        gamePause = false;
        timeline.play ();
    }


    // GET/SET METHODS
    public boolean isTurn() {
        return turn;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isGamePause() {
        return gamePause;
    }



}
