package game.entities;

// BRIEF CLASS DESCRIPTION
// Represents a player

public class Player {

    private String nickname;
    private boolean myTurn;
    private boolean allBallsPlotted;
    private boolean win;
    private int ballType; // CATEGORY ASSIGNED: 0 -> not assigned, 1 -> solid, 2 -> striped

    // CONSTRUCTOR METHOD
    public Player(String nickname) {
        this.nickname = nickname;
        this.myTurn = false;
        this.allBallsPlotted = false;
        this.win = false;
        this.ballType = 0;
    }

    // GET/SET METHODS

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isMyTurn() {
        return myTurn;
    }

    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }

    public boolean isAllBallsPlotted() {
        return allBallsPlotted;
    }

    public void setAllBallsPlotted(boolean allBallsPlotted) {
        this.allBallsPlotted = allBallsPlotted;
    }

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public int getBallType() {
        return ballType;
    }

    public void setBallType(int ballType) {
        this.ballType = ballType;
    }

}
