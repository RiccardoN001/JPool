package game.controller;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import game.Main;
import game.model.Ball;
import game.model.Player;
import game.model.Vector;
import game.utils.Constants;
import game.utils.Sounds;
import game.view.Board;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameController {

    // -------------------------------------------------- SCENE ATTRIBUTES --------------------------------------------------

    @FXML
    private Pane pane;

    @FXML
    private Button soundsButton;
    @FXML
    private Button menuButtonFromGame;
    @FXML
    private Label exitLabel;
    @FXML
    private Button exitYes;
    @FXML
    private Button exitNo;

    Rectangle blurredScene = new Rectangle(1400, 800, Color.WHITE);

    private boolean exit;

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
    private ImageView powerBar;
    @FXML
    private Label sliderVelocityLabel;

    private double xmr = -1;
    private double ymr = -1;

    public Ball ball[] = new Ball[16];
    public ImageView[] solidScoreBall = new ImageView[7];
    public ImageView[] stripedScoreBall = new ImageView[7];
    public ImageView blackScoreBall;

    @FXML
    public Label player1NicknameLabel;
    @FXML
    public Label player2NicknameLabel;
    @FXML
    public Label turnboardLabel;
    @FXML
    public Label foulboardLabel;
    @FXML
    public ProgressBar shotClockBar;

    private ImageView soundIconOff;
    private ImageView soundIconOn;

    public Player player1;
    public Player player2;

    public int turnNum;
    public boolean turn;
    public boolean foul;
    public boolean gamePause;
    public boolean gameOver;
    public boolean turnChange;
    public boolean foulWhite;
    public boolean foulWrongBallType;
    public boolean foulEight;
    public boolean foulNoBallHit;
    public boolean foulShotClock;
    public boolean guided;
    public boolean ballAssigned;
    public int cueBallCollisions;
    public boolean soundOff;
    public boolean shot;

    public ArrayList<Integer> thisTurnPottedBalls;
    public boolean potted[] = new boolean[16];
    private double stackY = 665;

    // threads
    private Timeline timeline = new Timeline();
    private Timer shotClock;

    @FXML
    public Circle pocket1;
    @FXML
    public Circle pocket2;
    @FXML
    public Circle pocket3;
    @FXML
    public Circle pocket4;
    @FXML
    public Circle pocket5;
    @FXML
    public Circle pocket6;

    @FXML
    public Button pocketButton1;
    @FXML
    public Button pocketButton2;
    @FXML
    public Button pocketButton3;
    @FXML
    public Button pocketButton4;
    @FXML
    public Button pocketButton5;
    @FXML
    public Button pocketButton6;

    public int eightPocket = 0;


    // CONTROLLER-CONTROLLER COMMUNICATION
    private static GameController instance;
    public GameController() {
        instance = this;
    }
    public static GameController getController() {
        return instance;
    }

    // -------------------------------------------------- SCENE METHODS --------------------------------------------------

    @FXML
    public void handleMenuButton(ActionEvent event) throws Exception {
        if(!soundOff){
          Sounds.playSound("PauseSound");  
        }
        exitLabel.setVisible(true);
        exitYes.setVisible(true);
        exitNo.setVisible(true);
        timeline.stop();
        powerSlider.setDisable(true);
        exit = true;
        blurredScene.setOpacity(0.5);
        pane.getChildren().add(blurredScene);
        exitLabel.toFront();
        exitYes.toFront();
        exitNo.toFront();
    }

    @FXML
    public void handleExitYesButton(ActionEvent event) throws Exception {
        Stage stage;
        Scene scene;
        Parent root;
        stage = (Stage) exitYes.getScene().getWindow();
        root = FXMLLoader.load(Main.class.getResource("view/Menu.fxml"));
        scene = new Scene(root);
        scene.getStylesheets().addAll(Main.class.getResource("view/style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void handleExitNoButton(ActionEvent event) throws Exception {
        exitLabel.setVisible(false);
        exitYes.setVisible(false);
        exitNo.setVisible(false);
        timeline.play();
        powerSlider.setDisable(false);
        exit = false;
        pane.getChildren().remove(blurredScene);
    }

    @FXML
    public void handleSoundsButton(ActionEvent event) throws Exception {
        if(!soundOff){
            soundOff = true;
            soundsButton.setGraphic(soundIconOff);
            soundsButton.setPrefSize(60, 60);
        }
        else if(soundOff){
            soundOff = false;
            soundsButton.setGraphic(soundIconOn);
            soundsButton.setPrefSize(60, 60);
        }
    }

    // -------------------------------------------------- ANIMATION METHODS --------------------------------------------------

    public void startGame() {
        KeyFrame keyFrame = new KeyFrame (
                Duration.seconds(0.015),
                event -> {
                    if(!gameOver) {
                        update();
                    } else {
                        timeline.stop();
                    }
                });
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void pauseGame() {
        gamePause = true;
        timeline.stop();
    }

    public void startFromPause() {
        gamePause = false;
        timeline.play();
    }

    // -------------------------------------------------- SHOTCLOCK METHODS --------------------------------------------------

    public void startShotClock() {
        shotClock = new Timer();
        TimerTask task = new TimerTask() {
            double countdown = 30;
            @Override
            public void run() {
                if(countdown > 0) {
                    shotClockBar.setProgress(countdown / 30);
                    countdown -= 0.001;
                    if(shot || gameOver) {
                        shotClock.cancel();
                    }
                } else {
                    turnChange = true;
                    foulShotClock = true;
                    shotClock.cancel();
                }
            }
        };
        shotClock.scheduleAtFixedRate(task, 1500, 1);
    }

    public void stopShotClock() {
        shotClock.cancel();
        shotClockBar.setProgress(0);
    }

    // -------------------------------------------------- GAME METHODS --------------------------------------------------

    // -------------------------------------------------- FXML LINKED --------------------------------------------------

    @FXML
    public void initialize() throws Exception {

        Sounds.playSound("RackSound");
        
        // SPLIT
        turnNum = 1;
        Ball.triangle(ball);
        for(int i = 0; i < 16; i++) {
            pane.getChildren().add(ball[i].drawBall());
        }

        // CUE LOADING
        cue = new ImageView(new Image("file:src/game/resources/Cues/Cue" + String.valueOf(SettingsController.getController().cueMenuIndex()+1 + ".png")));
        cue.setFitWidth(400);
        cue.setFitHeight(100);
        cue.setLayoutX(105);
        cue.setLayoutY(447);
        cue.setPreserveRatio(true);
        pane.getChildren().add(cue);
        powerBar.setOpacity(0.3);

        // PLAYERS
        if(SettingsController.getController().getP1Nickname().isEmpty()) {
            player1 = new Player("Giocatore 1");
        } else {
            player1 = new Player(SettingsController.getController().getP1Nickname());
        }
        if(SettingsController.getController().getP2Nickname().isEmpty()) {
            player2 = new Player("Giocatore 2");
        } else {
            player2 = new Player(SettingsController.getController().getP2Nickname());
        }
        Board.showPlayerNickname();

        // CONTROL VARIABLES
        player1.setMyTurn(true);
        turn = true;
        foul = false;
        gamePause = false;
        gameOver = false;
        turnChange = false;
        foulWhite = false;
        foulWrongBallType = false;
        foulEight = false;
        foulNoBallHit = true;
        foulShotClock = false;
        guided = false;
        ballAssigned = false;
        cueBallCollisions = 0;
        shot = false;

        // POTTED BALLS
        thisTurnPottedBalls = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            potted[i] = false;
        }

        // PAUSE
        exitLabel.setVisible(false);
        exitYes.setVisible(false);
        exitNo.setVisible(false);
        exit = false;

        //SOUNDS
        soundOff = false;

        soundIconOn = new ImageView(new Image("file:src/game/resources/Sounds/SpeakerOn.png"));
        soundIconOn.setFitWidth(60);
        soundIconOn.setFitHeight(60);
        soundIconOff = new ImageView(new Image("file:src/game/resources/Sounds/SpeakerOff.png"));
        soundIconOff.setFitWidth(60);
        soundIconOff.setFitHeight(60);

        soundsButton.setGraphic(soundIconOn);
        soundsButton.setPrefSize(60, 60);

        blackScoreBall = new ImageView(new Image("file:src/game/resources/ScoreBalls/Ball8.png"));
        blackScoreBall.setFitWidth(60);
        blackScoreBall.setFitHeight(60);
        blackScoreBall.setLayoutX(704-30);
        blackScoreBall.setLayoutY(172-30);
        pane.getChildren().add(blackScoreBall);
        blackScoreBall.setVisible(false);

        startShotClock();

        startGame();
    }

    @FXML
    public void guidedTrajectory(MouseEvent event) {

        double xm = event.getSceneX();
        double ym = event.getSceneY();

        if(turn && !gamePause && !gameOver && xm >= Constants.A_MARGIN+10 && xm <= Constants.B_MARGIN-10 && ym >= Constants.CD_MARGIN+10 && ym <= Constants.EF_MARGIN-10 && !exit) {

            guided = true;

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

            if(SettingsController.getController().modeMenuIndex() == 0) {
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
        if(turn && !gamePause && !gameOver && !exit) {
            xmr = event.getSceneX();
            ymr = event.getSceneY();
        }
    }

    @FXML
    public void cueLoading() {
        if(turn && !gamePause && !gameOver && !exit && guided) {

            double xcb = ball[0].getPosition().getX();
            double ycb = ball[0].getPosition().getY();

            double velocity = Math.floor(powerSlider.getValue() / 17 * 100);

            sliderVelocityLabel.setText(String.valueOf((int)velocity) + "%");

            powerBar.setOpacity(0.3 + Math.floor(powerSlider.getValue()) / 17 * 0.7);
            
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
        if(turn && !gamePause && !gameOver && xmr != -1 && ymr != -1 && !exit) {

            shot = true;

            if(!soundOff){
                Sounds.playSound("CueSound");
            }

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

                powerBar.setOpacity(0.3);
            }

        }
        powerSlider.setValue(0);
        sliderVelocityLabel.setText(0 +"%");
    }

// -------------------------------------------------- NOT FXML LINKED --------------------------------------------------

    private void update() {

        if(turnNum == 1 && !foulShotClock) {
            Board.showPlayerBreaking();
        }

        moveCueBall();

        boolean ballsMoving = false;
        for(int i = 0; i < 16; i++) {
            if(!ball[i].getVelocity().isNull()) {
                ballsMoving = true;
                turnChange = true;
            }
            updateBalls(i);
            checkPocket(i);
        }

        if(ballsMoving) {
            turn = false;
            foulboardLabel.setText("");
        } else if(!ballsMoving && !turnChange) {
            turn = true;
            Board.showPlayerTurn();
        } else if(!ballsMoving && turnChange) {

            Board.removeEightPockets();

            foul = false;

            if(!gamePause && !gameOver) {
                startShotClock();
            }
            
            if(foulShotClock) {
                foulNoBallHit = false;
            }

            //System.out.println(eightPocket);
            //System.out.println(eightDeclaredPocket + "\n");

            Rules.checkFoul();
            Rules.checkPotted();

            if(thisTurnPottedBalls.contains(Integer.valueOf(0))) {
                foulNoBallHit = false;
            }

            if(foul && !gameOver) {
                Board.showFoul();
            }

            turn = true;
            turnNum++;
            
            turnChange = false;
            foulWhite = false;
            foulWrongBallType = false;
            foulEight = false;
            foulNoBallHit = true;
            foulShotClock = false;
            cueBallCollisions = 0;
            shot = false;

            if(thisTurnPottedBalls.contains(Integer.valueOf(0))) {
                ball[0].setPosition(new Vector(Constants.HEAD_SPOT_X, Constants.HEAD_SPOT_Y));
                ball[0].getSphere().setVisible(true);
            }
            
            if(ballAssigned) {
                for(int i=1;i<=7;i++){
                    if(potted[i]){
                        solidScoreBall[i-1].setVisible(false);
                    }
                }
                for(int i=9;i<=15;i++){
                    if(potted[i]){
                        stripedScoreBall[i-9].setVisible(false);
                    }
                }
            }

            if(player1.isAllBallsPlotted() && player1.isMyTurn() && !gameOver) {
                Board.showEightPockets();
                Board.eightPocketDeclaration();
                //System.out.println("Ciao G1");
            }
    
            if(player2.isAllBallsPlotted() && player2.isMyTurn() && !gameOver) {
                Board.showEightPockets();
                Board.eightPocketDeclaration();
                //System.out.println("Ciao G2");
            }

            thisTurnPottedBalls.clear();

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
                if(event.getSceneX() >= Constants.A_MARGIN+12.5 && 
                    event.getSceneX() <= Constants.HEAD_SPOT_X && 
                    event.getSceneY() >= Constants.CD_MARGIN+12.5 && 
                    event.getSceneY() <= Constants.EF_MARGIN-12.5) {
                    ball[0].setPosition(new Vector(event.getSceneX(), event.getSceneY()));
                }
            } else if(turn && foul) {
                cue.setVisible(false);
                guidelineToBall.setVisible(false);
                ghostBall.setVisible(false);
                guidelineFromBall.setVisible(false);
                guidelineFromCue.setVisible(false);
                ball[0].getSphere().setCursor(Cursor.CLOSED_HAND);
                if(event.getSceneX() >= Constants.A_MARGIN+12.5 && 
                    event.getSceneX() <= Constants.B_MARGIN-12.5 && 
                    event.getSceneY() >= Constants.CD_MARGIN+12.5 && 
                    event.getSceneY() <= Constants.EF_MARGIN-12.5) {
                    ball[0].setPosition(new Vector(event.getSceneX(), event.getSceneY()));
                }
            }

        });

    }

    private void updateBalls(int ballNum) {
        if(ball[ballNum].getVelocity().getSize() <= 8e-2) {
            ball[ballNum].setVelocity(0, 0);  
        } else {
            ball[ballNum].getPosition().setX(ball[ballNum].getPosition().getX() + ball[ballNum].getVelocity().getX());
            ball[ballNum].getPosition().setY(ball[ballNum].getPosition().getY() + ball[ballNum].getVelocity().getY());

            for(int i = 0; i < 16; i++) {
                if(ball[ballNum].collides(ball[i]) && ballNum != ball[i].getBallNumber()) {

                    if(ballNum == 0) {
                        foulNoBallHit = false;
                        cueBallCollisions++;
                    }

                    if(ballNum == 0 && cueBallCollisions == 1 && turnNum==1 && !soundOff){
                        Sounds.playSound("SplitSound");
                    }
                    else if(turnNum != 1 && !soundOff){
                        Sounds.playSound("BallSound");
                    }

                    if(ballNum == 0 && player1.getBallType() == 0) {
                        if(ball[i].getBallNumber() == 8) {
                            foulEight = true;
                        }
                    }

                    if (ballNum == 0 && player1.getBallType() != 0) {
                        if(player1.isMyTurn()) {
                            if(player1.getBallType() != ball[i].getBallType() && cueBallCollisions==1) {
                                foulWrongBallType = true;
                                if(ball[i].getBallNumber() == 8 && !player1.isAllBallsPlotted()) {
                                    foulEight = true;
                                } else if(ball[i].getBallNumber() == 8 && player1.isAllBallsPlotted()) {
                                    foulEight = false;
                                    foulWrongBallType = false;
                                }
                            } else if(player1.getBallType() == ball[i].getBallType() && cueBallCollisions==1) {
                                foulWrongBallType = false;
                            }
                        } else {
                            if(player2.getBallType() != ball[i].getBallType() && cueBallCollisions==1) {
                                foulWrongBallType = true;
                                if(ball[i].getBallNumber() == 8 && !player2.isAllBallsPlotted()) {
                                    foulEight = true;
                                } else if(ball[i].getBallNumber() == 8 && player2.isAllBallsPlotted()) {
                                    foulEight = false;
                                    foulWrongBallType = false;
                                }
                            } else if(player2.getBallType() == ball[i].getBallType() && cueBallCollisions==1) {
                                foulWrongBallType = false;
                            }
                        }
                    }

                    ball[ballNum].getPosition().setX(ball[ballNum].getPosition().getX() - ball[ballNum].getVelocity().getX());
                    ball[ballNum].getPosition().setY(ball[ballNum].getPosition().getY() - ball[ballNum].getVelocity().getY());
                    ball[ballNum].ballCollision(ball[i]);
                    
                    break;

                }
            }
            ball[ballNum].spin();
            ball[ballNum].bankCollision();
            ball[ballNum].tableFriction();
        }
        ball[ballNum].getSphere().setLayoutX(ball[ballNum].getPosition().getX());
        ball[ballNum].getSphere().setLayoutY(ball[ballNum].getPosition().getY());
    }


    private void checkPocket(int ballNum) {

        double x = ball[ballNum].getPosition().getX();
        double y = ball[ballNum].getPosition().getY();

        double check = 25;

        if (distance(x, y, Constants.TOP_LEFT_POCKET_X, Constants.TOP_LEFT_POCKET_Y) <= check
            || ((y <= 244+15 || x <= 290+15) && !ball[ballNum].isDropped ())) {
            dropit (ballNum);
            if(ballNum == 8) {
                eightPocket = 1;
            }
        }
        else if (distance(x, y, Constants.BOTTOM_LEFT_POCKET_X, Constants.BOTTOM_LEFT_POCKET_Y) <= check
            || ((y >= 700-15 || x <= 290+15) && !ball[ballNum].isDropped ())) {
            dropit (ballNum);
            if(ballNum == 8) {
                eightPocket = 4;
            }
        }
        else if (distance(x, y, Constants.TOP_MIDDLE_POCKET_X, Constants.TOP_MIDDLE_POCKET_Y) <= check-5) {
            dropit (ballNum);
            if(ballNum == 8) {
                eightPocket = 2;
            }
        }
        else if (distance(x, y, Constants.BOTTOM_MIDDLE_POCKET_X, Constants.BOTTOM_MIDDLE_POCKET_Y) <= check-5) {
            dropit (ballNum);
            if(ballNum == 8) {
                eightPocket = 5;
            }
        }
        else if (distance(x, y, Constants.TOP_RIGHT_POCKET_X, Constants.TOP_RIGHT_POCKET_Y) <= check
            || ((y <= 244+15 || x >= 1174-15) && !ball[ballNum].isDropped ())) {
            dropit (ballNum);
            if(ballNum == 8) {
                eightPocket = 3;
            }
        }
        else if (distance(x, y, Constants.BOTTOM_RIGHT_POCKET_X, Constants.BOTTOM_RIGHT_POCKET_Y) <= check
            || ((y >= 700-15 || x >= 1174-15) && !ball[ballNum].isDropped ())) {
            dropit (ballNum);
            if(ballNum == 8) {
                eightPocket = 6;
            }
        }

    }

    private boolean collides(Circle circle, Ball ball) {
        double x = circle.getCenterX() - ball.getPosition().getX();
        double y = circle.getCenterY() - ball.getPosition().getY();
        double centersdistance = Math.sqrt(x * x + y * y);

        if (centersdistance - Constants.BALL_DIAMETER <= 3) {
            return true;
        } else {
            return false;
        }
    }

    public void setCueVelocity(double x, double y) {
        ball[0].setVelocity(x, y);
    }

    private double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1 - x2)*(x1 - x2) + (y1 - y2)*(y1 - y2));
    }

    private void dropit(int ballNum) {

        if(!soundOff && ballNum != 0){
            Sounds.playSound("PocketSound");
        }
        thisTurnPottedBalls.add(Integer.valueOf(ballNum));
        ball[ballNum].setDropped(true);
        ball[ballNum].setVelocity(0, 0);

        ball[ballNum].setPosition(new Vector(Constants.RACKSTACK_X, stackY));

        stackY -= 25;

        if (ballNum == 0) {
            stackY += 25;
            ball[0].getSphere ().setVisible (false);
            ball[0].setPosition (new Vector (0, 0));
            ball[0].setDropped (false);
        }
    }

    public void addToPane(Node node1, Node node2) {
        pane.getChildren().addAll(node1, node2);
    }

}
