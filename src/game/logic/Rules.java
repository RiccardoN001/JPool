package game.logic;

// BRIEF CLASS DESCRIPTION 
// Logical Referee: directs the match, implementing the rules

public class Rules {

    private GameController game = GameController.getController();

    public static int eightDeclaredPocket = 0;

    private Board board = new Board();

    public void checkFoul() {

        boolean change = false;

        // TURN 1
        if(game.turnNum == 1) {

            // 0 balls potted
            if(game.thisTurnPottedBalls.size() == 0) {
                change = true;
            // at least 1 ball potted
            } else {

                // check if eight, cue or other ball
                for(int i = 0; i < game.thisTurnPottedBalls.size(); i++) {
                    if(game.thisTurnPottedBalls.get(i).intValue() == 8) {
                        board.eightPotted();
                    } else if (game.thisTurnPottedBalls.get(i).intValue() == 0) {
                        game.foulWhite = true;
                    } else {
                        // update thisturnpottedballs arraylist
                        game.potted[game.thisTurnPottedBalls.get(i).intValue()] = true;
                    }
                }

            }

        // TURN >= 2 AND BALLS (CATEGORY) NOT ASSIGNED
        } else if(game.turnNum >= 2 && game.player1.getBallType() == 0) {

            // 0 balls potted
            if(game.thisTurnPottedBalls.size()== 0) {
                change = true;
            // at least 1 ball potted
            } else {

                // first potted ball -> players categories
                int firstPottedBallNum = game.thisTurnPottedBalls.get(0).intValue();
                if(firstPottedBallNum >= 1 && firstPottedBallNum <= 8) {
                    if(game.player1.isMyTurn()) {
                        game.player1.setBallType(1);
                        game.player2.setBallType(2);
                    } else {
                        game.player1.setBallType(2);
                        game.player2.setBallType(1);
                    }
                } else if(firstPottedBallNum >= 9 && firstPottedBallNum <= 15) {
                    if(game.player1.isMyTurn()) {
                        game.player1.setBallType(2);
                        game.player2.setBallType(1);
                    } else {
                        game.player1.setBallType(1);
                        game.player2.setBallType(2);
                    }
                }

                // check if eight, cue or other ball
                for(int i = 0; i < game.thisTurnPottedBalls.size(); i++) {
                    if(game.thisTurnPottedBalls.get(i).intValue() == 8) {
                        board.eightPotted();
                    } else if (game.thisTurnPottedBalls.get(i).intValue() == 0) {
                        game.foulWhite = true;
                    } else {
                        // update thisturnpottedballs arraylist
                        game.potted[game.thisTurnPottedBalls.get(i).intValue()] = true;
                    }
                }

                // assign balls
                if(!game.gameOver && !game.thisTurnPottedBalls.contains(Integer.valueOf(0))) {
                    board.ballAssignment();
                }

            }

        // TURN >= 2 AND BALLS (CATEGORY) ASSIGNED
        } else {

            // 0 balls potted
            if(game.thisTurnPottedBalls.size() == 0) {
                change = true;
            // just eight ball potted
            } else if(game.thisTurnPottedBalls.size() == 1 && game.thisTurnPottedBalls.get(0).intValue() == 8) {

                if(game.player1.isMyTurn()) {

                    // player 1 turn and has solid -> check all solid
                    if(game.player1.getBallType() == 1) {

                        boolean allBallsPotted = true;
                        for(int i = 1; i <= 7; i++) {
                            if(!game.potted[i]) {
                                allBallsPotted = false;
                                board.eightPotted();
                            }
                        }
                        // pocket in delcared pocket -> win
                        if(allBallsPotted && game.eightPocket == eightDeclaredPocket) {
                            board.win();
                            return;
                        // !pocket in declared pocket -> opponent win
                        } else if(allBallsPotted && game.eightPocket != eightDeclaredPocket) {
                            board.eightPotted();
                            return;
                        }

                    // player 1 turn and has striped -> check all striped
                    } else {

                        boolean allBallsPotted = true;
                        for(int i = 9; i <= 15; i++) {
                            if(!game.potted[i]) {
                                allBallsPotted = false;
                                board.eightPotted();
                            }
                        }
                        // pocket in delcared pocket -> win
                        if(allBallsPotted && game.eightPocket == eightDeclaredPocket) {
                            board.win();
                            return;
                        // !pocket in declared pocket -> opponent win
                        } else if(allBallsPotted && game.eightPocket != eightDeclaredPocket) {
                            board.eightPotted();
                            return; 
                        }

                    }

                } else {

                    // player 2 turn and has solid -> check all solid
                    if(game.player2.getBallType() == 1) {

                        boolean allBallsPotted = true;
                        for(int i = 1; i <= 7; i++) {
                            if(!game.potted[i]) {
                                allBallsPotted = false;
                                board.eightPotted();
                            }
                        }
                        // pocket in delcared pocket -> win
                        if(allBallsPotted && game.eightPocket == eightDeclaredPocket) {
                            board.win();
                            return;
                        // !pocket in declared pocket -> opponent win
                        } else if(allBallsPotted && game.eightPocket != eightDeclaredPocket) {
                            board.eightPotted();
                            return;
                        }

                    // player 2 turn and has striped -> check all striped
                    } else {

                        boolean allBallsPotted = true;
                        for(int i = 9; i <= 15; i++) {
                            if(!game.potted[i]) {
                                allBallsPotted = false;
                                board.eightPotted();
                            }
                        }
                        // pocket in delcared pocket -> win
                        if(allBallsPotted && game.eightPocket == eightDeclaredPocket) {
                            board.win();
                            return;
                        // !pocket in declared pocket -> opponent win
                        } else if(allBallsPotted && game.eightPocket != eightDeclaredPocket) {
                            board.eightPotted();
                            return;
                        }

                    }

                }

            // balls potted > 1 or eight ball potted
            } else {

                int firstPottedBallNum = game.thisTurnPottedBalls.get(0).intValue();

                if(game.player1.isMyTurn()) {

                    // first ball in different from own type -> change turn player
                    if(game.player1.getBallType() != game.ball[firstPottedBallNum].getBallType()) {
                        change = true;
                    }

                    // check if eight, cue or other ball
                    for(int i = 0; i < game.thisTurnPottedBalls.size(); i++) {
                        if(game.thisTurnPottedBalls.get(i).intValue() == 8) {
                            board.eightPotted();
                        } else if(game.thisTurnPottedBalls.get(i).intValue() == 0) {
                            game.foulWhite = true;
                        } else {
                            // update thisturnpottedballs arraylist
                            game.potted[game.thisTurnPottedBalls.get(i).intValue()] = true;
                        }
                    }

                } else {

                    // first ball in different from own type -> change turn player
                    if(game.player2.getBallType() != game.ball[firstPottedBallNum].getBallType()) {
                        change = true;
                    }

                    // check if eight, cue or other ball
                    for(int i = 0; i < game.thisTurnPottedBalls.size(); i++) {
                        if(game.thisTurnPottedBalls.get(i).intValue() == 8) {
                            board.eightPotted();
                        } else if(game.thisTurnPottedBalls.get(i).intValue() == 0) {
                            game.foulWhite = true;
                        } else {
                            // update thisturnpottedballs arraylist
                            game.potted[game.thisTurnPottedBalls.get(i).intValue()] = true;
                        }
                    }

                }

            }

        }

        if(game.foulWhite || game.foulWrongBallType || game.foulEight || game.foulNoBallHit || game.foulShotClock) {
            game.foul = true;
        }

        if(change || game.foulWhite || game.foulWrongBallType || game.foul || game.foulNoBallHit || game.foulShotClock) {
            board.changeTurn();
        }

    }

    public void checkPotted() {

        // balls not assigned
        if(game.player1.getBallType() == 0) {
            return;
        } 

        // player 1 turn
        if(game.player1.isMyTurn()) {
           
            boolean allBallsPotted = true;

            if(game.player1.getBallType() == 1) {
                // player 1 -> solid -> check all solid
                for(int i = 1; i <= 7; i++) {
                    if(!game.potted[i]) {
                        allBallsPotted = false;
                        break;
                    }
                }
            } else {
                // player 1 -> striped -> check all striped
                for(int i = 9; i <= 15; i++) {
                    if(!game.potted[i]) {
                        allBallsPotted = false;
                        break;
                    }
                }
            }

            if(allBallsPotted) {
                game.player1.setAllBallsPotted(true);
            }

        // player 2 turn
        } else {

            boolean allBallsPotted = true;

            if(game.player2.getBallType() == 1) {
                // player 2 -> solid -> check all solid
                for(int i = 1; i <= 7; i++) {
                    if(!game.potted[i]) {
                        allBallsPotted = false;
                        break;
                    }
                }
            } else {
                // player 2 -> striped -> check all striped
                for(int i = 9; i <= 15; i++) {
                    if(!game.potted[i]) {
                        allBallsPotted = false;
                        break;
                    }
                }
            }

            if(allBallsPotted) {
                game.player2.setAllBallsPotted(true);
            }

        }

    }
    
}
