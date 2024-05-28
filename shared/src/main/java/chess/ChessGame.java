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
    private boolean blackRookOneMoved = false;
    private boolean blackRookTwoMoved = false;
    private boolean whiteRookOneMoved = false;
    private boolean whiteRookTwoMoved = false;
    private boolean blackKingMoved = false;
    private boolean whiteKingMoved = false;
    private boolean whitePawnHalf = false;
    private boolean blackPawnHalf = false;
    private int whitePawnColumn;
    private int blackPawnColumn;
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
        ArrayList<ChessMove> theMoves = new ArrayList<>();

        if (isInStalemate(board.getPiece(startPosition).getTeamColor()) || isInCheckmate(board.getPiece(startPosition).getTeamColor())) {
            return theMoves;
        }
        else {
            ArrayList<ChessMove> friendMoves = (ArrayList<ChessMove>) board.getPiece(startPosition).pieceMoves(board, startPosition);
            for (ChessMove oneMove : friendMoves) {
                ChessBoard cloneBoard = cloneKnockoff();
                cloneBoard.addPiece(oneMove.getEndPosition(), cloneBoard.getPiece(oneMove.getStartPosition()));
                cloneBoard.addPiece(oneMove.getStartPosition(), null);
                if (!inDanger(board.getPiece(startPosition).getTeamColor(), cloneBoard, ChessPiece.PieceType.KING)) {
                    theMoves.add(oneMove);
                }
            }
            if (!whiteKingMoved) {
                ArrayList<ChessMove> howlsCastle = castleMoves(startPosition, 1, whiteRookOneMoved, whiteRookTwoMoved, whiteKingMoved, TeamColor.WHITE);
                theMoves.addAll(howlsCastle);
            }
            if (!blackKingMoved) {
                ArrayList<ChessMove> howlsCastle = castleMoves(startPosition, 8, blackRookOneMoved, blackRookTwoMoved, blackKingMoved, TeamColor.BLACK);
                theMoves.addAll(howlsCastle);
            }
            if (board.getPiece(startPosition).getPieceType() == ChessPiece.PieceType.PAWN) {
                ArrayList<ChessMove> gotcha = enPassantMove(startPosition, board.getPiece(startPosition).getTeamColor());
                theMoves.addAll(gotcha);
            }
            return theMoves;
        }
    }
    private ArrayList<ChessMove> enPassantMove(ChessPosition startPosition, ChessGame.TeamColor teamColor) {
        int startRow = 5;
        int multiplier = 1;
        boolean pawnhalf = blackPawnHalf;
        int pawnColumn = blackPawnColumn;
        if (teamColor == TeamColor.BLACK) {
            startRow =4;
            multiplier = -1;
            pawnhalf = whitePawnHalf;
            pawnColumn = whitePawnColumn;
        }
        ArrayList<ChessMove> gotcha = new ArrayList<>();
        if (startPosition.getRow() == startRow) {
            ArrayList<ChessMove> positive = plusOrMinus(startPosition, teamColor, multiplier, pawnhalf, pawnColumn, 1);
            ArrayList<ChessMove> negative = plusOrMinus(startPosition, teamColor, multiplier, pawnhalf, pawnColumn, -1);
            gotcha.addAll(positive);
            gotcha.addAll(negative);
        }
        return gotcha;
    }
    private ArrayList<ChessMove> plusOrMinus(ChessPosition startPosition, ChessGame.TeamColor teamColor, int multiplier, boolean pawnhalf, int pawnColumn, int yesOrNo) {
        ArrayList<ChessMove> gotcha = new ArrayList<>();
        if (startPosition.getColumn() + yesOrNo <= 8
                && startPosition.getColumn() + yesOrNo >= 1) {
            ChessPosition leftHand = new ChessPosition(startPosition.getRow() + multiplier, startPosition.getColumn() + yesOrNo);
            ChessPosition leftEnemy = new ChessPosition(startPosition.getRow(), startPosition.getColumn() + yesOrNo);
            if (pawnhalf && pawnColumn == leftEnemy.getColumn()) {
                ChessBoard pawnClone = cloneKnockoff();
                pawnClone.addPiece(leftHand, pawnClone.getPiece(startPosition));
                pawnClone.addPiece(startPosition, null);
                pawnClone.addPiece(leftEnemy, null);
                if (!inDanger(teamColor, pawnClone, ChessPiece.PieceType.KING)) {
                    ChessMove youShallPass = new ChessMove(startPosition, leftHand, null);
                    gotcha.add(youShallPass);
                }
            }
        }
        return gotcha;
    }
    private ArrayList<ChessMove> castleMoves(ChessPosition startPosition, int firstOrLastRow, boolean leftRook, boolean rightRook, boolean kingPin, TeamColor teamColor) {
        ArrayList<ChessMove> theMoves = new ArrayList<>();
        if (startPosition.getRow() == firstOrLastRow && startPosition.getColumn() == 5) {
            if (!kingPin && !leftRook
                    && !inDanger(teamColor, board, ChessPiece.PieceType.KING)) {
                ChessPosition rookPosition = new ChessPosition(firstOrLastRow, 1);
                ChessPosition positionTwo = new ChessPosition(firstOrLastRow, 2);
                ChessPosition positionThree = new ChessPosition(firstOrLastRow, 3);
                ChessPosition positionFour = new ChessPosition(firstOrLastRow, 4);
                if (board.getPiece(positionTwo) == null && board.getPiece(positionThree) == null && board.getPiece(positionFour) == null) {
                    ArrayList<ChessMove> rookie = castleSimplifier(startPosition, teamColor, positionThree, positionFour, rookPosition);
                    theMoves.addAll(rookie);
                }
            }
            if (!kingPin && !rightRook
                    && !inDanger(teamColor, board, ChessPiece.PieceType.KING)) {
                ChessPosition rookPosition = new ChessPosition(firstOrLastRow, 8);
                ChessPosition positionSix = new ChessPosition(firstOrLastRow, 6);
                ChessPosition positionSeven = new ChessPosition(firstOrLastRow, 7);
                if (board.getPiece(positionSix) == null && board.getPiece(positionSeven) == null) {
                    ArrayList<ChessMove> rookie = castleSimplifier(startPosition, teamColor, positionSeven, positionSix, rookPosition);
                    theMoves.addAll(rookie);
                }
            }
        }
        return theMoves;
    }
    private ArrayList<ChessMove> castleSimplifier(ChessPosition startPosition, ChessGame.TeamColor teamColor, ChessPosition kingEnd, ChessPosition rookEnd, ChessPosition rookPosition) {
        ArrayList<ChessMove> theMoves = new ArrayList<>();
        ChessBoard cheapo = cloneKnockoff();
        cheapo.addPiece(kingEnd, board.getPiece(startPosition));                                                  //  kingEnd, or positionSeven, or positionThree
        cheapo.addPiece(rookEnd, board.getPiece(rookPosition));                                                     //  rookEnd, or positionSix, or positionFour
        cheapo.addPiece(startPosition, null);
        cheapo.addPiece(rookPosition, null);
        if (!isInCheck(teamColor)) {
            ArrayList<ChessMove> newEnemyMoves = enemyHitList(teamColor, cheapo);
            boolean allClear = true;
            for (ChessMove wallHitter : newEnemyMoves) {
                if (wallHitter.getEndPosition().getRow() == rookEnd.getRow()
                        && wallHitter.getEndPosition().getColumn() == rookEnd.getColumn()) {
                    allClear = false;
                }
            }
            if (allClear) {
                ChessMove castle = new ChessMove(startPosition, kingEnd, null);
                theMoves.add(castle);
            }
        }
        return theMoves;
    }
    private void rookEnd(ChessMove move) {
        int startRow;
        boolean kingMoved;
        boolean rookOneMoved;
        boolean rookTwoMoved;
        if (isWhiteTurn) {
            startRow = 1;
            kingMoved = whiteKingMoved;
            rookOneMoved = whiteRookOneMoved;
            rookTwoMoved = whiteRookTwoMoved;
        }
        else {
            startRow = 8;
            kingMoved = blackKingMoved;
            rookOneMoved = blackRookOneMoved;
            rookTwoMoved = blackRookTwoMoved;
        }
        if (move.getStartPosition().getRow() == startRow
                && move.getStartPosition().getColumn() == 5 && !kingMoved) {
            if (move.getEndPosition().getRow() == startRow
                    && move.getEndPosition().getColumn() == 3 && !rookOneMoved) {
                ChessPosition rookStart = new ChessPosition(startRow, 1);
                ChessPosition rookEnd = new ChessPosition(startRow, 4);
                board.addPiece(rookEnd, board.getPiece(rookStart));
                board.addPiece(rookStart, null);
                if (startRow == 1) {
                    whiteRookOneMoved = true;
                }
                else {
                    blackRookOneMoved = true;
                }
            }
            else if (move.getEndPosition().getRow() == startRow
                    && move.getEndPosition().getColumn() == 7 && !rookTwoMoved) {
                ChessPosition rookStart = new ChessPosition(startRow, 8);
                ChessPosition rookEnd = new ChessPosition(startRow, 6);
                board.addPiece(rookEnd, board.getPiece(rookStart));
                board.addPiece(rookStart, null);
                if (startRow == 1) {
                    whiteRookTwoMoved = true;
                }
                else {
                    blackRookTwoMoved = true;
                }
            }
        }
    }
    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if ( board.getPiece(move.getStartPosition()) != null
                && (isWhiteTurn && board.getPiece(move.getStartPosition()).getTeamColor() == TeamColor.WHITE)
                || (!isWhiteTurn && board.getPiece(move.getStartPosition()).getTeamColor() == TeamColor.BLACK)) {
            ChessBoard cloneBoard = cloneKnockoff();
            cloneBoard.addPiece(move.getEndPosition(), cloneBoard.getPiece(move.getStartPosition()));
            cloneBoard.addPiece(move.getStartPosition(), null);
            if (!inDanger(board.getPiece(move.getStartPosition()).getTeamColor(), cloneBoard, ChessPiece.PieceType.KING)) {
                boolean itIsGood = false;
                for (ChessMove theMove : validMoves(move.getStartPosition())) {
                    if (theMove.getStartPosition().getRow() == move.getStartPosition().getRow()
                            && theMove.getStartPosition().getColumn() == move.getStartPosition().getColumn()
                            && theMove.getEndPosition().getRow() == move.getEndPosition().getRow()
                            && theMove.getEndPosition().getColumn() == move.getEndPosition().getColumn()) {
                        itIsGood = true;
                    }
                }
                if (itIsGood) {
                    ChessBoard pawnTracker = cloneKnockoff();
                    board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
                    board.addPiece(move.getStartPosition(), null);
                    if (move.getPromotionPiece() != null) {
                        ChessPiece doubleAgent = new ChessPiece(board.getPiece(move.getEndPosition()).getTeamColor(), move.getPromotionPiece());
                        board.addPiece(move.getEndPosition(), doubleAgent);
                    }
                    rookEnd(move);
                    if (move.getStartPosition().getRow() == 1) {
                        if(move.getStartPosition().getColumn() == 1) {
                            whiteRookOneMoved = true;
                        }
                        else if (move.getStartPosition().getColumn() == 8) {
                            whiteRookTwoMoved = true;
                        }
                        else if (move.getStartPosition().getColumn() == 5) {
                            whiteKingMoved = true;
                        }
                    }
                    else if (move.getStartPosition().getRow() == 8 && move.getStartPosition().getColumn() == 1) {
                        if(move.getStartPosition().getColumn() == 1) {
                            blackRookOneMoved = true;
                        }
                        else if (move.getStartPosition().getColumn() == 8) {
                            blackRookTwoMoved = true;
                        }
                        else if (move.getStartPosition().getColumn() == 5) {
                            blackKingMoved = true;
                        }
                    }
                    if (pawnTracker.getPiece(move.getStartPosition()).getPieceType() == ChessPiece.PieceType.PAWN) {
                        if (isWhiteTurn && move.getEndPosition().getRow() - move.getStartPosition().getRow() == 2) {
                            whitePawnColumn = move.getEndPosition().getColumn();
                            whitePawnHalf = true;
                        }
                        if (!isWhiteTurn && move.getStartPosition().getRow() - move.getEndPosition().getRow() == 2) {
                            blackPawnColumn = move.getEndPosition().getColumn();
                            blackPawnHalf = true;
                        }
                    }
                    if (isWhiteTurn && pawnTracker.getPiece(move.getStartPosition()).getPieceType() == ChessPiece.PieceType.PAWN
                            && blackPawnHalf && blackPawnColumn == move.getEndPosition().getColumn()) {
                        ChessPosition enemyPosition = new ChessPosition(move.getStartPosition().getRow(), blackPawnColumn);
                        board.addPiece(enemyPosition, null);
                    }
                    if (!isWhiteTurn && pawnTracker.getPiece(move.getStartPosition()).getPieceType() == ChessPiece.PieceType.PAWN
                            && whitePawnHalf && whitePawnColumn == move.getEndPosition().getColumn()) {
                        ChessPosition enemyPosition = new ChessPosition(move.getStartPosition().getRow(), whitePawnColumn);
                        board.addPiece(enemyPosition, null);
                    }
                    if (isWhiteTurn) {
                        isWhiteTurn = false;
                        blackPawnHalf = false;
                    } else {
                        isWhiteTurn = true;
                        whitePawnHalf = false;
                    }
                }
                else {
                    throw new InvalidMoveException("Nay, this move is not valid.");
                }
            }
            else {
                throw new InvalidMoveException("Nay, the king shall perish.");
            }
        }
        else {
            throw new InvalidMoveException("Nay, it is not your turn.");
        }
    }
    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return inDanger(teamColor, board, ChessPiece.PieceType.KING);
    }
    public boolean inDanger(TeamColor teamColor, ChessBoard chessBoard, ChessPiece.PieceType pieceType) {
        boolean isInDanger = false;
        ArrayList<ChessMove> enemyMoves = enemyHitList(teamColor, chessBoard);
        for (ChessMove moveIterator : enemyMoves) {
            if (chessBoard.getPiece(moveIterator.getEndPosition()) != null && chessBoard.getPiece(moveIterator.getEndPosition()).getPieceType() == pieceType) {
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
                    enemyMoves.addAll(specificMoves);
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
                    friendMoves.addAll(specificMoves);
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
                if (!inDanger(teamColor, cloneBoard, ChessPiece.PieceType.KING)) {
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
        boolean isCrusty = true;
        if (!isInCheck(teamColor)) {
            ArrayList<ChessMove> friendMoves = friendHitList(teamColor);
            for (ChessMove oneMove : friendMoves) {
                boolean allClear = true;
                ChessBoard cloneBoard = cloneKnockoff();
                cloneBoard.addPiece(oneMove.getEndPosition(), cloneBoard.getPiece(oneMove.getStartPosition()));
                cloneBoard.addPiece(oneMove.getStartPosition(), null);
                ArrayList<ChessMove> enemyMoves = enemyHitList(teamColor, cloneBoard);
                for (ChessMove antiMove : enemyMoves) {
                    if (oneMove.getEndPosition().getRow() == antiMove.getEndPosition().getRow()
                            && oneMove.getEndPosition().getColumn() == antiMove.getEndPosition().getColumn()) {
                        allClear = false;
                    }
                }
                if (allClear) {
                    isCrusty = false;
                }
            }
            boolean isThere = false;
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    ChessPosition potentialKing = new ChessPosition(r + 1, c + 1);
                    if (board.getPiece(potentialKing) != null
                            && board.getPiece(potentialKing).getTeamColor() == teamColor
                            && board.getPiece(potentialKing).getPieceType() == ChessPiece.PieceType.KING) {
                        isThere = true;
                    }
                }
            }
            if (!isThere) {
                isCrusty = false;
            }
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
        ChessPosition newPosition = new ChessPosition(8, 1);
        blackRookOneMoved = board.getPiece(newPosition) == null || board.getPiece(newPosition).getPieceType() != ChessPiece.PieceType.ROOK || board.getPiece(newPosition).getTeamColor() != TeamColor.BLACK;
        newPosition = new ChessPosition(8, 8);
        blackRookTwoMoved = board.getPiece(newPosition) == null || board.getPiece(newPosition).getPieceType() != ChessPiece.PieceType.ROOK || board.getPiece(newPosition).getTeamColor() != TeamColor.BLACK;
        newPosition = new ChessPosition(1, 1);
        whiteRookOneMoved = board.getPiece(newPosition) == null || board.getPiece(newPosition).getPieceType() != ChessPiece.PieceType.ROOK || board.getPiece(newPosition).getTeamColor() != TeamColor.WHITE;
        newPosition = new ChessPosition(1, 8);
        whiteRookTwoMoved = board.getPiece(newPosition) == null || board.getPiece(newPosition).getPieceType() != ChessPiece.PieceType.ROOK || board.getPiece(newPosition).getTeamColor() != TeamColor.WHITE;
        newPosition = new ChessPosition(8, 5);
        blackKingMoved = board.getPiece(newPosition) == null || board.getPiece(newPosition).getPieceType() != ChessPiece.PieceType.KING || board.getPiece(newPosition).getTeamColor() != TeamColor.BLACK;
        newPosition = new ChessPosition(1, 5);
        whiteKingMoved = board.getPiece(newPosition) == null || board.getPiece(newPosition).getPieceType() != ChessPiece.PieceType.KING || board.getPiece(newPosition).getTeamColor() != TeamColor.WHITE;
        isWhiteTurn = true;
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
