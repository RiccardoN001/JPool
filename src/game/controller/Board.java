package game.controller;

import java.util.Timer;
import java.util.TimerTask;

// BRIEF CLASS DESCRIPTION 
// Visual Referee: updates the View with graphics to direct the match

import game.utils.Constants;
import game.utils.Sounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Board {

    private GameController game = GameController.getController();

    public void showPlayerNickname() {
        game.player1NicknameLabel.setText(game.player1.getNickname());
        game.player2NicknameLabel.setText(game.player2.getNickname());
    }
    
    public void showPlayerBreaking() {
        if(game.player1.isMyTurn()) {
            game.turnboardLabel.setText("SPACCA " + game.player1.getNickname());
        } else {
            game.turnboardLabel.setText("SPACCA " + game.player2.getNickname());
        }
    }

    public void showPlayerTurn() {
        if(game.player1.isMyTurn()) {
            game.turnboardLabel.setText("TURNO DI " + game.player1.getNickname());
        } else {
            game.turnboardLabel.setText("TURNO DI " + game.player2.getNickname());
        }
    }

    public void showFoul() {
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

    public void changeTurn() {

        if(!game.soundOff && !game.gameOver) {
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

    public void ballAssignment() {

        if(!game.foul) {

            game.ballAssigned = true;
            game.centralboardLabel.setVisible(true);
            game.centralboardLabel.toFront();

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
                if(game.player1.isMyTurn()) {
                    game.centralboardLabel.setText(game.player1.getNickname() + " \nHA LE PIENE");
                } else {
                    game.centralboardLabel.setText(game.player2.getNickname() + " \nHA LE SPEZZATE");
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
                if(game.player1.isMyTurn()) {
                    game.centralboardLabel.setText(game.player1.getNickname() + " \nHA LE SPEZZATE");
                } else {
                    game.centralboardLabel.setText(game.player2.getNickname() + " \nHA LE PIENE");
                }
            }

            Timer popup = new Timer();
            TimerTask task = new TimerTask() {
                double countdown = 2;
                @Override
                public void run() {
                    if(countdown > 0) {
                        countdown -= 1;
                    } else {
                        game.centralboardLabel.setVisible(false);
                        popup.cancel();
                    }
                }
            };
            popup.scheduleAtFixedRate(task, 0, 1000);

        }
        
    }

    public void showEightPockets() {

        game.blackScoreBall = new ImageView(new Image("file:src/game/resources/ScoreBalls/Ball8.png"));
        game.blackScoreBall.setFitWidth(60);
        game.blackScoreBall.setFitHeight(60);
        game.blackScoreBall.setLayoutX(674);
        game.blackScoreBall.setLayoutY(142);
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

    public void removeEightPockets() {

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

    public void eightPocketDeclaration() {
        game.pocketButton1.setOnAction(event -> {
            game.pocket1.setStroke(Color.GREEN);
            Rules.eightDeclaredPocket = 1;
            eightOff();
        });
        game.pocketButton2.setOnAction(event -> {
            game.pocket2.setStroke(Color.GREEN);
            Rules.eightDeclaredPocket = 2;
            eightOff();
        });
        game.pocketButton3.setOnAction(event -> {
            game.pocket3.setStroke(Color.GREEN);
            Rules.eightDeclaredPocket = 3;
            eightOff();
        });
        game.pocketButton4.setOnAction(event -> {
            game.pocket4.setStroke(Color.GREEN);
            Rules.eightDeclaredPocket = 4;
            eightOff();
        });
        game.pocketButton5.setOnAction(event -> {
            game.pocket5.setStroke(Color.GREEN);
            Rules.eightDeclaredPocket = 5;
            eightOff();
        });
        game.pocketButton6.setOnAction(event -> {
            game.pocket6.setStroke(Color.GREEN);
            Rules.eightDeclaredPocket = 6;
            eightOff();
        });
    }

    private void eightOff(){
        game.pocketButton1.setVisible(false);
        game.pocketButton2.setVisible(false);
        game.pocketButton3.setVisible(false);
        game.pocketButton4.setVisible(false);
        game.pocketButton5.setVisible(false);
        game.pocketButton6.setVisible(false);
    }

    public void win() {

        removeObjects();
        gameFinished();

        if(!game.soundOff) {
            Sounds.playSound("WinSound");
        }

        if(game.player1.isMyTurn()) {
            game.player1.setWin(true);
            game.player2.setWin(false);
            game.gameOver = true;
            game.winLabel.setVisible(true);
            game.winLabel.toFront();
            game.winLabel.setText("VINCE  " + game.player1.getNickname());
        } else {
            game.player1.setWin(false);
            game.player2.setWin(true);
            game.gameOver = true;
            game.winLabel.setVisible(true);
            game.winLabel.toFront();
            game.winLabel.setText("VINCE  " + game.player2.getNickname());
        }

    }

    public void eightPotted() {

        removeObjects();
        gameFinished();

        if(!game.soundOff) {
            Sounds.playSound("WinSound");
        }

        if(game.player1.isMyTurn()) {
            game.player1.setWin(false);
            game.player2.setWin(true);
            game.gameOver = true;
            game.winLabel.setVisible(true);
            game.winLabel.toFront();
            game.winLabel.setText("VINCE  " + game.player2.getNickname());
        } else {
            game.player1.setWin(true);
            game.player2.setWin(false);
            game.gameOver = true;
            game.winLabel.setVisible(true);
            game.winLabel.toFront();
            game.winLabel.setText("VINCE  " + game.player1.getNickname());
        }

    }

    public void removeObjects() {
        for(int i = 0; i < 16; i++) {
            if(!game.ball[i].isDropped()) {
                game.removeFromPane(game.ball[i].drawBall());
            }
        }
        game.removeFromPane(game.cue);
        game.removeFromPane(game.guidelineToBall);
        game.removeFromPane(game.ghostBall);
        game.removeFromPane(game.guidelineFromBall);
        game.removeFromPane(game.guidelineFromCue);
        game.removeFromPane(game.shotClockBar);
        game.removeFromPane(game.turnboardLabel);
    }

    public void gameFinished() {
        game.menuButtonFromGame.setLayoutX(675);
        game.menuButtonFromGame.setLayoutY(450);
    }

}
