package game.controller;

// BRIEF CLASS DESCRIPTION
// Controls the animations to play the game, containing all needed objects and calling all needed methods

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import game.Main;
import game.model.Ball;
import game.model.Player;
import game.model.Vector;
import game.utils.Constants;
import game.utils.Sounds;
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

    // -------------------------------------------------- GUI --------------------------------------------------

    @FXML
    private Pane pane;
    @FXML
    private Button menuButtonFromGame;
    @FXML
    private Button soundsButton;
    private ImageView soundIconOn;
    private ImageView soundIconOff;
    public boolean soundOff;

    Rectangle blurredScene = new Rectangle(1400, 800, Color.WHITE);
    @FXML
    private Label exitLabel;
    @FXML
    private Button exitYes;
    @FXML
    private Button exitNo;
    private boolean exit;

    // -------------------------------------------------- THREAD --------------------------------------------------

    private Timeline timeline = new Timeline();
    private Timer shotClock;
    private boolean startShotClock;

    // -------------------------------------------------- GAME --------------------------------------------------

    public Player player1;
    public Player player2;
    public Ball ball[] = new Ball[16];

    public ImageView cue;
    @FXML
    public Line guidelineToBall;
    @FXML
    public Circle ghostBall;
    @FXML
    public Line guidelineFromBall;
    @FXML
    public Line guidelineFromCue;
    @FXML
    
    private Slider powerSlider;
    @FXML
    private ImageView powerBar;
    @FXML
    private Label sliderVelocityLabel;
    private double xMouseReleased = -1;
    private double yMouseReleased = -1;

    @FXML
    public Label player1NicknameLabel;
    @FXML
    public Label player2NicknameLabel;
    @FXML
    public Label turnboardLabel;
    @FXML
    public Label foulboardLabel;
    @FXML
    public Label centralboardLabel;
    @FXML
    public ProgressBar shotClockBar;
    public ImageView[] solidScoreBall = new ImageView[7];
    public ImageView[] stripedScoreBall = new ImageView[7];
    public ImageView blackScoreBall = new ImageView();

    public ArrayList<Integer> thisTurnPottedBalls;
    public boolean potted[] = new boolean[16];
    public double rackStack = 665;

    public int turnNum;
    public boolean turn;

    public boolean guided;
    public boolean shot;
    public boolean ballAssigned;

    public boolean foul;
    public boolean foulWhite;
    public boolean foulWrongBallType;
    public boolean foulEight;
    public boolean foulNoBallHit;
    public boolean foulShotClock;
    public int cueBallCollisions;

    public boolean turnChange;
    public boolean gameOver;

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
    public int eightPocket;

    // -------------------------------------------------- COMMUNICATION --------------------------------------------------

    private Ball ballInstance;
    private Rules rules;
    private Board board;
    private SettingsController settings = SettingsController.getController();

    private static GameController instance;
    public GameController() {
        instance = this;
    }
    public static GameController getController() {
        return instance;
    }

    // -------------------------------------------------- GUI METHODS --------------------------------------------------

    @FXML
    public void handleMenuButton(ActionEvent event) throws Exception {

        if(!soundOff) {
          Sounds.playSound("PauseSound");  
        }

        exitLabel.setVisible(true);
        exitYes.setVisible(true);
        exitNo.setVisible(true);
        exit = true;

        timeline.stop();
        powerSlider.setDisable(true);

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
        stopShotClock();
    }

    @FXML
    public void handleExitNoButton(ActionEvent event) throws Exception {

        exitLabel.setVisible(false);
        exitYes.setVisible(false);
        exitNo.setVisible(false);
        exit = false;

        timeline.play();
        powerSlider.setDisable(false);

        pane.getChildren().remove(blurredScene);

    }

    @FXML
    public void handleSoundsButton(ActionEvent event) throws Exception {
        if(!soundOff) {
            soundsButton.setGraphic(soundIconOff);
            soundsButton.setPrefSize(60, 60);
            soundOff = true;
        } else {
            soundsButton.setGraphic(soundIconOn);
            soundsButton.setPrefSize(60, 60);
            soundOff = false;
        }
    }

    // -------------------------------------------------- THREAD METHODS --------------------------------------------------

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

    public void startShotClock() {
        shotClock = new Timer();
        TimerTask task = new TimerTask() {
            double countdown = 30;
            @Override
            public void run() {
                if(countdown > 0) {
                    shotClockBar.setProgress(countdown / 30);
                    countdown -= 0.001;
                    if(Math.floor(countdown) == 3 && startShotClock) {
                        if(!soundOff) {
                            Sounds.playSound("ShotClockSound");
                        }
                        startShotClock = false;
                    }
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

    @FXML
    public void initialize() throws Exception {

        ballInstance = new Ball();
        rules = new Rules();
        board = new Board();

        // PLAYERS
        if(settings.getP1Nickname().isEmpty()) {
            player1 = new Player("Giocatore 1");
        } else {
            player1 = new Player(settings.getP1Nickname());
        }
        if(settings.getP2Nickname().isEmpty()) {
            player2 = new Player("Giocatore 2");
        } else {
            player2 = new Player(settings.getP2Nickname());
        }
        board.showPlayerNickname();
        if(settings.getSplitPlayer() == 0) {
            player1.setMyTurn(true);
        } else {
            player2.setMyTurn(true);
        }

        // CUE
        cue = new ImageView(new Image("file:src/game/resources/Cues/Cue" + String.valueOf(settings.cueMenuIndex()+1 + ".png")));
        cue.setFitWidth(400);
        cue.setFitHeight(100);
        cue.setLayoutX(105);
        cue.setLayoutY(447);
        cue.setPreserveRatio(true);
        pane.getChildren().add(cue);
        powerBar.setOpacity(0.3);
        
        // SPLIT
        turnNum = 1;
        Ball.triangle(ball);
        for(int i = 0; i < 16; i++) {
            pane.getChildren().add(ball[i].drawBall());
        }

        turn = true;
        guided = false;
        shot = false;
        ballAssigned = false;
        foul = false;
        foulWhite = false;
        foulWrongBallType = false;
        foulEight = false;
        foulNoBallHit = true;
        foulShotClock = false;
        cueBallCollisions = 0;
        turnChange = false;
        gameOver = false;

        thisTurnPottedBalls = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            potted[i] = false;
        }

        centralboardLabel.setVisible(false);

        exitLabel.setVisible(false);
        exitYes.setVisible(false);
        exitNo.setVisible(false);
        exit = false;

        // SOUNDS
        soundOff = settings.getSoundOff();
        if(!soundOff){
            Sounds.playSound("RackSound");
         }
        soundIconOn = new ImageView(new Image("file:src/game/resources/Sounds/SpeakerOn.png"));
        soundIconOn.setFitWidth(60);
        soundIconOn.setFitHeight(60);
        soundIconOff = new ImageView(new Image("file:src/game/resources/Sounds/SpeakerOff.png"));
        soundIconOff.setFitWidth(60);
        soundIconOff.setFitHeight(60);

        if(!soundOff){
          soundsButton.setGraphic(soundIconOn);
          soundsButton.setPrefSize(60, 60);  
        }
        else if(soundOff){
            soundsButton.setGraphic(soundIconOff);
            soundsButton.setPrefSize(60, 60); 
        }

        // THREAD
        startGame();
        startShotClock();
        startShotClock = true;

    }

    @FXML
    public void guidedTrajectory(MouseEvent event) {

        double xm = event.getSceneX();
        double ym = event.getSceneY();

        if(turn && !gameOver && !exit && xm >= Constants.A_MARGIN+10 && xm <= Constants.B_MARGIN-10 && ym >= Constants.CD_MARGIN+10 && ym <= Constants.EF_MARGIN-10) {

            guided = true;

            Vector cueBallPosition = new Vector(ball[0].getPosition().getX(), ball[0].getPosition().getY());
            Vector ghostBallPosition = new Vector(xm, ym);

            guidelineToBall.setStartX(cueBallPosition.getX());
            guidelineToBall.setStartY(cueBallPosition.getY());
            guidelineToBall.setEndX(xm);
            guidelineToBall.setEndY(ym);
            guidelineToBall.setStroke(Color.WHITE);
            guidelineToBall.setVisible(true);

            ghostBall.setCenterX(xm);
            ghostBall.setCenterY(ym);
            ghostBall.setRadius(10);
            ghostBall.setStroke(Color.WHITE);
            ghostBall.setFill(Color.TRANSPARENT);
            ghostBall.setVisible(true);

            guidelineFromBall.setStroke(Color.WHITE);
            guidelineFromBall.setVisible(false);

            guidelineFromCue.setStroke(Color.WHITE);
            guidelineFromCue.setVisible(false);

            if(settings.modeMenuIndex() == 0) {
                for(int i = 0; i < 16; i++) {
                    if(ballInstance.ghostCollides(ghostBall, ball[i])) {

                        double cueBallVelocity = 50;

                        Vector ballPosition = new Vector(ball[i].getPosition().getX(), ball[i].getPosition().getY());
                        
                        double angle = Math.atan2(ym - cueBallPosition.getY(), xm - cueBallPosition.getX());
                        Vector velocity = new Vector(cueBallVelocity * Math.cos(angle), cueBallVelocity * Math.sin(angle));

                        Vector n1 = ghostBallPosition.sub(ball[i].getPosition());
                        n1.normalize(); // un1
                        double v1n = velocity.scalar(n1);
                        n1.multiply(v1n); // v1n (vector)
                        Vector n2 = ball[i].getPosition().sub(ghostBallPosition);
                        n2.normalize(); // un2
                        double v2n = ball[i].getVelocity().scalar(n2);
                        n2.multiply(v2n); // v2n (vector)
                        Vector v2f = ball[i].getVelocity().sub(n2);
                        Vector ballFinalVelocity = v2f.add(n1);

                        Vector cueFinalVelocity = new Vector(0, 0);
                        double d = ghostBallPosition.determinant(cueBallPosition, ballPosition);
                        if(d > 0) {
                            cueFinalVelocity = ballFinalVelocity.perpendicularLeft();
                        } else {
                            cueFinalVelocity = ballFinalVelocity.perpendicularRight();
                        }

                        double x = ball[i].getSphere().getLayoutX();
                        double y = ball[i].getSphere().getLayoutY();
    
                        guidelineFromBall.setStartX(x);
                        guidelineFromBall.setStartY(y);
                        guidelineFromBall.setEndX(x + ballFinalVelocity.getX());
                        guidelineFromBall.setEndY(y + ballFinalVelocity.getY());
                        guidelineFromBall.setVisible(true);
    
                        guidelineFromCue.setStartX(xm);
                        guidelineFromCue.setStartY(ym);
                        guidelineFromCue.setEndX(xm + cueFinalVelocity.getX());
                        guidelineFromCue.setEndY(ym + cueFinalVelocity.getY());
                        guidelineFromCue.setVisible(true);
    
                    }
                }
            }

            cue.setVisible(true);
            cue.setLayoutX(cueBallPosition.getX() - 385);
            cue.setLayoutY(cueBallPosition.getY() - 20);
            double angle = Math.toDegrees(Math.atan2(ym - cueBallPosition.getY(), xm - cueBallPosition.getX()));
            cue.setRotate(angle);

            double midX = cue.getLayoutX() + cue.getFitWidth() / 2;
            double midY = cue.getLayoutY() + cue.getFitHeight() / 2;
            double dist = cueBallPosition.getX() - midX;

            double rotX = midX + (dist - dist*Math.cos(Math.toRadians(angle)));
            double rotY = midY + dist*Math.sin(Math.toRadians(-angle));
            
            double finalX = rotX - cue.getFitWidth() / 2;
            double finalY = rotY - cue.getFitHeight() / 2 + 2;

            cue.setLayoutX(finalX);
            cue.setLayoutY(finalY);

        }

    }

    @FXML
    public void fixTrajectory(MouseEvent event) {
        if(turn && !gameOver && !exit) {
            xMouseReleased = event.getSceneX();
            yMouseReleased = event.getSceneY();
        }
    }

    @FXML
    public void cueLoading() {
        if(turn && guided && !gameOver && !exit) {

            Vector cueBallPosition = new Vector(ball[0].getPosition().getX(), ball[0].getPosition().getY());

            double velocity = Math.floor(powerSlider.getValue() / 15 * 100);
            sliderVelocityLabel.setText(String.valueOf((int)velocity) + "%");
            powerBar.setOpacity(0.3 + Math.floor(powerSlider.getValue()) / 15 * 0.7);
            
            cue.setLayoutX(cueBallPosition.getX() - 385 - velocity);
            cue.setLayoutY(cueBallPosition.getY() - 20);
            
            double angle = Math.toDegrees(Math.atan2(yMouseReleased - cueBallPosition.getY(), xMouseReleased - cueBallPosition.getX()));

            double midX = cue.getLayoutX() + cue.getFitWidth() / 2;
            double midY = cue.getLayoutY() + cue.getFitHeight() / 2;
            double dist = cueBallPosition.getX() - midX;

            double rotX = midX + (dist - dist*Math.cos(Math.toRadians(angle)));
            double rotY = midY + dist*Math.sin(Math.toRadians(-angle));
            
            double finalX = rotX - cue.getFitWidth() / 2;
            double finalY = rotY - cue.getFitHeight() / 2 + 2;

            cue.setLayoutX(finalX);
            cue.setLayoutY(finalY);

        }
    }

    @FXML
    public void cueShot()  {
        double cueBallVelocity = 0;
        if(turn && !gameOver && !exit && xMouseReleased != -1 && yMouseReleased != -1) {

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
                
                double angle = Math.atan2(yMouseReleased - ball[0].getPosition().getY(), xMouseReleased - ball[0].getPosition().getX());
                ballInstance.setCueVelocity(cueBallVelocity * Math.cos(angle), cueBallVelocity * Math.sin(angle));

                xMouseReleased = -1;
                yMouseReleased = -1;

                cue.setVisible(false);

                powerBar.setOpacity(0.3);
            }

        }
        powerSlider.setValue(0);
        sliderVelocityLabel.setText(0 + "%");
    }

    private void update() {

        if(turnNum == 1 && !foulShotClock) {
            board.showPlayerBreaking();
        }

        ballInstance.moveCueBall();

        boolean ballsMoving = false;
        for(int i = 0; i < 16; i++) {
            if(!ball[i].getVelocity().isNull()) {
                ballsMoving = true;
                turnChange = true;
            }
            ballInstance.ballAnimation(i);
            ballInstance.checkPocket(i);
        }

        if(ballsMoving) {
            turn = false;
            foulboardLabel.setText("");
        } else if(!ballsMoving && !turnChange) {
            turn = true;
            board.showPlayerTurn();
        } else if(!ballsMoving && turnChange) {

            ball[0].getSphere().setCursor(Cursor.DEFAULT);

            board.removeEightPockets();

            foul = false;

            if(!gameOver) {
                startShotClock = true;
                startShotClock();
            }
            
            if(foulShotClock) {
                foulNoBallHit = false;
            }

            rules.checkFoul();
            rules.checkPotted();

            if(thisTurnPottedBalls.contains(Integer.valueOf(0))) {
                foulNoBallHit = false;
            }

            if(foul && !gameOver) {
                board.showFoul();
            }

            turn = true;
            turnNum++;
            guided = false;
            shot = false;
            foulWhite = false;
            foulWrongBallType = false;
            foulEight = false;
            foulNoBallHit = true;
            foulShotClock = false;
            cueBallCollisions = 0;
            turnChange = false;
            gameOver = false;

            if(thisTurnPottedBalls.contains(Integer.valueOf(0))) {
                ball[0].setPosition(new Vector(Constants.HEAD_SPOT_X, Constants.HEAD_SPOT_Y));
                ball[0].getSphere().setVisible(true);
            }
            
            if(ballAssigned) {
                for(int i = 1; i <=7 ; i++) {
                    if(potted[i]) {
                        solidScoreBall[i - 1].setVisible(false);
                    }
                }
                for(int i = 9; i <= 15; i++) {
                    if(potted[i]) {
                        stripedScoreBall[i - 9].setVisible(false);
                    }
                }
            }

            if(player1.isAllBallsPlotted() && player1.isMyTurn() && !gameOver) {
                board.showEightPockets();
                board.eightPocketDeclaration();
            }
    
            if(player2.isAllBallsPlotted() && player2.isMyTurn() && !gameOver) {
                board.showEightPockets();
                board.eightPocketDeclaration();
            }

            thisTurnPottedBalls.clear();

        }

    }

    public void addToPane(Node node) {
        pane.getChildren().add(node);
    }

}
