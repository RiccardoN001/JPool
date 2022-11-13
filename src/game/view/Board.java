package game.view;

import java.util.ArrayList;

import game.controller.GameSceneController;
import game.model.Player;
import javafx.scene.image.ImageView;

public class Board {

    private static Player player1 = GameSceneController.getGameSceneController().getPlayer1();
    private static Player player2 = GameSceneController.getGameSceneController().getPlayer2();

    public static void showPlayerNickname() {
        GameSceneController.getGameSceneController().setPlayer1NicknameLabelText(player1.getNickname());
        GameSceneController.getGameSceneController().setPlayer1NicknameLabelText(player2.getNickname());
    }
    
    public static void showPlayerBreaking() {
        if(player1.isMyTurn()) {
            GameSceneController.getGameSceneController().setTurnboardLabelText("SPACCA " + player1.getNickname());
        } else {
            GameSceneController.getGameSceneController().setTurnboardLabelText("SPACCA " + player2.getNickname());
        }
    }

    public static void showPlayerTurn() {
        if(player1.isMyTurn()) {
            GameSceneController.getGameSceneController().setTurnboardLabelText("TURNO DI  " + player1.getNickname());
        } else {
            GameSceneController.getGameSceneController().setTurnboardLabelText("TURNO DI  " + player2.getNickname());
        }
    }

    public static void showFoul() {
        if(GameSceneController.getGameSceneController().isFoulWhite()) {
            GameSceneController.getGameSceneController().setFoulboardLabelText("BIANCA IMBUCATA");
        }
        if(GameSceneController.getGameSceneController().isFoulWrongBallType()) {
            GameSceneController.getGameSceneController().setFoulboardLabelText("CATEGORIA ERRATA");
        }
        if(GameSceneController.getGameSceneController().isFoulEight()) {
            GameSceneController.getGameSceneController().setFoulboardLabelText("CATEGORIA ERRATA (NERA)");
        }
        if(GameSceneController.getGameSceneController().isFoulNoBallHit()) {
            GameSceneController.getGameSceneController().setFoulboardLabelText("NESSUNA BILIA COLPITA");
        }
        if(GameSceneController.getGameSceneController().isFoulShotClock()) {
            GameSceneController.getGameSceneController().setFoulboardLabelText("TEMPO SCADUTO");
        }
    }

}
