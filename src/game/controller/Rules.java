package game.controller;

import game.view.Board;

public class Rules {

    private static GameController game = GameController.getController();

    public static int eightDeclaredPocket = 0;

    public static void checkFoul() {

        boolean change = false;

        if(game.turnNum == 1) {

            if(game.thisTurnPottedBalls.size() == 0) {
                change = true;
            } else {
                for(int i = 0; i < game.thisTurnPottedBalls.size(); i++) {
                    if(game.thisTurnPottedBalls.get(i).intValue() == 8) {
                        Board.eightPotted();
                    } else if (game.thisTurnPottedBalls.get(i).intValue() == 0) {
                        game.foulWhite = true;
                    } else {
                        game.potted[game.thisTurnPottedBalls.get(i).intValue()] = true;
                    }
                }
            }

        } else if(game.turnNum >= 2 && game.player1.getBallType() == 0) {

            if(game.thisTurnPottedBalls.size()== 0) {
                change = true;
            } else {
                int firstPuttedBallNum = game.thisTurnPottedBalls.get(0).intValue();
                if(firstPuttedBallNum >= 1 && firstPuttedBallNum <= 8) {
                    if(game.player1.isMyTurn()) {
                        game.player1.setBallType(1);
                        game.player2.setBallType(2);
                    } else {
                        game.player1.setBallType(2);
                        game.player2.setBallType(1);
                    }
                    Board.ballAssignment();
                } else if(firstPuttedBallNum >= 9 && firstPuttedBallNum <= 15) {
                    if(game.player1.isMyTurn()) {
                        game.player1.setBallType(2);
                        game.player2.setBallType(1);
                    } else {
                        game.player1.setBallType(1);
                        game.player2.setBallType(2);
                    }
                    Board.ballAssignment();
                }

                for(int i = 0; i < game.thisTurnPottedBalls.size(); i++) {
                    if(game.thisTurnPottedBalls.get(i).intValue() == 8) {
                        Board.eightPotted();
                    } else if (game.thisTurnPottedBalls.get(i).intValue() == 0) {
                        game.foulWhite = true;
                    } else {
                        game.potted[game.thisTurnPottedBalls.get(i).intValue()] = true;
                    }
                }

            }

        } else {

            if(game.thisTurnPottedBalls.size() == 0) {
                change = true;
            } else if(game.thisTurnPottedBalls.size() == 1 && game.thisTurnPottedBalls.get(0).intValue() == 8) {

                if(game.player1.isMyTurn()) {

                    if(game.player1.getBallType() == 1) {
                        int f = 0;
                        for(int i = 1; i<= 7; i++) {
                            if(!game.potted[i]) {
                                f = 1;
                                Board.eightPotted();
                            }
                        }
                        if(f == 0 && game.eightPocket == eightDeclaredPocket) {
                            Board.win();
                            return;
                        } else if(f == 0 && game.eightPocket != eightDeclaredPocket) {
                            //System.out.println("Buca errata dichiarata");
                            Board.eightPotted();
                            return;
                        }
                    } else {

                        int f = 0;
                        for(int i = 9; i<= 15; i++) {
                            if(!game.potted[i]) {
                                f = 1;
                                Board.eightPotted();
                            }
                        }
                        if(f == 0 && game.eightPocket == eightDeclaredPocket) {
                            Board.win();
                            return;
                        } else if(f == 0 && game.eightPocket != eightDeclaredPocket) {
                            //System.out.println("Buca errata dichiarata");
                            Board.eightPotted();
                            return; 
                        }

                    }

                } else {

                    if(game.player2.getBallType() == 1) {
                        int f = 0;
                        for(int i = 1; i<= 7; i++) {
                            if(!game.potted[i]) {
                                f = 1;
                                Board.eightPotted();
                            }
                        }
                        if(f == 0 && game.eightPocket == eightDeclaredPocket) {
                            Board.win();
                            return;
                        } else if(f == 0 && game.eightPocket != eightDeclaredPocket) {
                            //System.out.println("Buca errata dichiarata");
                            Board.eightPotted();
                            return;
                        }
                    } else {

                        int f = 0;
                        for(int i = 9; i<= 15; i++) {
                            if(!game.potted[i]) {
                                f = 1;
                                Board.eightPotted();
                            }
                        }
                        if(f == 0 && game.eightPocket == eightDeclaredPocket) {
                            Board.win();
                            return;
                        } else if(f == 0 && game.eightPocket != eightDeclaredPocket) {
                            //System.out.println("Buca errata dichiarata");
                            Board.eightPotted();
                            return;
                        }

                    }

                }

            } else {

                int firstPuttedBallNum = game.thisTurnPottedBalls.get(0).intValue();

                if(game.player1.isMyTurn()) {

                    if(game.player1.getBallType() != game.ball[firstPuttedBallNum].getBallType()) {
                        change = true;
                    }

                    for(int i = 0; i < game.thisTurnPottedBalls.size(); i++) {
                        if(game.thisTurnPottedBalls.get(i).intValue() == 8) {
                            Board.eightPotted();
                        } else if(game.thisTurnPottedBalls.get(i).intValue() == 0) {
                            game.foulWhite = true;
                        } else {
                            game.potted[game.thisTurnPottedBalls.get(i).intValue()] = true;
                        }
                    }

                } else {

                    if(game.player2.getBallType() != game.ball[firstPuttedBallNum].getBallType()) {
                        change = true;
                    }

                    for(int i = 0; i < game.thisTurnPottedBalls.size(); i++) {
                        if(game.thisTurnPottedBalls.get(i).intValue() == 8) {
                            Board.eightPotted();
                        } else if(game.thisTurnPottedBalls.get(i).intValue() == 0) {
                            game.foulWhite = true;
                        } else {
                            game.potted[game.thisTurnPottedBalls.get(i).intValue()] = true;
                        }
                    }

                }

            }

        }

        if(game.foulWhite || game.foulWrongBallType || game.foulEight || game.foulNoBallHit || game.foulShotClock) {
            game.foul = true;
        }

        if(change || game.foulWhite || game.foulWrongBallType|| game.foul || game.foulNoBallHit || game.foulShotClock) {
            Board.changeTurn();
        }

    }

    public static void checkPotted() {

        if(game.player1.getBallType() == 0) {
            return;
        } 

        if(game.player1.isMyTurn()) {
           
            int f = 0;
            if(game.player1.getBallType() == 1) {
                for(int i = 1; i<= 7; i++) {
                    if(!game.potted[i]) {
                        f = 1;
                        break;
                    }
                }
            } else {
                for(int i = 9; i<= 15; i++) {
                    if(!game.potted[i]) {
                        f = 1;
                        break;
                    }
                }
            }

            if(f==0) {
                game.player1.setAllBallsPlotted(true);
            }

        } else {

            int f = 0;
            if(game.player2.getBallType() == 1) {
                for(int i = 1; i<= 7; i++) {
                    if(!game.potted[i]) {
                        f = 1;
                        break;
                    }
                }
            } else {
                for(int i = 9; i<= 15; i++) {
                    if(!game.potted[i]) {
                        f = 1;
                        break;
                    }
                }
            }

            if(f==0) {
                game.player2.setAllBallsPlotted(true);
            }

        }

    }
    
}
