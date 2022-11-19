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
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
    private Button menuButtonFromGame;
    @FXML
    private Button soundsButton;
    @FXML
    private Label exitLabel;
    @FXML
    private Button exitYes;
    @FXML
    private Button exitNo;
    Rectangle blurredScene = new Rectangle(1400, 800, Color.WHITE);
    private boolean exit;
    private ImageView soundIconOff;
    private ImageView soundIconOn;

    // -------------------------------------------------- GAME ATTRIBUTES --------------------------------------------------

    public Player player1;
    public Player player2;
    public static Ball ball[] = new Ball[16];

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
    public Label winLabel;
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
    public boolean foul;
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


    private Board board;
    private Rules rules;
    private Ball ballMethods;



    // -------------------------------------------------- THREAD ATTRIBUTES --------------------------------------------------

    private Timeline timeline = new Timeline();
    private Timer shotClock;
    private boolean startShotClock;

    // -------------------------------------------------- CONTROLLER COMMUNICATION --------------------------------------------------

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
        if(!soundOff) {
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
        if(!soundOff) {
            soundOff = true;
            soundsButton.setGraphic(soundIconOff);
            soundsButton.setPrefSize(60, 60);
        } else if(soundOff) {
            soundOff = false;
            soundsButton.setGraphic(soundIconOn);
            soundsButton.setPrefSize(60, 60);
        }
    }

    public void shutdown(){
        shotClock.cancel();
        //Anche i suoni
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
                    if(Math.floor(countdown) == 3 && startShotClock) {
                        if(!soundOff){
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
        soundOff = SettingsController.getController().getSoundOff();//oppure direttamente alla dichiarazione della variabile
        System.out.println("GameScene Init " +soundOff);

        board = new Board();
        rules = new Rules();
        ballMethods = new Ball();

        // PLAYERS
        if(SettingsController.getController().getP1Nickname().isEmpty()) {
            player1 = new Player("Giocatore 1");
        } else {
            player1 = new Player(SettingsController.getController().getPlayerNick1());
        }
        if(SettingsController.getController().getP2Nickname().isEmpty()) {
            player2 = new Player("Giocatore 2");
        } else {
            player2 = new Player(SettingsController.getController().getPlayerNick2());
        }
        board.showPlayerNickname();
        board.showSplitPlayer();

        // CUE LOADING
        cue = new ImageView(new Image("file:src/game/resources/Cues/Cue" + String.valueOf(SettingsController.getController().cueMenuIndex()+1 + ".png")));
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
        if(!soundOff){
           Sounds.playSound("RackSound");
        }
       

        // RULES VARIABLES
        turn = true;
        foul = false;
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

        // SCENE VARIABLES
        exitLabel.setVisible(false);
        exitYes.setVisible(false);
        exitNo.setVisible(false);
        exit = false;
        winLabel.setVisible(false);

        //SOUNDS
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
        startShotClock = true;
        startShotClock();
        startGame();
    }

    @FXML
    public void guidedTrajectory(MouseEvent event) {

        double xm = event.getSceneX();
        double ym = event.getSceneY();

        if(turn && !gameOver && xm >= Constants.A_MARGIN+10 && xm <= Constants.B_MARGIN-10 && ym >= Constants.CD_MARGIN+10 && ym <= Constants.EF_MARGIN-10 && !exit) {

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
                    if(ballMethods.ghostCollides(ghostBall, ball[i])) {
    
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
                        if(xm > ball[i].getPosition().getX() && ym > ball[i].getPosition().getY() && xcb > ball[i].getPosition().getX()) {
                            cueFinalVelocity = new Vector(ballFinalVelocity.getY(), -ballFinalVelocity.getX());
                        } else if(xm > ball[i].getPosition().getX() && ym > ball[i].getPosition().getY() && xcb < ball[i].getPosition().getX()) {
                            cueFinalVelocity = new Vector(-ballFinalVelocity.getY(), ballFinalVelocity.getX());
                        } else if(xm > ball[i].getPosition().getX() && ym < ball[i].getPosition().getY() && xcb > ball[i].getPosition().getX()) {
                            cueFinalVelocity = new Vector(-ballFinalVelocity.getY(), ballFinalVelocity.getX());
                        } else if(xm > ball[i].getPosition().getX() && ym < ball[i].getPosition().getY() && xcb < ball[i].getPosition().getX()) {
                            cueFinalVelocity = new Vector(ballFinalVelocity.getY(), -ballFinalVelocity.getX());
                        } else if(xm < ball[i].getPosition().getX() && ym > ball[i].getPosition().getY() && xcb < ball[i].getPosition().getX()) {
                            cueFinalVelocity = new Vector(-ballFinalVelocity.getY(), ballFinalVelocity.getX());
                        } else if(xm < ball[i].getPosition().getX() && ym > ball[i].getPosition().getY() && xcb > ball[i].getPosition().getX()) {
                            cueFinalVelocity = new Vector(ballFinalVelocity.getY(), -ballFinalVelocity.getX());
                        } else if(xm < ball[i].getPosition().getX() && ym < ball[i].getPosition().getY() && xcb < ball[i].getPosition().getX()) {
                            cueFinalVelocity = new Vector(ballFinalVelocity.getY(), -ballFinalVelocity.getX());
                        } else if(xm < ball[i].getPosition().getX() && ym < ball[i].getPosition().getY() && xcb > ball[i].getPosition().getX()) {
                            cueFinalVelocity = new Vector(ballFinalVelocity.getY(), -ballFinalVelocity.getX());
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
        if(turn && !gameOver && !exit) {
            xMouseReleased = event.getSceneX();
            yMouseReleased = event.getSceneY();
        }
    }

    @FXML
    public void cueLoading() {
        if(turn && !gameOver && !exit && guided) {

            double xcb = ball[0].getPosition().getX();
            double ycb = ball[0].getPosition().getY();

            double velocity = Math.floor(powerSlider.getValue() / 17 * 100);

            sliderVelocityLabel.setText(String.valueOf((int)velocity) + "%");

            powerBar.setOpacity(0.3 + Math.floor(powerSlider.getValue()) / 17 * 0.7);
            
            cue.setLayoutX(xcb - 385 - velocity);
            cue.setLayoutY(ycb - 20);
            
            double angle = Math.toDegrees(Math.atan2(yMouseReleased - ycb, xMouseReleased - xcb));

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
        if(turn && !gameOver && xMouseReleased != -1 && yMouseReleased != -1 && !exit) {

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
                Ball.setCueVelocity(cueBallVelocity * Math.cos(angle), cueBallVelocity * Math.sin(angle));

                xMouseReleased = -1;
                yMouseReleased = -1;

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
            board.showPlayerBreaking();
        }

        ballMethods.moveCueBall();

        boolean ballsMoving = false;
        for(int i = 0; i < 16; i++) {
            if(!ball[i].getVelocity().isNull()) {
                ballsMoving = true;
                turnChange = true;
            }
            ballMethods.ballAnimation(i);
            ballMethods.checkPocket(i);
        }

        if(ballsMoving) {
            turn = false;
            foulboardLabel.setText("");
        } else if(!ballsMoving && !turnChange) {
            turn = true;
            board.showPlayerTurn();
        } else if(!ballsMoving && turnChange) {

            board.removeEightPockets();

            foul = false;

            if(!gameOver) {
                startShotClock = true;
                startShotClock();
            }
            
            if(foulShotClock) {
                foulNoBallHit = false;
            }

            //System.out.println(eightPocket);
            //System.out.println(eightDeclaredPocket + "\n");

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
                board.showEightPockets();
                board.eightPocketDeclaration();
                //System.out.println("Ciao G1");
            }
    
            if(player2.isAllBallsPlotted() && player2.isMyTurn() && !gameOver) {
                board.showEightPockets();
                board.eightPocketDeclaration();
                //System.out.println("Ciao G2");
            }

            thisTurnPottedBalls.clear();

        }

    }

    public void addToPane(Node node) {
        pane.getChildren().add(node);
    }

}
