package game.controller;

import java.util.ArrayList;

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
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameSceneController {

    // GAMESCENE COMPONENTS
    // STATIC
    @FXML
    private Pane pane;
    @FXML
    private MenuButton menuBar;
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
    @FXML
    private Label player1NicknameLabel;
    @FXML
    private Label player2NicknameLabel;
    @FXML
    private Label scoreboardLabel;
    @FXML
    private ProgressBar timer;
    @FXML
    private Label sliderVelocityLabel;
    // DYNAMIC
    private ImageView cue;
    private Ball ball[] = new Ball[16];
    private ImageView[] solidScoreBall = new ImageView[7];
    private ImageView[] stripedScoreBall = new ImageView[7];
    // ANIMATIONS
    private Timeline timeline;

    // GAMESCENE VARIABLES (RULES IMPLEMENTATION)
    private Player player1, player2;
    private boolean turn = true;
    private int turnNum;
    private boolean gamePause = false;
    private boolean gameOver = false;
    private double angle;
    private double xp,yp;
    private double stack_y = 630;
    // control flags
    private boolean turnChange;
    private boolean foulWhat3;
    private boolean foulWrongBallType;
    private boolean foulNoBallHit;
    private ArrayList<Integer> thisTurnPottedBalls;
    private boolean foul;

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
    public void initialize() throws Exception {
        
        // GAMEBALLS INITIALIZATION (SPLIT)
        // HEAD SPOT
        ball[0] = new Ball(Constants.HEAD_SPOT_X, Constants.HEAD_SPOT_Y, "src/game/resources/Balls/CueBallExt.png", 0, 0);
        // TRIANGLE ROW 1
        ball[1] = new Ball(Constants.FOOT_SPOT_X-150, Constants.FOOT_SPOT_Y-100, "src/game/resources/Balls/Ball1Ext.png", 1, 1);
        // TRIANGLE ROW 2
        ball[11] = new Ball(Constants.TRIANGLE_ROW2_X-200, Constants.TRIANGLE_COL4_Y+100, "src/game/resources/Balls/Ball11Ext.png", 2, 11);
        ball[6] = new Ball(Constants.TRIANGLE_ROW2_X-600, Constants.TRIANGLE_COL6_Y+100, "src/game/resources/Balls/Ball6Ext.png", 1, 6);
        // TRIANGLE ROW 3 Constants.TRIANGLE_COL1_Y
        ball[14] = new Ball(Constants.TRIANGLE_ROW3_X, Constants.TRIANGLE_COL3_Y, "src/game/resources/Balls/Ball14Ext.png", 2, 14);
        ball[8] = new Ball(Constants.TRIANGLE_ROW3_X, Constants.TRIANGLE_COL5_Y, "src/game/resources/Balls/Ball8Ext.png", 3, 8);
        ball[10] = new Ball(Constants.TRIANGLE_ROW3_X, Constants.TRIANGLE_COL7_Y, "src/game/resources/Balls/Ball10Ext.png", 2, 10);
        // TRIANGLE ROW 4
        ball[13] = new Ball(Constants.TRIANGLE_ROW4_X, Constants.TRIANGLE_COL2_Y, "src/game/resources/Balls/Ball13Ext.png", 2, 13);
        ball[15] = new Ball(Constants.TRIANGLE_ROW4_X, Constants.TRIANGLE_COL4_Y, "src/game/resources/Balls/Ball15Ext.png", 2, 15);
        ball[2] = new Ball(Constants.TRIANGLE_ROW4_X, Constants.TRIANGLE_COL6_Y, "src/game/resources/Balls/Ball2Ext.png", 1, 2);
        ball[5] = new Ball(Constants.TRIANGLE_ROW4_X, Constants.TRIANGLE_COL8_Y, "src/game/resources/Balls/Ball5Ext.png", 1, 5); 
        // TRIANGLE ROW 5
        ball[4] = new Ball(Constants.TRIANGLE_ROW5_X, Constants.TRIANGLE_COL1_Y, "src/game/resources/Balls/Ball4Ext.png", 1, 4);
        ball[12] = new Ball(Constants.TRIANGLE_ROW5_X, Constants.TRIANGLE_COL3_Y, "src/game/resources/Balls/Ball12Ext.png", 2, 12);
        ball[3] = new Ball(Constants.TRIANGLE_ROW5_X, Constants.TRIANGLE_COL5_Y, "src/game/resources/Balls/Ball3Ext.png", 1, 3);
        ball[9] = new Ball(Constants.TRIANGLE_ROW5_X, Constants.TRIANGLE_COL7_Y, "src/game/resources/Balls/Ball9Ext.png", 2, 9);
        ball[7] = new Ball(Constants.TRIANGLE_ROW5_X, Constants.TRIANGLE_COL9_Y, "src/game/resources/Balls/Ball7Ext.png", 1, 7);
        // ADD BALLS
        for(int i = 0; i < 16; i++) {
            pane.getChildren().add(ball[i].DrawBall());
        }

        // SCOREBALLS INITIALIZATION
        for(int i = 0; i < 7; i++) {
            solidScoreBall[i] = new ImageView(new Image("file:src/game/resources/ScoreBalls/Ball" + String.valueOf(i + 1) + ".png"));
            solidScoreBall[i].setFitHeight(30);
            solidScoreBall[i].setFitWidth(30);
            solidScoreBall[i].setLayoutX(254 + 40*i);//254
            solidScoreBall[i].setLayoutY(157);//157
            stripedScoreBall[i] = new ImageView(new Image("file:src/game/resources/ScoreBalls/Ball" + String.valueOf(i + 9) + ".png"));
            stripedScoreBall[i].setFitHeight(30);
            stripedScoreBall[i].setFitWidth(30);
            stripedScoreBall[i].setLayoutX(879 + 40*i);
            stripedScoreBall[i].setLayoutY(157);
            ImageView blackball = new ImageView(new Image("file:src/game/resources/ScoreBalls/Ball8.png"));
            blackball.setFitHeight(60);
            blackball.setFitWidth(60);
            blackball.setLayoutX(672);
            blackball.setLayoutY(142);
            pane.getChildren().addAll(solidScoreBall[i], stripedScoreBall[i],blackball);
        }

        //CUE LOADING
        cue = new ImageView(new Image("file:src/game/resources/Cues/Cue"+String.valueOf(SettingsSceneController.getSettingsSceneController().cueMenuIndex()+1 +".png")));
        cue.setFitWidth(400);
        cue.setFitHeight(100);
        cue.setLayoutX(105);
        cue.setLayoutY(447);
        cue.setPreserveRatio(true);
        pane.getChildren().add(cue);
        
        // startGame(); 
    }

    
    // on mouse dragged fxml
    @FXML
    public void guidedTrajectory(MouseEvent event) {
        if(isTurn() && !isGameOver() && !isGamePause() /*&& player1.isMyTurn()*/) {//la guidedTrajectory serve sicuramente ad entrambi i giocatori, quindi servono che calcoli
            guidelineToBall.setVisible(true);
            guidelineToBall.setStroke(Color.WHITE);
            cueBallPreview.setVisible(true);
            cueBallPreview.setFill(Color.TRANSPARENT);
            cueBallPreview.setStroke(Color.WHITE);

            guidelineFromBall.setVisible(true);

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
            cueBallPreview.setRadius(10);
            guidelineFromBall.setVisible(false);
            guidelineFromCue.setVisible(false);
            guidelineFromBall.setStroke(Color.WHITE);
            guidelineFromCue.setStroke(Color.WHITE);

            for(int i = 0; i < 16; i++) {
                if(collides(cueBallPreview, ball[i])) {
                    double cueBallVelocity = 40;
                    Vector cueVelocity = new Vector(cueBallVelocity, cueBallVelocity);
                    // double angle = Math.atan((ym-ycb)/(xm-xcb));
                    double angle = Math.atan2(ym-ycb, xm-xcb);
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

                    Vector bCollisionVector = ball[i].getVelocity().sub(normalToA);
                    Vector ballFinalVelocity = bCollisionVector.add(normalToB);

                    Vector aCollisionVector = cueVelocity.sub(normalToB);
                    Vector cueFinalVelocity = aCollisionVector.add(normalToA);

                    double x = ball[i].getSphere().getLayoutX();
                    double y = ball[i].getSphere().getLayoutY();

                    guidelineFromBall.setStartX(x);
                    guidelineFromBall.setStartY(y);

                    guidelineFromBall.setEndX(x + ballFinalVelocity.getX());
                    guidelineFromBall.setEndY(y + ballFinalVelocity.getY());


                    guidelineFromCue.setStartX(xm);
                    guidelineFromCue.setStartY(ym);

                    guidelineFromCue.setEndX(xm+cueFinalVelocity.getX());
                    guidelineFromCue.setEndY(ym+cueFinalVelocity.getY());

                    guidelineFromBall.setVisible(true);
                    guidelineFromCue.setVisible(true);

                }
            }

            // da rivedere
            cue.setVisible(true);
            cue.setLayoutX (Constants.HEAD_SPOT_X - 385);
            cue.setLayoutY (Constants.HEAD_SPOT_Y - 20);
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
        double centersdistance = Math.sqrt(x * x + y * y);

        if (centersdistance - ball.getDiameter() <= 3) {
            return true;
        } else {
            return false;
        }
    }

    // on mouse released fxml
    @FXML
    public void released(MouseEvent event) { 
        if (isTurn() && !isGameOver() && !isGamePause()) {
            double x = event.getSceneX (); //Returns horizontal position of the event relative to the origin of the Scene that contains the MouseEvent's source.
            double y = event.getSceneY (); //Returns Vertical position of the event relative to the origin of the Scene that contains the MouseEvent's source.
            double xp = x; //update variabile d'ambiente
            double yp = y;
        }
    }

    public void showVelocity() {
        if (isTurn() && !isGameOver() && !isGamePause() && player1.isMyTurn()) {
            sliderVelocityLabel.setText(String.valueOf(Math.floor(powerSlider.getValue() / 30 * 100)));
            cue.setLayoutX(0);
            cue.setLayoutY(0);
            /*angle = Math.toDegrees (Math.atan ((yp - ball[0].getPosition().getY()) / (xp - ball[0].getPosition().getX())));
            if (ball[0].getPosition().getX() >= xp) {
                angle = 180 - angle;
                angle = -angle;
            }*/
            angle = Math.toDegrees(Math.atan2(yp - ball[0].getPosition().getY(), xp - ball[0].getPosition().getX()));

            double mid_x = cue.getLayoutX() + cue.getFitWidth() / 2;
            double mid_y = cue.getLayoutY() + cue.getFitHeight() / 2;
            double dist = ball[0].getPosition().getX() - mid_x;

            double new_x = mid_x + (dist-dist*Math.cos(Math.toRadians(angle)));
            double new_y = mid_y + dist*Math.sin(Math.toRadians(angle));

            cue.setLayoutX(new_x - cue.getFitWidth() / 2);
            cue.setLayoutY(new_y - cue.getFitHeight() / 2);
        }
    }

    public void mereDaw()  {
        double cueBallVelocity = 0;
        if(isTurn() && !isGameOver() && !isGamePause() && player1.isMyTurn()) {
            // sound effects
            cueBallVelocity = powerSlider.getValue();
            if(cueBallVelocity != 0) {
                guidelineFromBall.setVisible(false);
                cueBallPreview.setVisible(false);
                powerSlider.setValue(0);
                sliderVelocityLabel.setText("0");
                
                double angle = Math.toDegrees(Math.atan2(yp - ball[0].getPosition().getY(), xp - ball[0].getPosition().getX()));
                setCueVelocity(cueBallVelocity * Math.cos(angle), cueBallVelocity * Math.sin(angle));

                xp = -1;
                yp = -1;

                cue.setVisible(false);

            }

        } else {
            powerSlider.setValue(0);
            sliderVelocityLabel.setText("0");
        }
    }

    public void reinitialize() {
        // if necessary
    }

    private void update() {

        if(turnNum == 1) {
            labelDekhaw();
        }

        int flag = 0;
        moveCueBall();

        for(int i = 0; i < 16; i++) {
            if(!ball[i].getVelocity().isNull()) { // per ogni palla non ferma
                flag = 1;
                turnChange = true;
            }

            updateBalls(i);
            checkPocket(i);
        }

        if(flag == 1) {
            turn = false;
        } else if(flag == 0 && !turnChange) {
            turn = true;
            // turn label
        } else if(flag == 0 && turnChange) {
            foul = false;
            // check cases method
            // check potted balls method
            if(isFoul() && !gameOver) {
                stopGame();
            }
        }



    }

    private void labelDekhaw() {
        player1NicknameLabel.setText(player1.getNickname());
        player2NicknameLabel.setText(player2.getNickname());
        if(player1.isMyTurn()) {
            scoreboardLabel.setText(player1.getNickname() + "IS BREAKING");
        } else {
            scoreboardLabel.setText(player2.getNickname() + "IS BREAKING");
        }
    }

    private void moveCueBall() {

        ball[0].getSphere().addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            // split
            if(isTurn() && turnNum == 1 && player1.isMyTurn()) {
                cue.setVisible(false);
                guidelineToBall.setVisible(false);
                cueBallPreview.setVisible(false);
                guidelineFromBall.setVisible(false);
                guidelineFromCue.setVisible(false);
                ball[0].getSphere().setCursor(Cursor.CLOSED_HAND); // imposta la "manina" che afferra la palla
                if(event.getSceneX() >= Constants.LEFT_BANK &&event.getSceneX() <= Constants.HEAD_SPOT_X && event.getSceneY() >= Constants.UP_BANK && event.getSceneY() <= Constants.DOWN_BANK) { // controllo che la palla venga posizionata nel rettangolo head spot
                    ball[0].setPosition(new Vector(event.getSceneX(), event.getSceneY()));
                }
            // after foul
            } else if (isTurn() && isFoul() && player1.isMyTurn()) {
                cue.setVisible(false);
                guidelineToBall.setVisible(false);
                cueBallPreview.setVisible(false);
                guidelineFromBall.setVisible(false);
                guidelineFromCue.setVisible(false);
                ball[0].getSphere().setCursor(Cursor.CLOSED_HAND);
                if(event.getSceneX() >= Constants.LEFT_BANK &&event.getSceneX() <= Constants.RIGHT_BANK && event.getSceneY() >= Constants.UP_BANK && event.getSceneY() <= Constants.DOWN_BANK) { // palla nel campo
                    ball[0].setPosition(new Vector(event.getSceneX(), event.getSceneY()));
                }
            }


            


        });

    }

    // virgola mobile
    //double mille = 1000.0;
    //double milleSci = 1.0e3;

    private void updateBalls(int ballNum) {
        if(ball[ballNum].getVelocity().getSize() <= 8e-2) { // 0.08 da verificare come valore
            ball[ballNum].setVelocity(0, 0);  
        } else { // se ancora si ha velocità significativa
            ball[ballNum].getPosition ().setX (ball[ballNum].getPosition ().getX () + ball[ballNum].getVelocity ().getX ());
            ball[ballNum].getPosition ().setY (ball[ballNum].getPosition ().getY () + ball[ballNum].getVelocity ().getY ());
            for(Ball b: ball) { // scorrimento lista (vedi segnalibro chrome)
                if(ballNum != b.getBallNumber() && ball[ballNum].collides(b)) {

                    if(ballNum == 0 && !foulWrongBallType && player1.getBallType() == 0) {
                        foulWrongBallType = true;
                        if(b.getBallType() == 3) {
                            foulWhat3 = true; // first foul type
                        }
                    } else if (ballNum == 0 && !foulWrongBallType && player1.getBallType() != 0) {
                        foulWrongBallType = true;
                        if(player1.isMyTurn()) { // se è il turno di P1
                            if(player1.getBallType() != b.getBallType()) {
                                if(b.getBallNumber() == 8 && player1.isAllBallsPlotted()) {
                                    foulWhat3 = false;
                                } else {
                                    foulWhat3 = true;
                                }
                            }
                        } else { // se è di P2
                            if(player2.getBallType() != b.getBallType()) {
                                if(b.getBallNumber() == 8 && player2.isAllBallsPlotted()) {
                                    foulWhat3 = false;
                                } else {
                                    foulWhat3 = true;
                                }
                            }
                        }
                    } else if(ballNum == 0) {
                        foulNoBallHit = false;
                    }

                    ball[ballNum].getPosition().setX(ball[ballNum].getPosition().getX() - ball[ballNum].getVelocity().getX());
                    ball[ballNum].getPosition().setY(ball[ballNum].getPosition().getY() - ball[ballNum].getVelocity().getY());
                    ball[ballNum].ballCollision(b);
                    break;

                }
            }

            ball[ballNum].bankCollision();
            ball[ballNum].tableFriction();
            ball[ballNum].spin();
        }
        ball[ballNum].getSphere().setLayoutX(ball[ballNum].getPosition().getX());
        ball[ballNum].getSphere().setLayoutY(ball[ballNum].getPosition().getY());
    }


    private void checkPocket(int ballNum) {

        double x = ball[ballNum].getPosition().getX();
        double y = ball[ballNum].getPosition().getY();

        double check = 25; //forse per 

        if (distance(x, y, Constants.TOP_LEFT_POCKET_X, Constants.TOP_LEFT_POCKET_Y) <= check) {
            dropit (ballNum);
        }
        else if (distance(x, y, Constants.BOTTOM_LEFT_POCKET_X, Constants.BOTTOM_LEFT_POCKET_Y) <= check) {
            dropit (ballNum);
        }
        else if (distance(x, y, Constants.TOP_MIDDLE_POCKET_X, Constants.TOP_MIDDLE_POCKET_Y) <= check) {
            dropit (ballNum);
        }
        else if (distance(x, y, Constants.BOTTOM_MIDDLE_POCKET_X, Constants.BOTTOM_MIDDLE_POCKET_Y) <= check) {
            dropit (ballNum);
        }
        else if (distance(x, y, Constants.TOP_RIGHT_POCKET_X, Constants.TOP_RIGHT_POCKET_Y) <= check) {
            dropit (ballNum);
        }
        else if (distance(x, y, Constants.BOTTOM_RIGHT_POCKET_X, Constants.BOTTOM_RIGHT_POCKET_Y) <= check) {
            dropit (ballNum);
        }
        if ((y <= 230 || y >= 750) && !ball[ballNum].isDropped ()) { //Per essere sicuri che qualsiasi palla fuori dal campo risulti imbucata
            dropit (ballNum);
        }
        if ((x <= 270 || x >= 1190) && !ball[ballNum].isDropped ()) {
            dropit (ballNum);
        }

    }

    private double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1 - x2)*(x1 - x2) + (y1 - y2)*(y1 - y2));
    }

    private void dropit(int ballNum) {
        thisTurnPottedBalls.add(Integer.valueOf(ballNum));
        ball[ballNum].setDropped(true);
        ball[ballNum].setVelocity(0, 0);
        ball[ballNum].setPosition(new Vector(1196, stack_y)); // nello stack
        if(ballNum<=7 && ballNum!= 0){ //ScoreBoard update
            solidScoreBall[ballNum].setVisible(false);
        }
        else if(ballNum>=9 && ballNum!= 0){
            stripedScoreBall[ballNum].setVisible(false);
        }

        stack_y -= 25; //Se entra la bianca
        if (ballNum == 0) {
            stack_y += 25;
            ball[0].getSphere ().setVisible (false);
            ball[0].setPosition (new Vector (0, 0));
            ball[0].setDropped (false);
        }
    }

    private void checkCases() {

    }

    private void checkPotted() {

    }
    private void changeTurn() {
        if (player1.isMyTurn ()) {
            player1.setMyTurn (false);
            player2.setMyTurn (true);
        }
        else {
            player2.setMyTurn (false);
            player1.setMyTurn (true);
        }
        //if (!TurnOffSounds)
            //SoundEffects.TURNCHANGE.play (); Suono per il cambio di turno
    }


    // ANIMATION MANAGEMENT METHODS
    public void startGame() {
        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame keyFrame = new KeyFrame (
                Duration.seconds(0.015),
                event -> {
                    update();
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

    public void setCueVelocity(double x, double y) {
        ball[0].setVelocity(x, y);
    }

    public boolean isFoul() {
        return foul;
    }



}
