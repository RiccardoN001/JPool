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

    private Ball ball[] = new Ball[16];
    private ImageView[] solidScoreBall = new ImageView[7];
    private ImageView[] stripedScoreBall = new ImageView[7];
    private ImageView blackScoreBall;

    @FXML
    private Label player1NicknameLabel;
    @FXML
    private Label player2NicknameLabel;
    @FXML
    private Label turnboardLabel;
    @FXML
    private Label foulboardLabel;
    @FXML
    private ProgressBar shotClockBar;

    private ImageView soundIconOff;
    private ImageView soundIconOn;

    private Player player1;
    private Player player2;

    private int turnNum;
    private boolean turn;
    private boolean foul;
    private boolean gamePause;
    private boolean gameOver;
    private boolean turnChange;
    private boolean foulWhite;
    private boolean foulWrongBallType;
    private boolean foulEight;
    private boolean foulNoBallHit;
    private boolean foulShotClock;
    private boolean guided;
    private boolean ballAssigned;
    private int cueBallCollisions;
    private boolean soundOff;
    private int eightPocket;
    private int eightDeclaredPocket;
    private boolean shot;

    private ArrayList<Integer> thisTurnPottedBalls;
    private boolean potted[] = new boolean[16];
    private double stackY = 665;

    // threads
    private Timeline timeline = new Timeline();
    private Timer shotClock;

    @FXML
    private Circle pocket1;
    @FXML
    private Circle pocket2;
    @FXML
    private Circle pocket3;
    @FXML
    private Circle pocket4;
    @FXML
    private Circle pocket5;
    @FXML
    private Circle pocket6;

    @FXML
    private Button pocketButton1;
    @FXML
    private Button pocketButton2;
    @FXML
    private Button pocketButton3;
    @FXML
    private Button pocketButton4;
    @FXML
    private Button pocketButton5;
    @FXML
    private Button pocketButton6;


    // CONTROLLER-CONTROLLER COMMUNICATION
    private static GameController instance;
    public GameController() {
        instance = this;
    }
    public static GameController getGameSceneController() {
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
                    if(shot) {
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
        cue = new ImageView(new Image("file:src/game/resources/Cues/Cue" + String.valueOf(SettingsController.getSettingsSceneController().cueMenuIndex()+1 + ".png")));
        cue.setFitWidth(400);
        cue.setFitHeight(100);
        cue.setLayoutX(105);
        cue.setLayoutY(447);
        cue.setPreserveRatio(true);
        pane.getChildren().add(cue);
        powerBar.setOpacity(0.3);

        // PLAYERS
        if(SettingsController.getSettingsSceneController().getP1Nickname().isEmpty()) {
            player1 = new Player("Giocatore 1");
        } else {
            player1 = new Player(SettingsController.getSettingsSceneController().getP1Nickname());
        }
        if(SettingsController.getSettingsSceneController().getP2Nickname().isEmpty()) {
            player2 = new Player("Giocatore 2");
        } else {
            player2 = new Player(SettingsController.getSettingsSceneController().getP2Nickname());
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
        eightPocket = 0;
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

            if(SettingsController.getSettingsSceneController().modeMenuIndex() == 0) {
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

            removeEightPockets();

            foul = false;

            if(!gamePause && !gameOver) {
                startShotClock();
            }
            
            if(foulShotClock) {
                foulNoBallHit = false;
            }

            //System.out.println(eightPocket);
            //System.out.println(eightDeclaredPocket + "\n");

            checkCases();
            checkAllPotted();

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
                showEightPockets();
                eightPocketDeclaration();
                //System.out.println("Ciao G1");
            }
    
            if(player2.isAllBallsPlotted() && player2.isMyTurn() && !gameOver) {
                showEightPockets();
                eightPocketDeclaration();
                //System.out.println("Ciao G2");
            }

            thisTurnPottedBalls.clear();

        }

    }

    private void showEightPockets() {

        blackScoreBall.setVisible(true);

        pocket1.setStroke(Color.DARKGRAY);
        pocket1.setStrokeWidth(3);
        pocket2.setStroke(Color.DARKGRAY);
        pocket2.setStrokeWidth(3);
        pocket3.setStroke(Color.DARKGRAY);
        pocket3.setStrokeWidth(3);
        pocket4.setStroke(Color.DARKGRAY);
        pocket4.setStrokeWidth(3);
        pocket5.setStroke(Color.DARKGRAY);
        pocket5.setStrokeWidth(3);
        pocket6.setStroke(Color.DARKGRAY);
        pocket6.setStrokeWidth(3);

        pocketButton1.setVisible(true);
        pocketButton2.setVisible(true);
        pocketButton3.setVisible(true);
        pocketButton4.setVisible(true);
        pocketButton5.setVisible(true);
        pocketButton6.setVisible(true);

    }

    private void removeEightPockets() {

        blackScoreBall.setVisible(false);

        pocket1.setStroke(Color.BLACK);
        pocket1.setStrokeWidth(1);
        pocket2.setStroke(Color.BLACK);
        pocket2.setStrokeWidth(1);
        pocket3.setStroke(Color.BLACK);
        pocket3.setStrokeWidth(1);
        pocket4.setStroke(Color.BLACK);
        pocket4.setStrokeWidth(1);
        pocket5.setStroke(Color.BLACK);
        pocket5.setStrokeWidth(1);
        pocket6.setStroke(Color.BLACK);
        pocket6.setStrokeWidth(1);

    }

    private void eightPocketDeclaration() {
        pocketButton1.setOnAction(event -> {
            pocket1.setStroke(Color.GREEN);
            eightDeclaredPocket = 1;
            pocketButton1.setVisible(false);
            pocketButton2.setVisible(false);
            pocketButton3.setVisible(false);
            pocketButton4.setVisible(false);
            pocketButton5.setVisible(false);
            pocketButton6.setVisible(false);
        });
        pocketButton2.setOnAction(event -> {
            pocket2.setStroke(Color.GREEN);
            eightDeclaredPocket = 2;
            pocketButton1.setVisible(false);
            pocketButton2.setVisible(false);
            pocketButton3.setVisible(false);
            pocketButton4.setVisible(false);
            pocketButton5.setVisible(false);
            pocketButton6.setVisible(false);
        });
        pocketButton3.setOnAction(event -> {
            pocket3.setStroke(Color.GREEN);
            eightDeclaredPocket = 3;
            pocketButton1.setVisible(false);
            pocketButton2.setVisible(false);
            pocketButton3.setVisible(false);
            pocketButton4.setVisible(false);
            pocketButton5.setVisible(false);
            pocketButton6.setVisible(false);
        });
        pocketButton4.setOnAction(event -> {
            pocket4.setStroke(Color.GREEN);
            eightDeclaredPocket = 4;
            pocketButton1.setVisible(false);
            pocketButton2.setVisible(false);
            pocketButton3.setVisible(false);
            pocketButton4.setVisible(false);
            pocketButton5.setVisible(false);
            pocketButton6.setVisible(false);
        });
        pocketButton5.setOnAction(event -> {
            pocket5.setStroke(Color.GREEN);
            eightDeclaredPocket = 5;
            pocketButton1.setVisible(false);
            pocketButton2.setVisible(false);
            pocketButton3.setVisible(false);
            pocketButton4.setVisible(false);
            pocketButton5.setVisible(false);
            pocketButton6.setVisible(false);
        });
        pocketButton6.setOnAction(event -> {
            pocket6.setStroke(Color.GREEN);
            eightDeclaredPocket = 6;
            pocketButton1.setVisible(false);
            pocketButton2.setVisible(false);
            pocketButton3.setVisible(false);
            pocketButton4.setVisible(false);
            pocketButton5.setVisible(false);
            pocketButton6.setVisible(false);
        });
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

    private void checkCases() {

        boolean change = false;

        if(turnNum == 1) {

            if(thisTurnPottedBalls.size() == 0) {
                change = true;
            } else {
                for(int i = 0; i < thisTurnPottedBalls.size(); i++) {
                    if(thisTurnPottedBalls.get(i).intValue() == 8) {
                        eightIn();
                    } else if (thisTurnPottedBalls.get(i).intValue() == 0) {
                        foulWhite = true;
                    } else {
                        potted[thisTurnPottedBalls.get(i).intValue()] = true;
                    }
                }
            }

        } else if(turnNum >= 2 && player1.getBallType() == 0) {

            if(thisTurnPottedBalls.size()== 0) {
                change = true;
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
                        foulWhite = true;
                    } else {
                        potted[thisTurnPottedBalls.get(i).intValue()] = true;
                    }
                }

            }

        } else {

            if(thisTurnPottedBalls.size() == 0) {
                change = true;
            } else if(thisTurnPottedBalls.size() == 1 && thisTurnPottedBalls.get(0).intValue() == 8) {

                if(player1.isMyTurn()) {

                    if(player1.getBallType() == 1) {
                        int f = 0;
                        for(int i = 1; i<= 7; i++) {
                            if(!potted[i]) {
                                f = 1;
                                eightIn();
                            }
                        }
                        if(f == 0 && eightPocket == eightDeclaredPocket) {
                            win();
                            return;
                        } else if(f == 0 && eightPocket != eightDeclaredPocket) {
                            //System.out.println("Buca errata dichiarata");
                            eightIn();
                            return;
                        }
                    } else {

                        int f = 0;
                        for(int i = 9; i<= 15; i++) {
                            if(!potted[i]) {
                                f = 1;
                                eightIn();
                            }
                        }
                        if(f == 0 && eightPocket == eightDeclaredPocket) {
                            win();
                            return;
                        } else if(f == 0 && eightPocket != eightDeclaredPocket) {
                            //System.out.println("Buca errata dichiarata");
                            eightIn();
                            return; 
                        }

                    }

                } else {

                    if(player2.getBallType() == 1) {
                        int f = 0;
                        for(int i = 1; i<= 7; i++) {
                            if(!potted[i]) {
                                f = 1;
                                eightIn();
                            }
                        }
                        if(f == 0 && eightPocket == eightDeclaredPocket) {
                            win();
                            return;
                        } else if(f == 0 && eightPocket != eightDeclaredPocket) {
                            //System.out.println("Buca errata dichiarata");
                            eightIn();
                            return;
                        }
                    } else {

                        int f = 0;
                        for(int i = 9; i<= 15; i++) {
                            if(!potted[i]) {
                                f = 1;
                                eightIn();
                            }
                        }
                        if(f == 0 && eightPocket == eightDeclaredPocket) {
                            win();
                            return;
                        } else if(f == 0 && eightPocket != eightDeclaredPocket) {
                            //System.out.println("Buca errata dichiarata");
                            eightIn();
                            return;
                        }

                    }

                }

            } else {

                int firstPuttedBallNum = thisTurnPottedBalls.get(0).intValue();

                if(player1.isMyTurn()) {

                    if(player1.getBallType() != ball[firstPuttedBallNum].getBallType()) {
                        change = true;
                    }

                    for(int i = 0; i < thisTurnPottedBalls.size(); i++) {
                        if(thisTurnPottedBalls.get(i).intValue() == 8) {
                            eightIn();
                        } else if(thisTurnPottedBalls.get(i).intValue() == 0) {
                            foulWhite = true;
                        } else {
                            potted[thisTurnPottedBalls.get(i).intValue()] = true;
                        }
                    }

                } else {

                    if(player2.getBallType() != ball[firstPuttedBallNum].getBallType()) {
                        change = true;
                    }

                    for(int i = 0; i < thisTurnPottedBalls.size(); i++) {
                        if(thisTurnPottedBalls.get(i).intValue() == 8) {
                            eightIn();
                        } else if(thisTurnPottedBalls.get(i).intValue() == 0) {
                            foulWhite = true;
                        } else {
                            potted[thisTurnPottedBalls.get(i).intValue()] = true;
                        }
                    }

                }

            }

        }

        if(foulWhite || foulWrongBallType || foulEight || foulNoBallHit || foulShotClock) {
            foul = true;
        }

        if(change || foulWhite || foulWrongBallType || foulEight || foulNoBallHit || foulShotClock) {
            changeTurn();
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

    private void ballAssignment() {

        ballAssigned = true;

        if(!foul) {
            if(player1.getBallType() == 1) {
                turnboardLabel.setText(player1.getNickname() + "ha le piene\n" + player2.getNickname() + "ha le spezzate");
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
                turnboardLabel.setText(player1.getNickname() + "ha le spezzate\n" + player2.getNickname() + "ha le piene");
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
        
    }

    private void eightIn() {
        if(!soundOff){
            Sounds.playSound("WinSound");
        }
        stopShotClock();
        if(player1.isMyTurn()) {
            player2.setWin(true);
            player1.setWin(false);
            gameOver = true;
            turnboardLabel.setText("Vince " + player2.getNickname());
        } else {
            player2.setWin(false);
            player1.setWin(true);
            gameOver = true;
            turnboardLabel.setText("Vince " + player1.getNickname());
        }
    }

    private void win() {
        if(!soundOff){
            Sounds.playSound("WinSound");
        }
        stopShotClock();
        if(player1.isMyTurn()) {
            player2.setWin(false);
            player1.setWin(true);
            gameOver = true;
            turnboardLabel.setText("Vince " + player1.getNickname());
        } else {
            player2.setWin(true);
            player1.setWin(false);
            gameOver = true;
            turnboardLabel.setText("Vince " + player2.getNickname());
        }
    }

    private void changeTurn() {
        if(!soundOff){
            if(foul){
                Sounds.playSound("FoulSound");
            }else{
                Sounds.playSound("TurnChangeSound");
            }
        }

        if (player1.isMyTurn ()) {
            player1.setMyTurn (false);
            player2.setMyTurn (true);
        }
        else {
            player2.setMyTurn (false);
            player1.setMyTurn (true);
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

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setTurnboardLabelText(String turnboardText) {
        turnboardLabel.setText(turnboardText);
    }

    public void setFoulboardLabelText(String foulboardText) {
        foulboardLabel.setText(foulboardText);
    }

    public void setPlayer1NicknameLabelText(String player1NicknameText) {
        player1NicknameLabel.setText(player1NicknameText);
    }

    public void setPlayer2NicknameLabelText(String player2NicknameText) {
        player2NicknameLabel.setText(player2NicknameText);
    }

    public ArrayList<Integer> getThisTurnPottedBalls() {
        return thisTurnPottedBalls;
    }

    public boolean isFoulWhite() {
        return foulWhite;
    }

    public boolean isFoulEight() {
        return foulEight;
    }

    public boolean isFoulWrongBallType() {
        return foulWrongBallType;
    }

    public boolean isFoulNoBallHit() {
        return foulNoBallHit;
    }

    public boolean isFoulShotClock() {
        return foulShotClock;
    }

    public ImageView[] getSolidScoreBall() {
        return solidScoreBall;
    }

    public ImageView[] getStripedScoreBall() {
        return stripedScoreBall;
    }

    public void setBallAssigned(boolean ballAssigned) {
        this.ballAssigned = ballAssigned;
    }

    public boolean isFoul() {
        return foul;
    }

    public void addToPane(Node node) {
        pane.getChildren().add(node);
    }

    


}