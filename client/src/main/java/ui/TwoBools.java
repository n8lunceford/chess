package ui;

public class TwoBools {
    private boolean move;
    private boolean piece;
    public TwoBools() {
        move = false;
        piece = false;
    }

    public void setMove(boolean move) {
        this.move = move;
    }

    public void setPiece(boolean piece) {
        this.piece = piece;
    }

    public boolean getMove() {
        return move;
    }

    public boolean getPiece() {
        return piece;
    }
}
