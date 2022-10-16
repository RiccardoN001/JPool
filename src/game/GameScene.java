package game;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class GameScene {

    private Group group;
    private Scene scene;
    private Parent root;
    
    static Ball ball[];

    private static Player player1, player2;
    private static boolean turn;
    private static boolean gamePause;
    private static boolean gameOver;

    private ImageView[] SolidScoreBall = new ImageView[7];
    private ImageView[] StripedScoreBall = new ImageView[7];
    private ImageView[] BlackScoreBall = new ImageView[2];

    // CONSTRUCTOR METHOD
    public GameScene(Group group, Scene scene, Parent root) {
        this.group = group;
        this.scene = scene;
        this.root = root;
        group.getChildren().add(root);

        // GAMEBALLS INITIALIZATION (SPLIT)
        // CUE BALL
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
        // ADD BALLS
        for(int i = 0; i < 16; i++) {
            group.getChildren().add(ball[i].DrawBall());
        }

        // SCOREBALLS INITIALIZATION
        for(int i = 0; i < 7; i++) {
            SolidScoreBall[i] = new ImageView("src/game/Resources/Ball" + String.valueOf(i + 1)  + ".png");
            StripedScoreBall[i] = new ImageView("src/game/Resources/Ball" + String.valueOf(i + 9)  + ".png");
            SolidScoreBall[i].setVisible(false);
            StripedScoreBall[i].setVisible(false);
            group.getChildren().addAll(SolidScoreBall[i], StripedScoreBall[i]);
    
        }
        for(int i = 0; i < 2; i++) {
            BlackScoreBall[i] = new ImageView("src/game/Resources/Ball8.png");
            BlackScoreBall[i].setVisible(false);
            group.getChildren().add(BlackScoreBall[i]);
        }
        
    }


    // GET/SET METHODS

    public static boolean isTurn() {
        return turn;
    }

    public static boolean isGameOver() {
        return gameOver;
    }

    public static boolean isGamePause() {
        return gamePause;
    }

    public static Player getPlayer1() {
        return player1;
    }

    public static Player getPlayer2() {
        return player2;
    }

    public static Ball getCueBall() {
        return ball[0];
    }    

}
