package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private boolean isWhiteTurn = true;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        if (isWhiteTurn) {
            return TeamColor.WHITE;
        }
        else {
            return TeamColor.BLACK;
        }
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        if (team == TeamColor.WHITE) {
            isWhiteTurn = true;
        }
        else {
            isWhiteTurn = false;
        }
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        //ArrayList<ChessMove> theMoves = new ArrayList<>();

        //ChessBoard cloneBoard = new ChessBoard(board);

        //return theMoves;
        throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if ((isWhiteTurn && board.getPiece(move.getStartPosition()).getTeamColor() == TeamColor.WHITE)
                || (!isWhiteTurn && board.getPiece(move.getStartPosition()).getTeamColor() == TeamColor.BLACK)) {
            boolean itIsGood = false;
            for (ChessMove theMove : validMoves(move.getStartPosition())) {
                if (theMove == move) {
                    itIsGood = true;
                }
            }
            if (itIsGood) {
                board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
                board.addPiece(move.getStartPosition(), null);
                if (isWhiteTurn) {
                    isWhiteTurn = false;
                }
                else {
                    isWhiteTurn = true;
                }
            }
            else {
                throw new InvalidMoveException();
            }
        }
        else {
            throw new InvalidMoveException();
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return inDanger(teamColor, board);
    }



    private boolean inDanger(TeamColor teamColor, ChessBoard chessBoard) {
        boolean isInDanger = false;
        ArrayList<ChessMove> enemyMoves = enemyHitList(teamColor, chessBoard);
        for (ChessMove moveIterator : enemyMoves) {
            if (chessBoard.getPiece(moveIterator.getEndPosition()) != null && chessBoard.getPiece(moveIterator.getEndPosition()).getPieceType() == ChessPiece.PieceType.KING) {
                isInDanger = true;
            }
        }
        return isInDanger;
    }

    private ArrayList<ChessMove> enemyHitList(TeamColor teamColor, ChessBoard chessBoard) {
        ArrayList<ChessMove> enemyMoves = new ArrayList<>();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                ChessPosition thisPosition = new ChessPosition(r + 1, c + 1);
                if (chessBoard.getPiece(thisPosition) != null && chessBoard.getPiece(thisPosition).getTeamColor() != teamColor) {
                    ArrayList<ChessMove> specificMoves = (ArrayList<ChessMove>) chessBoard.getPiece(thisPosition).pieceMoves(chessBoard, thisPosition);
                    for (ChessMove onlyOne : specificMoves) {
                        enemyMoves.add(onlyOne);
                    }
                }
            }
        }
        return enemyMoves;
    }

    private ArrayList<ChessMove> friendHitList(TeamColor teamColor) {
        ArrayList<ChessMove> friendMoves = new ArrayList<>();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                ChessPosition thisPosition = new ChessPosition(r + 1, c + 1);
                if (board.getPiece(thisPosition) != null && board.getPiece(thisPosition).getTeamColor() == teamColor) {
                    ArrayList<ChessMove> specificMoves = (ArrayList<ChessMove>) board.getPiece(thisPosition).pieceMoves(board, thisPosition);
                    for (ChessMove onlyOne : specificMoves) {
                        friendMoves.add(onlyOne);
                    }
                }
            }
        }
        return friendMoves;
    }



    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        boolean isDead = false;
        if (isInCheck(teamColor)) {
            isDead = true;
            ArrayList<ChessMove> friendMoves = friendHitList(teamColor);

            for (ChessMove moveIterator : friendMoves) {
                ChessBoard cloneBoard = cloneKnockoff();
                cloneBoard.addPiece(moveIterator.getEndPosition(), cloneBoard.getPiece(moveIterator.getStartPosition()));
                cloneBoard.addPiece(moveIterator.getStartPosition(), null);
                if (!inDanger(teamColor, cloneBoard)) {
                    isDead = false;
                }
            }
        }
        return isDead;
    }

    private ChessBoard cloneKnockoff() {
        ChessBoard cloneBoard = new ChessBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPosition thisPosition = new ChessPosition(i + 1, j + 1);
                if (board.getPiece(thisPosition) != null) {
                    ChessPiece onePiece = new ChessPiece(board.getPiece(thisPosition).getTeamColor(),board.getPiece(thisPosition).getPieceType());
                    cloneBoard.addPiece(thisPosition, onePiece);
                }
            }
        }
        return cloneBoard;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {

        boolean isCrusty;

        if (!isInCheck(teamColor)) {
            isCrusty = true;

            ArrayList<ChessMove> enemyMoves = enemyHitList(teamColor, board);





        }
        else {
            isCrusty = false;
        }
        return isCrusty;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }
}
