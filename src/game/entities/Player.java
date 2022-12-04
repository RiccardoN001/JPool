package game.entities;

// BRIEF CLASS DESCRIPTION
// Represents a player

public class Player {

    private String nickname;
    private int ballType; // CATEGORY ASSIGNED: 0 -> not assigned, 1 -> solid, 2 -> striped
    private boolean myTurn;
    private boolean allBallsPotted;
    private boolean win;

    // CONSTRUCTOR METHOD
    public Player(String nickname) {
        this.nickname = nickname;
        this.ballType = 0;
        this.myTurn = false;
        this.allBallsPotted = false;
        this.win = false;
    }

    // GET/SET METHODS

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getBallType() {
        return ballType;
    }

    public void setBallType(int ballType) {
        this.ballType = ballType;
    }

    public boolean isMyTurn() {
        return myTurn;
    }

    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }

    public boolean isAllBallsPotted() {
        return allBallsPotted;
    }

    public void setAllBallsPotted(boolean allBallsPotted) {
        this.allBallsPotted = allBallsPotted;
    }

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

}
