package game.model;

public class Player {
    
    // BRIEF CLASS DESCRIPTION (FINISHED)
    // Represents a player

    private String nickname;
    private boolean myTurn;
    private boolean allBallsPlotted;
    private boolean win;
    private int ballType;

    // CONSTRUCTOR METHODS
    public Player() {
        nickname = "";
        myTurn = false;
        allBallsPlotted = false;
        win = false;
        ballType = 0;
    }

    public Player(String nickname) {
        this.nickname = nickname;
        myTurn = false;
        allBallsPlotted = false;
        win = false;
        ballType = 0;
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
