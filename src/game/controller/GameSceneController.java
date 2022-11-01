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

    // -------------------------------------------------- SCENE ATTRIBUTES --------------------------------------------------

    @FXML
    private Pane pane;

    @FXML
    private MenuButton menuBar;
    @FXML
    private Button menuButtonFromGame;

    // -------------------------------------------------- GAME ATTRIBUTES --------------------------------------------------

    private ImageView cue;
    @FXML
    private Line guidelineToBall;
    @FXML
    private Circle ghostBall;
    @FXML
    private Line guidelineFromBall;
    @FXML
    private Line guidelineFromCue;

    @FXML
    private Slider powerSlider;
    @FXML
    private Label sliderVelocityLabel;

    private double xmr = -1;
    private double ymr = -1;

    private Ball ball[] = new Ball[16];
    private ImageView[] solidScoreBall = new ImageView[7];
    private ImageView[] stripedScoreBall = new ImageView[7];

    @FXML
    private Label player1NicknameLabel;
    @FXML
    private Label player2NicknameLabel;
    @FXML
    private Label scoreboardLabel;
    @FXML
    private ProgressBar timer;

    private Player player1;
    private Player player2;
    private boolean turn;
    private int turnNum;
    private boolean foul;
    private boolean gamePause;
    private boolean gameOver;

    private ArrayList<Integer> thisTurnPottedBalls;
    private boolean potted[] = new boolean[16];

    private boolean turnChange;
    private boolean foulEight;
    private boolean foulWrongBallType;
    private boolean foulNoBallHit;

    private Timeline timeline = new Timeline();

    // -------------------------------------------------- SCENE METHODS --------------------------------------------------

    @FXML
    public void handleMenuButton(ActionEvent event) throws Exception {
        Stage stage;
        Scene scene;
        Parent root;
        stage = (Stage) menuBar.getScene().getWindow();
        root = FXMLLoader.load(Main.class.getResource("view/MenuScene.fxml"));
        scene = new Scene(root);
        scene.getStylesheets().addAll(Main.class.getResource("view/style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    // -------------------------------------------------- ANIMATION METHODS --------------------------------------------------

    public void startGame() {
        KeyFrame keyFrame = new KeyFrame (
                Duration.seconds(0.015),
                event -> {
                    if(!gameOver) {
                        update();
                    } else {
                        gameOver = false;
                        // gameoverdilg
                    }
                });
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void stopGame() {
        timeline.stop();
        gamePause = true;
    }

    public void startFromPause() {
        gamePause = false;
        timeline.play();
    }

    public void startNewGame() {
        stopGame();
        gamePause = false;
        timeline.play();
    }

    // -------------------------------------------------- GAME METHODS --------------------------------------------------

    // -------------------------------------------------- FXML LINKED --------------------------------------------------

    @FXML
    public void initialize() throws Exception {
        
        // SPLIT
        turnNum = 1;

        // HEAD SPOT
        ball[0] = new Ball(Constants.HEAD_SPOT_X, Constants.HEAD_SPOT_Y, "src/game/resources/Balls/CueBallExt.png", 0, 0);
        // TRIANGLE ROW 1
        ball[1] = new Ball(Constants.FOOT_SPOT_X-150, Constants.FOOT_SPOT_Y-100, "src/game/resources/Balls/Ball1Ext.png", 1, 1);
        // TRIANGLE ROW 2
        ball[11] = new Ball(Constants.TRIANGLE_ROW2_X-200, Constants.TRIANGLE_COL4_Y+100, "src/game/resources/Balls/Ball11Ext.png", 2, 11);
        ball[6] = new Ball(Constants.TRIANGLE_ROW2_X-600, Constants.TRIANGLE_COL6_Y+100, "src/game/resources/Balls/Ball6Ext.png", 1, 6);
        // TRIANGLE ROW 3
        ball[14] = new Ball(Constants.TRIANGLE_ROW3_X-600, Constants.TRIANGLE_COL3_Y-100, "src/game/resources/Balls/Ball14Ext.png", 2, 14);
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
        for(int i = 0; i < 16; i++) {
            pane.getChildren().add(ball[i].DrawBall());
        }

        // CUE LOADING
        cue = new ImageView(new Image("file:src/game/resources/Cues/Cue"+String.valueOf(SettingsSceneController.getSettingsSceneController().cueMenuIndex()+1 +".png")));
        cue.setFitWidth(400);
        cue.setFitHeight(100);
        cue.setLayoutX(105);
        cue.setLayoutY(447);
        cue.setPreserveRatio(true);
        pane.getChildren().add(cue);

        // PLAYERS
        player1 = new Player(SettingsSceneController.getSettingsSceneController().getP1Nickname());
        player2 = new Player(SettingsSceneController.getSettingsSceneController().getP2Nickname());

        // CONTROL VARIABLES
        player1.setMyTurn(turn); // random ?
        turn = true;
        foul = false;
        gamePause = false;
        gameOver = false;
        turnChange = false;
        foulEight = false;
        foulWrongBallType = false;
        foulNoBallHit = true;

        // POTTED BALLS
        thisTurnPottedBalls = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            potted[i] = false;
        }
        
        startGame();
    }

    @FXML
    public void guidedTrajectory(MouseEvent event) {

        double xm = event.getSceneX();
        double ym = event.getSceneY();

        if(turn && !gamePause && !gameOver && xm >= Constants.LEFT_BANK && xm <= Constants.RIGHT_BANK && ym >= Constants.UP_BANK && ym <= Constants.DOWN_BANK) {

            guidelineToBall.setStroke(Color.WHITE);
            ghostBall.setStroke(Color.WHITE);
            ghostBall.setFill(Color.TRANSPARENT);
            guidelineFromBall.setStroke(Color.WHITE);
            guidelineFromCue.setStroke(Color.WHITE);

            double xcb = ball[0].getPosition().getX();
            double ycb = ball[0].getPosition().getY();

            guidelineToBall.setStartX(xcb);
            guidelineToBall.setStartY(ycb);
            guidelineToBall.setEndX(xm);
            guidelineToBall.setEndY(ym);
            ghostBall.setCenterX(xm);
            ghostBall.setCenterY(ym);
            ghostBall.setRadius(10);

            guidelineToBall.setVisible(true);
            ghostBall.setVisible(true);
            guidelineFromBall.setVisible(false);
            guidelineFromCue.setVisible(false);

            if(SettingsSceneController.getSettingsSceneController().modeMenuIndex() == 0) {
                for(int i = 0; i < 16; i++) {
                    if(collides(ghostBall, ball[i])) {
    
                        guidelineFromBall.setVisible(true);
                        guidelineFromCue.setVisible(true);
                        
                        double angle = Math.atan2(ym - ycb, xm - xcb);
                        
                        Vector position = new Vector(ghostBall.getCenterX(), ghostBall.getCenterY());
                        double cueBallVelocity = 50;
                        Vector velocity = new Vector(cueBallVelocity * Math.cos(angle), cueBallVelocity * Math.sin(angle));

                        Vector normalToB = position.sub(ball[i].getPosition());
                        normalToB.normalize();
                        normalToB.multiply(velocity.scalar(normalToB));
                        Vector normalToA = ball[i].getPosition().sub(position);
                        normalToA.normalize();
                        normalToA.multiply(ball[i].getVelocity().scalar(normalToA));
                        Vector aCollisionVector = ball[i].getVelocity().sub(normalToA);
                        Vector ballFinalVelocity = aCollisionVector.add(normalToB);

                        // ORTHOGONAL VECTOR
                        Vector cueFinalVelocity = new Vector(0, 0);
                        if(xm > ball[i].getPosition().getX()) {
                            if(ym > ball[i].getPosition().getY()) {
                                cueFinalVelocity = new Vector(ballFinalVelocity.getY(), -ballFinalVelocity.getX());
                            } else {
                                cueFinalVelocity = new Vector(-ballFinalVelocity.getY(), ballFinalVelocity.getX());
                            }
                        } else {
                            if(ym > ball[i].getPosition().getY()) {
                                cueFinalVelocity = new Vector(-ballFinalVelocity.getY(), ballFinalVelocity.getX());
                            } else {
                                cueFinalVelocity = new Vector(ballFinalVelocity.getY(), -ballFinalVelocity.getX());
                            }
                        }

                        double x = ball[i].getSphere().getLayoutX();
                        double y = ball[i].getSphere().getLayoutY();
    
                        guidelineFromBall.setStartX(x);
                        guidelineFromBall.setStartY(y);
                        guidelineFromBall.setEndX(x + ballFinalVelocity.getX());
                        guidelineFromBall.setEndY(y + ballFinalVelocity.getY());
    
                        guidelineFromCue.setStartX(xm);
                        guidelineFromCue.setStartY(ym);
                        guidelineFromCue.setEndX(xm + cueFinalVelocity.getX());
                        guidelineFromCue.setEndY(ym + cueFinalVelocity.getY());
    
                    }
                }
            }

            cue.setVisible(true);
            cue.setLayoutX(xcb - 385);
            cue.setLayoutY(ycb - 20);
            double angle = Math.toDegrees(Math.atan2(ym - ycb, xm - xcb));
            cue.setRotate(angle);

            double mid_x = cue.getLayoutX() + cue.getFitWidth() / 2;
            double mid_y = cue.getLayoutY() + cue.getFitHeight() / 2;
            double dist = xcb - mid_x;

            double now_x = mid_x + (dist - dist*Math.cos(Math.toRadians(angle)));
            double now_y = mid_y + dist*Math.sin(Math.toRadians(-angle));
            
            double pos_x = now_x - cue.getFitWidth() / 2;
            double pos_y = now_y - cue.getFitHeight() / 2 + 2;

            cue.setLayoutX(pos_x);
            cue.setLayoutY(pos_y);

        }

    }

    @FXML
    public void fixTrajectory(MouseEvent event) {
        if(turn && !gamePause && !gameOver) {
            xmr = event.getSceneX();;
            ymr = event.getSceneY();
        }
    }

    @FXML
    public void cueLoading() {
        if(turn && !gamePause && !gameOver) {

            double xcb = ball[0].getPosition().getX();
            double ycb = ball[0].getPosition().getY();

            double velocity = Math.floor(powerSlider.getValue() / 30 * 100);

            sliderVelocityLabel.setText(String.valueOf(velocity));
            
            cue.setLayoutX(xcb - 385 - velocity);
            cue.setLayoutY(ycb - 20);
            
            double angle = Math.toDegrees(Math.atan2(ymr - ycb, xmr - xcb));

            double mid_x = cue.getLayoutX() + cue.getFitWidth() / 2;
            double mid_y = cue.getLayoutY() + cue.getFitHeight() / 2;
            double dist = ball[0].getPosition().getX() - mid_x;

            double now_x = mid_x + (dist - dist*Math.cos(Math.toRadians(angle)));
            double now_y = mid_y + dist*Math.sin(Math.toRadians(-angle));
            
            double pos_x = now_x - cue.getFitWidth() / 2;
            double pos_y = now_y - cue.getFitHeight() / 2 + 2;

            cue.setLayoutX(pos_x);
            cue.setLayoutY(pos_y);
        }
    }

    @FXML
    public void cueShot()  {
        double cueBallVelocity = 0;
        if(turn && !gamePause && !gameOver && xmr != -1 && ymr != -1) {

            cueBallVelocity = powerSlider.getValue();
            
            if(cueBallVelocity != 0) {
                
                guidelineToBall.setVisible(false);
                ghostBall.setVisible(false);
                guidelineFromBall.setVisible(false);
                guidelineFromCue.setVisible(false);
                
                double angle = Math.atan2(ymr - ball[0].getPosition().getY(), xmr - ball[0].getPosition().getX());
                setCueVelocity(cueBallVelocity * Math.cos(angle), cueBallVelocity * Math.sin(angle));

                xmr = -1;
                ymr = -1;

                cue.setVisible(false);
            }

        }
    }

// -------------------------------------------------- NOT FXML LINKED --------------------------------------------------

    private void update() {

        if(turnNum == 1) {
            playerBreaking();
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
            turnLabel();
        } else if(flag == 0 && turnChange) {
            foul = false;
            checkCases();
            checkAllPotted();

            if(isFoul() && !gameOver) {
                stopGame();
                showFoul();
                startFromPause();
            }

            turnChange = false;
            foulEight = false;
            foulWrongBallType = false;
            foulNoBallHit = false;
            turnNum++;
            turn = true;

            if(thisTurnPottedBalls.contains(Integer.valueOf(0))) {
                ball[0].setPosition(new Vector(Constants.HEAD_SPOT_X, Constants.HEAD_SPOT_Y));
                ball[0].getSphere().setVisible(false);
            }

            thisTurnPottedBalls.clear();

        }



    }

    private void playerBreaking() {
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

            if(turn && turnNum == 1) {
                cue.setVisible(false);
                guidelineToBall.setVisible(false);
                ghostBall.setVisible(false);
                guidelineFromBall.setVisible(false);
                guidelineFromCue.setVisible(false);
                ball[0].getSphere().setCursor(Cursor.CLOSED_HAND);
                if(event.getSceneX() >= Constants.LEFT_BANK && event.getSceneX() <= Constants.HEAD_SPOT_X && event.getSceneY() >= Constants.UP_BANK && event.getSceneY() <= Constants.DOWN_BANK) { // controllo che la palla venga posizionata nel rettangolo head spot
                    ball[0].setPosition(new Vector(event.getSceneX(), event.getSceneY()));
                }
            } else if (turn && foul) {
                cue.setVisible(false);
                guidelineToBall.setVisible(false);
                ghostBall.setVisible(false);
                guidelineFromBall.setVisible(false);
                guidelineFromCue.setVisible(false);
                ball[0].getSphere().setCursor(Cursor.CLOSED_HAND);
                if(event.getSceneX() >= Constants.LEFT_BANK && event.getSceneX() <= Constants.RIGHT_BANK && event.getSceneY() >= Constants.UP_BANK && event.getSceneY() <= Constants.DOWN_BANK) { // palla nel campo
                    ball[0].setPosition(new Vector(event.getSceneX(), event.getSceneY()));
                }
            }

        });

    }


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
                            foulEight = true; // first foul type
                        }
                    } else if (ballNum == 0 && !foulWrongBallType && player1.getBallType() != 0) {
                        foulWrongBallType = true;
                        if(player1.isMyTurn()) { // se è il turno di P1
                            if(player1.getBallType() != b.getBallType()) {
                                if(b.getBallNumber() == 8 && player1.isAllBallsPlotted()) {
                                    foulEight = false;
                                } else {
                                    foulEight = true;
                                }
                            }
                        } else { // se è di P2
                            if(player2.getBallType() != b.getBallType()) {
                                if(b.getBallNumber() == 8 && player2.isAllBallsPlotted()) {
                                    foulEight = false;
                                } else {
                                    foulEight = true;
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

        double stackY = Constants.RACKSTACK;
        ball[ballNum].setPosition(new Vector(1196, stackY)); // nello stack

        if(ballNum<=7 && ballNum!= 0){ //ScoreBoard update
            solidScoreBall[ballNum].setVisible(false);
        }
        else if(ballNum>=9 && ballNum!= 0){
            stripedScoreBall[ballNum - 9].setVisible(false);
        }

        stackY -= 25; //Se entra la bianca
        if (ballNum == 0) {
            stackY += 25;
            ball[0].getSphere ().setVisible (false);
            ball[0].setPosition (new Vector (0, 0));
            ball[0].setDropped (false);
        }
    }

    private void checkCases() {

        int flag = 0;

        if(turnNum == 1) {

            if(thisTurnPottedBalls.size() == 0) {
                flag = 1;
            } else {
                for(int i = 0; i < thisTurnPottedBalls.size(); i++) {
                    if(thisTurnPottedBalls.get(i).intValue() == 8) {
                        eightIn();
                    } else if (thisTurnPottedBalls.get(i).intValue() == 0) {
                        foul = true;
                        flag = 1;
                    } else {
                        potted[thisTurnPottedBalls.get(i).intValue()] = true;
                    }
                }
            }

        } else if(turnNum >= 2 && player1.getBallType() == 0) {

            if(thisTurnPottedBalls.size()== 0) {
                flag = 1;
            } else {
                int firstPuttedBallNum = thisTurnPottedBalls.get(0).intValue();
                if(firstPuttedBallNum >= 1 && firstPuttedBallNum <= 8) {
                    if(player1.isMyTurn()) {
                        player1.setBallType(1);
                        player2.setBallType(2);
                    } else {
                        player1.setBallType(2);
                        player2.setBallType(1);
                    }
                    ballAssignment();
                } else if(firstPuttedBallNum >= 9 && firstPuttedBallNum <= 15) {
                    if(player1.isMyTurn()) {
                        player1.setBallType(2);
                        player2.setBallType(1);
                    } else {
                        player1.setBallType(1);
                        player2.setBallType(2);
                    }
                    ballAssignment();
                }

                for(int i = 0; i < thisTurnPottedBalls.size(); i++) {
                    if(thisTurnPottedBalls.get(i).intValue() == 8) {
                        eightIn();
                    } else if (thisTurnPottedBalls.get(i).intValue() == 0) {
                        foul = true;
                        flag = 1;
                    } else {
                        potted[thisTurnPottedBalls.get(i).intValue()] = true;
                    }
                }

            }

        } else { // turno maggiore di 2 ma player1 non ha balltype 0 (balltype assegnata)

            if(thisTurnPottedBalls.size() == 0) {
                flag = 1;
            } else if(thisTurnPottedBalls.size() == 1 && thisTurnPottedBalls.get(0).intValue() == 8) {

                if(player1.isMyTurn()) {

                    if(player1.getBallType() == 1) {
                        int f = 0;
                        for(int i = 1; i<= 7; i++) {
                            if(!potted[i]) { // imbuco la nera con almeno una intera non imbucata
                                f = 1;
                                eightIn();
                            }
                        }
                        if(f == 0) {
                            win();
                        }
                    } else { // spezzate

                        int f = 0;
                        for(int i = 9; i<= 15; i++) {
                            if(!potted[i]) { // imbuco la nera con almeno una spezzata non imbucata
                                f = 1;
                                eightIn();
                            }
                        }
                        if(f == 0) {
                            win();
                        }

                    }

                } else {

                    if(player2.getBallType() == 1) {
                        int f = 0;
                        for(int i = 1; i<= 7; i++) {
                            if(!potted[i]) { // imbuco la nera con almeno una intera non imbucata
                                f = 1;
                                eightIn();
                            }
                        }
                        if(f == 0) {
                            win();
                        }
                    } else { // spezzate

                        int f = 0;
                        for(int i = 9; i<= 15; i++) {
                            if(!potted[i]) { // imbuco la nera con almeno una spezzata non imbucata
                                f = 1;
                                eightIn();
                            }
                        }
                        if(f == 0) {
                            win();
                        }

                    }

                }

            } else {

                int firstPuttedBallNum = thisTurnPottedBalls.get(0).intValue();

                if(player1.isMyTurn()) {

                    if(player1.getBallType() != ball[firstPuttedBallNum].getBallType()) {
                        flag = 1; // fallo
                    }

                    for(int i = 0; i < thisTurnPottedBalls.size(); i++) {
                        if(thisTurnPottedBalls.get(i).intValue() == 8) {
                            eightIn();
                        } else if(thisTurnPottedBalls.get(i).intValue() == 0) {
                            flag = 1;
                            foul = true;
                        } else {
                            potted[thisTurnPottedBalls.get(i).intValue()] = true;
                        }
                    }

                } else {

                    if(player2.getBallType() != ball[firstPuttedBallNum].getBallType()) {
                        flag = 1; // fallo
                    }

                    for(int i = 0; i < thisTurnPottedBalls.size(); i++) {
                        if(thisTurnPottedBalls.get(i).intValue() == 8) {
                            eightIn();
                        } else if(thisTurnPottedBalls.get(i).intValue() == 0) {
                            flag = 1;
                            foul = true;
                        } else {
                            potted[thisTurnPottedBalls.get(i).intValue()] = true;
                        }
                    }

                }

            }

        }

        if(flag == 1 || foulEight || foulNoBallHit || foulWrongBallType) {
            foul = true;
            changeTurn();
        }

    }

    private void showFoul() {
        if(thisTurnPottedBalls.contains(Integer.valueOf(0))) {
            scoreboardLabel.setText("Hai imbucato la bianca");

        } else if(foulWrongBallType || foulEight) {
            scoreboardLabel.setText("Hai colpito la palla sbagliata");
        } else {
            scoreboardLabel.setText("Devi colpire una palla");
        }

        if(player1.isMyTurn()) {
            scoreboardLabel.setText(player1.getNickname() + "ha la bianca in mano");
        } else {
            scoreboardLabel.setText(player2.getNickname() + "ha la bianca in mano");
        }
    }

    private void ballAssignment() {
        if(player1.getBallType() == 1) {
            scoreboardLabel.setText(player1.getNickname() + "ha le piene\n" + player2.getNickname() + "ha le spezzate");
            for(int i = 0; i < 7; i++) {
                solidScoreBall[i] = new ImageView(new Image("file:src/game/resources/ScoreBalls/Ball" + String.valueOf(i + 1) + ".png"));
                solidScoreBall[i].setFitHeight(30);
                solidScoreBall[i].setFitWidth(30);
                solidScoreBall[i].setLayoutX(Constants.RACK_LEFT + 40*i);
                solidScoreBall[i].setLayoutY(157);
                stripedScoreBall[i] = new ImageView(new Image("file:src/game/resources/ScoreBalls/Ball" + String.valueOf(i + 9) + ".png"));
                stripedScoreBall[i].setFitHeight(30);
                stripedScoreBall[i].setFitWidth(30);
                stripedScoreBall[i].setLayoutX(Constants.RACK_RIGHT + 40*i);
                stripedScoreBall[i].setLayoutY(157);
                pane.getChildren().addAll(solidScoreBall[i], stripedScoreBall[i]);
            }
        } else {
            scoreboardLabel.setText(player1.getNickname() + "ha le spezzate\n" + player2.getNickname() + "ha le piene");
            for(int i = 0; i < 7; i++) {
                solidScoreBall[i] = new ImageView(new Image("file:src/game/resources/ScoreBalls/Ball" + String.valueOf(i + 1) + ".png"));
                solidScoreBall[i].setFitHeight(30);
                solidScoreBall[i].setFitWidth(30);
                solidScoreBall[i].setLayoutX(Constants.RACK_RIGHT + 40*i);
                solidScoreBall[i].setLayoutY(157);
                stripedScoreBall[i] = new ImageView(new Image("file:src/game/resources/ScoreBalls/Ball" + String.valueOf(i + 9) + ".png"));
                stripedScoreBall[i].setFitHeight(30);
                stripedScoreBall[i].setFitWidth(30);
                stripedScoreBall[i].setLayoutX(Constants.RACK_LEFT + 40*i);
                stripedScoreBall[i].setLayoutY(157);
                pane.getChildren().addAll(solidScoreBall[i], stripedScoreBall[i]);
            }
        }
    }


    private void eightIn() {
        if(player1.isMyTurn()) {
            player2.setWin(true);
            player1.setWin(false);
            gameOver = true;
        } else {
            player2.setWin(false);
            player1.setWin(true);
            gameOver = true;
        }
    }

    private void win() {
        if(player1.isMyTurn()) {
            player2.setWin(false);
            player1.setWin(true);
            gameOver = true;
        } else {
            player2.setWin(true);
            player1.setWin(false);
            gameOver = true;
        }
    }


    private void checkAllPotted() {

        if(player1.getBallType() == 0) {
            return;
        } 

        if(player1.isMyTurn()) {
           
            int f = 0;
            if(player1.getBallType() == 1) {
                for(int i = 1; i<= 7; i++) {
                    if(!potted[i]) {
                        f = 1;
                        break;
                    }
                }
            } else {
                for(int i = 9; i<= 15; i++) {
                    if(!potted[i]) {
                        f = 1;
                        break;
                    }
                }
            }

            if(f==0) {
                player1.setAllBallsPlotted(true);
            }

        } else {

            int f = 0;
            if(player2.getBallType() == 1) {
                for(int i = 1; i<= 7; i++) {
                    if(!potted[i]) {
                        f = 1;
                        break;
                    }
                }
            } else {
                for(int i = 9; i<= 15; i++) {
                    if(!potted[i]) {
                        f = 1;
                        break;
                    }
                }
            }

            if(f==0) {
                player2.setAllBallsPlotted(true);
            }

        }

    }

    private void turnLabel() {
        if (player1.isMyTurn()) {
            scoreboardLabel.setText ("Turno di " + player1.getNickname());
        }
        else {
            scoreboardLabel.setText ("Turno di " + player2.getNickname());
        }
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

    public boolean isFoul() {
        return foul;
    }

    // small methods used by other methods

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

    public void setCueVelocity(double x, double y) {
        ball[0].setVelocity(x, y);
    }



}
