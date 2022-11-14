package game.view;

import java.util.ArrayList;

import game.controller.GameController;
import game.model.Player;

public class Board {

    private static Player player1 = GameController.getGameSceneController().getPlayer1();
    private static Player player2 = GameController.getGameSceneController().getPlayer2();

    public static void showPlayerNickname() {
        GameController.getGameSceneController().setPlayer1NicknameLabelText(player1.getNickname());
        GameController.getGameSceneController().setPlayer2NicknameLabelText(player2.getNickname());
    }
    
    public static void showPlayerBreaking() {
        if(player1.isMyTurn()) {
            GameController.getGameSceneController().setTurnboardLabelText("SPACCA " + player1.getNickname());
        } else {
            GameController.getGameSceneController().setTurnboardLabelText("SPACCA " + player2.getNickname());
        }
    }

    public static void showPlayerTurn() {
        if(player1.isMyTurn()) {
            GameController.getGameSceneController().setTurnboardLabelText("TURNO DI  " + player1.getNickname());
        } else {
            GameController.getGameSceneController().setTurnboardLabelText("TURNO DI  " + player2.getNickname());
        }
    }

    public static void showFoul() {
        if(GameController.getGameSceneController().isFoulWhite()) {
            GameController.getGameSceneController().setFoulboardLabelText("BIANCA IMBUCATA");
        }
        if(GameController.getGameSceneController().isFoulWrongBallType()) {
            GameController.getGameSceneController().setFoulboardLabelText("CATEGORIA ERRATA");
        }
        if(GameController.getGameSceneController().isFoulEight()) {
            GameController.getGameSceneController().setFoulboardLabelText("CATEGORIA ERRATA (NERA)");
        }
        if(GameController.getGameSceneController().isFoulNoBallHit()) {
            GameController.getGameSceneController().setFoulboardLabelText("NESSUNA BILIA COLPITA");
        }
        if(GameController.getGameSceneController().isFoulShotClock()) {
            GameController.getGameSceneController().setFoulboardLabelText("TEMPO SCADUTO");
        }
    }

}
