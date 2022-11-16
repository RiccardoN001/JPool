package game.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import game.controller.GameController;
import game.controller.Rules;
import game.utils.Constants;
import game.utils.Sounds;

public class Board {

    private static GameController game = GameController.getController();

    public static void showSplitPlayer() {
        int splitPlayer = (int)(Math.random()*2);
        if(splitPlayer == 0) {
            game.player1.setMyTurn(true);
        } else {
            game.player2.setMyTurn(true);
        }
    }

    public static void showPlayerNickname() {
        game.player1NicknameLabel.setText(game.player1.getNickname());
        game.player2NicknameLabel.setText(game.player2.getNickname());
    }
    
    public static void showPlayerBreaking() {
        if(game.player1.isMyTurn()) {
            game.turnboardLabel.setText("SPACCA " + game.player1.getNickname());
        } else {
            game.turnboardLabel.setText("SPACCA " + game.player2.getNickname());
        }
    }

    public static void showPlayerTurn() {
        if(game.player1.isMyTurn()) {
            game.turnboardLabel.setText("TURNO DI " + game.player1.getNickname());
        } else {
            game.turnboardLabel.setText("TURNO DI " + game.player2.getNickname());
        }
    }

    public static void showFoul() {
        if(game.foulWhite) {
            game.foulboardLabel.setText("BIANCA IMBUCATA");
        }
        if(game.foulWrongBallType) {
            game.foulboardLabel.setText("CATEGORIA ERRATA");
        }
        if(game.foulEight) {
            game.foulboardLabel.setText("CATEGORIA ERRATA (NERA)");
        }
        if(game.foulNoBallHit) {
            game.foulboardLabel.setText("NESSUNA BILIA COLPITA");
        }
        if(game.foulShotClock) {
            game.foulboardLabel.setText("TEMPO SCADUTO");
        }
    }

    public static void changeTurn() {

        if(!game.soundOff) {
            if(game.foul) {
                Sounds.playSound("FoulSound");
            } else {
                Sounds.playSound("TurnChangeSound");
            }
        }

        if(game.player1.isMyTurn()) {
            game.player1.setMyTurn(false);
            game.player2.setMyTurn(true);
        } else {
            game.player1.setMyTurn(true);
            game.player2.setMyTurn(false);
        }

    }

    public static void ballAssignment() {

        game.ballAssigned = true;

        if(!game.foul) {
            if(game.player1.getBallType() == 1) {
                for(int i = 0; i < 7; i++) {
                    game.solidScoreBall[i] = new ImageView(new Image("file:src/game/resources/ScoreBalls/Ball" + String.valueOf(i + 1) + ".png"));
                    game.solidScoreBall[i].setFitWidth(30);
                    game.solidScoreBall[i].setFitHeight(30);
                    game.solidScoreBall[i].setLayoutX(Constants.RACK_LEFT + 40*i);
                    game.solidScoreBall[i].setLayoutY(157);
                    game.stripedScoreBall[i] = new ImageView(new Image("file:src/game/resources/ScoreBalls/Ball" + String.valueOf(i + 9) + ".png"));
                    game.stripedScoreBall[i].setFitWidth(30);
                    game.stripedScoreBall[i].setFitHeight(30);
                    game.stripedScoreBall[i].setLayoutX(Constants.RACK_RIGHT + 40*i);
                    game.stripedScoreBall[i].setLayoutY(157);
                    game.addToPane(game.solidScoreBall[i]);
                    game.addToPane(game.stripedScoreBall[i]);
                }
            } else {
                for(int i = 0; i < 7; i++) {
                    game.solidScoreBall[i] = new ImageView(new Image("file:src/game/resources/ScoreBalls/Ball" + String.valueOf(i + 1) + ".png"));
                    game.solidScoreBall[i].setFitWidth(30);
                    game.solidScoreBall[i].setFitHeight(30);
                    game.solidScoreBall[i].setLayoutX(Constants.RACK_RIGHT + 40*i);
                    game.solidScoreBall[i].setLayoutY(157);
                    game.stripedScoreBall[i] = new ImageView(new Image("file:src/game/resources/ScoreBalls/Ball" + String.valueOf(i + 9) + ".png"));
                    game.stripedScoreBall[i].setFitWidth(30);
                    game.stripedScoreBall[i].setFitHeight(30);
                    game.stripedScoreBall[i].setLayoutX(Constants.RACK_LEFT + 40*i);
                    game.stripedScoreBall[i].setLayoutY(157);
                    game.addToPane(game.solidScoreBall[i]);
                    game.addToPane(game.stripedScoreBall[i]);
                }
            }
        }
        
    }

    public static void showEightPockets() {

        game.blackScoreBall = new ImageView(new Image("file:src/game/resources/ScoreBalls/Ball8.png"));
        game.blackScoreBall.setFitWidth(60);
        game.blackScoreBall.setFitHeight(60);
        game.blackScoreBall.setLayoutX(704-30);
        game.blackScoreBall.setLayoutY(172-30);
        game.blackScoreBall.setVisible(true);
        game.addToPane(game.blackScoreBall);

        game.pocket1.setStroke(Color.DARKGRAY);
        game.pocket1.setStrokeWidth(3);
        game.pocket2.setStroke(Color.DARKGRAY);
        game.pocket2.setStrokeWidth(3);
        game.pocket3.setStroke(Color.DARKGRAY);
        game.pocket3.setStrokeWidth(3);
        game.pocket4.setStroke(Color.DARKGRAY);
        game.pocket4.setStrokeWidth(3);
        game.pocket5.setStroke(Color.DARKGRAY);
        game.pocket5.setStrokeWidth(3);
        game.pocket6.setStroke(Color.DARKGRAY);
        game.pocket6.setStrokeWidth(3);

        game.pocketButton1.setVisible(true);
        game.pocketButton2.setVisible(true);
        game.pocketButton3.setVisible(true);
        game.pocketButton4.setVisible(true);
        game.pocketButton5.setVisible(true);
        game.pocketButton6.setVisible(true);

    }

    public static void removeEightPockets() {

        game.blackScoreBall.setVisible(false);

        game.pocket1.setStroke(Color.BLACK);
        game.pocket1.setStrokeWidth(1);
        game.pocket2.setStroke(Color.BLACK);
        game.pocket2.setStrokeWidth(1);
        game.pocket3.setStroke(Color.BLACK);
        game.pocket3.setStrokeWidth(1);
        game.pocket4.setStroke(Color.BLACK);
        game.pocket4.setStrokeWidth(1);
        game.pocket5.setStroke(Color.BLACK);
        game.pocket5.setStrokeWidth(1);
        game.pocket6.setStroke(Color.BLACK);
        game.pocket6.setStrokeWidth(1);

    }

    public static void eightPocketDeclaration() {
        game.pocketButton1.setOnAction(event -> {
            game.pocket1.setStroke(Color.GREEN);
            Rules.eightDeclaredPocket = 1;
            game.pocketButton1.setVisible(false);
            game.pocketButton2.setVisible(false);
            game.pocketButton3.setVisible(false);
            game.pocketButton4.setVisible(false);
            game.pocketButton5.setVisible(false);
            game.pocketButton6.setVisible(false);
        });
        game.pocketButton2.setOnAction(event -> {
            game.pocket2.setStroke(Color.GREEN);
            Rules.eightDeclaredPocket = 2;
            game.pocketButton1.setVisible(false);
            game.pocketButton2.setVisible(false);
            game.pocketButton3.setVisible(false);
            game.pocketButton4.setVisible(false);
            game.pocketButton5.setVisible(false);
            game.pocketButton6.setVisible(false);
        });
        game.pocketButton3.setOnAction(event -> {
            game.pocket3.setStroke(Color.GREEN);
            Rules.eightDeclaredPocket = 3;
            game.pocketButton1.setVisible(false);
            game.pocketButton2.setVisible(false);
            game.pocketButton3.setVisible(false);
            game.pocketButton4.setVisible(false);
            game.pocketButton5.setVisible(false);
            game.pocketButton6.setVisible(false);
        });
        game.pocketButton4.setOnAction(event -> {
            game.pocket4.setStroke(Color.GREEN);
            Rules.eightDeclaredPocket = 4;
            game.pocketButton1.setVisible(false);
            game.pocketButton2.setVisible(false);
            game.pocketButton3.setVisible(false);
            game.pocketButton4.setVisible(false);
            game.pocketButton5.setVisible(false);
            game.pocketButton6.setVisible(false);
        });
        game.pocketButton5.setOnAction(event -> {
            game.pocket5.setStroke(Color.GREEN);
            Rules.eightDeclaredPocket = 5;
            game.pocketButton1.setVisible(false);
            game.pocketButton2.setVisible(false);
            game.pocketButton3.setVisible(false);
            game.pocketButton4.setVisible(false);
            game.pocketButton5.setVisible(false);
            game.pocketButton6.setVisible(false);
        });
        game.pocketButton6.setOnAction(event -> {
            game.pocket6.setStroke(Color.GREEN);
            Rules.eightDeclaredPocket = 6;
            game.pocketButton1.setVisible(false);
            game.pocketButton2.setVisible(false);
            game.pocketButton3.setVisible(false);
            game.pocketButton4.setVisible(false);
            game.pocketButton5.setVisible(false);
            game.pocketButton6.setVisible(false);
        });
    }

    public static void win() {

        if(!game.soundOff) {
            Sounds.playSound("WinSound");
        }

        if(game.player1.isMyTurn()) {
            game.player1.setWin(true);
            game.player2.setWin(false);
            game.gameOver = true;
            game.turnboardLabel.setText("VINCE  " + game.player1.getNickname());
        } else {
            game.player1.setWin(false);
            game.player2.setWin(true);
            game.gameOver = true;
            game.turnboardLabel.setText("VINCE  " + game.player2.getNickname());
        }

    }

    public static void eightPotted() {

        if(!game.soundOff) {
            Sounds.playSound("WinSound");
        }

        if(game.player1.isMyTurn()) {
            game.player1.setWin(false);
            game.player2.setWin(true);
            game.gameOver = true;
            game.turnboardLabel.setText("VINCE  " + game.player2.getNickname());
        } else {
            game.player1.setWin(true);
            game.player2.setWin(false);
            game.gameOver = true;
            game.turnboardLabel.setText("VINCE  " + game.player1.getNickname());
        }

    }

}
