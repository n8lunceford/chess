package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();

        if (type == PieceType.ROOK) {

            boolean hazard_N = false;
            boolean hazard_S = false;
            boolean hazard_E = false;
            boolean hazard_W = false;

            for (int i = 1; i < 8; i++) {

                if (myPosition.getRow() + i > 8) {
                    hazard_N = true;
                }
                if (myPosition.getRow() - i < 1) {
                    hazard_S = true;
                }
                if (myPosition.getColumn() + i > 8) {
                    hazard_E = true;
                }
                if (myPosition.getColumn() - i < 1) {
                    hazard_W = true;
                }

                //  N O R T H
                if (!hazard_N) {
                    ChessPosition position_North = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn());

                    if (board.getPiece(position_North) == null || board.getPiece(position_North).getTeamColor() != pieceColor) {
                        ChessMove validMove = new ChessMove(myPosition, position_North, null);
                        possibleMoves.add(validMove);
                    }
                    if (board.getPiece(position_North) != null) {
                        hazard_N = true;
                    }
                }

                //  S O U T H
                if (!hazard_S) {
                    ChessPosition position_South = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn());

                    if (board.getPiece(position_South) == null || board.getPiece(position_South).getTeamColor() != pieceColor) {
                        ChessMove validMove = new ChessMove(myPosition, position_South, null);
                        possibleMoves.add(validMove);
                    }
                    if (board.getPiece(position_South) != null) {
                        hazard_S = true;
                    }
                }

                //  E A S T
                if (!hazard_E) {
                    ChessPosition position_East = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + i);

                    if (board.getPiece(position_East) == null || board.getPiece(position_East).getTeamColor() != pieceColor) {
                        ChessMove validMove = new ChessMove(myPosition, position_East, null);
                        possibleMoves.add(validMove);
                    }
                    if (board.getPiece(position_East) != null) {
                        hazard_E = true;
                    }
                }

                //  W E S T
                if (!hazard_W) {
                    ChessPosition position_West = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - i);

                    if (board.getPiece(position_West) == null || board.getPiece(position_West).getTeamColor() != pieceColor) {
                        ChessMove validMove = new ChessMove(myPosition, position_West, null);
                        possibleMoves.add(validMove);
                    }
                    if (board.getPiece(position_West) != null) {
                        hazard_W = true;
                    }
                }

            }
        }

        else if (type == PieceType.BISHOP) {

            boolean hazard_NE = false;
            boolean hazard_NW = false;
            boolean hazard_SE = false;
            boolean hazard_SW = false;

            for (int i = 1; i < 8; i++) {

                if (myPosition.getRow() + i > 8) {
                    hazard_NE = true;
                    hazard_NW = true;
                }
                if (myPosition.getRow() - i < 1) {
                    hazard_SE = true;
                    hazard_SW = true;
                }
                if (myPosition.getColumn() + i > 8) {
                    hazard_NE = true;
                    hazard_SE = true;
                }
                if (myPosition.getColumn() - i < 1) {
                    hazard_NW = true;
                    hazard_SW = true;
                }

                //  N O R T H   E A S T
                if (!hazard_NE) {
                    ChessPosition position_NorthEast = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i);

                    if (board.getPiece(position_NorthEast) == null || board.getPiece(position_NorthEast).getTeamColor() != pieceColor) {
                        ChessMove validMove = new ChessMove(myPosition, position_NorthEast, null);
                        possibleMoves.add(validMove);
                    }
                    if (board.getPiece(position_NorthEast) != null) {
                        hazard_NE = true;
                    }
                }

                //  N O R T H   W E S T
                if (!hazard_NW) {
                    ChessPosition position_NorthWest = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - i);

                    if (board.getPiece(position_NorthWest) == null || board.getPiece(position_NorthWest).getTeamColor() != pieceColor) {
                        ChessMove validMove = new ChessMove(myPosition, position_NorthWest, null);
                        possibleMoves.add(validMove);
                    }
                    if (board.getPiece(position_NorthWest) != null) {
                        hazard_NW = true;
                    }
                }

                //  S O U T H   E A S T
                if (!hazard_SE) {
                    ChessPosition position_SouthEast = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + i);

                    if (board.getPiece(position_SouthEast) == null || board.getPiece(position_SouthEast).getTeamColor() != pieceColor) {
                        ChessMove validMove = new ChessMove(myPosition, position_SouthEast, null);
                        possibleMoves.add(validMove);
                    }
                    if (board.getPiece(position_SouthEast) != null) {
                        hazard_SE = true;
                    }
                }

                //  S O U T H   W E S T
                if (!hazard_SW) {
                    ChessPosition position_SouthWest = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - i);

                    if (board.getPiece(position_SouthWest) == null || board.getPiece(position_SouthWest).getTeamColor() != pieceColor) {
                        ChessMove validMove = new ChessMove(myPosition, position_SouthWest, null);
                        possibleMoves.add(validMove);
                    }
                    if (board.getPiece(position_SouthWest) != null) {
                        hazard_SW = true;
                    }
                }

            }
        }

        else if (type == PieceType.QUEEN) {

            boolean hazard_N = false;
            boolean hazard_S = false;
            boolean hazard_E = false;
            boolean hazard_W = false;

            boolean hazard_NE = false;
            boolean hazard_NW = false;
            boolean hazard_SE = false;
            boolean hazard_SW = false;

            for (int i = 1; i < 8; i++) {

                if (myPosition.getRow() + i > 8) {
                    hazard_N = true;
                    hazard_NE = true;
                    hazard_NW = true;
                }
                if (myPosition.getRow() - i < 1) {
                    hazard_S = true;
                    hazard_SE = true;
                    hazard_SW = true;
                }
                if (myPosition.getColumn() + i > 8) {
                    hazard_E = true;
                    hazard_NE = true;
                    hazard_SE = true;
                }
                if (myPosition.getColumn() - i < 1) {
                    hazard_W = true;
                    hazard_NW = true;
                    hazard_SW = true;
                }

                //  N O R T H
                if (!hazard_N) {
                    ChessPosition position_North = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn());

                    if (board.getPiece(position_North) == null || board.getPiece(position_North).getTeamColor() != pieceColor) {
                        ChessMove validMove = new ChessMove(myPosition, position_North, null);
                        possibleMoves.add(validMove);
                    }
                    if (board.getPiece(position_North) != null) {
                        hazard_N = true;
                    }
                }

                //  S O U T H
                if (!hazard_S) {
                    ChessPosition position_South = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn());

                    if (board.getPiece(position_South) == null || board.getPiece(position_South).getTeamColor() != pieceColor) {
                        ChessMove validMove = new ChessMove(myPosition, position_South, null);
                        possibleMoves.add(validMove);
                    }
                    if (board.getPiece(position_South) != null) {
                        hazard_S = true;
                    }
                }

                //  E A S T
                if (!hazard_E) {
                    ChessPosition position_East = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + i);

                    if (board.getPiece(position_East) == null || board.getPiece(position_East).getTeamColor() != pieceColor) {
                        ChessMove validMove = new ChessMove(myPosition, position_East, null);
                        possibleMoves.add(validMove);
                    }
                    if (board.getPiece(position_East) != null) {
                        hazard_E = true;
                    }
                }

                //  W E S T
                if (!hazard_W) {
                    ChessPosition position_West = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - i);

                    if (board.getPiece(position_West) == null || board.getPiece(position_West).getTeamColor() != pieceColor) {
                        ChessMove validMove = new ChessMove(myPosition, position_West, null);
                        possibleMoves.add(validMove);
                    }
                    if (board.getPiece(position_West) != null) {
                        hazard_W = true;
                    }
                }

                //  N O R T H   E A S T
                if (!hazard_NE) {
                    ChessPosition position_NorthEast = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i);

                    if (board.getPiece(position_NorthEast) == null || board.getPiece(position_NorthEast).getTeamColor() != pieceColor) {
                        ChessMove validMove = new ChessMove(myPosition, position_NorthEast, null);
                        possibleMoves.add(validMove);
                    }
                    if (board.getPiece(position_NorthEast) != null) {
                        hazard_NE = true;
                    }
                }

                //  N O R T H   W E S T
                if (!hazard_NW) {
                    ChessPosition position_NorthWest = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - i);

                    if (board.getPiece(position_NorthWest) == null || board.getPiece(position_NorthWest).getTeamColor() != pieceColor) {
                        ChessMove validMove = new ChessMove(myPosition, position_NorthWest, null);
                        possibleMoves.add(validMove);
                    }
                    if (board.getPiece(position_NorthWest) != null) {
                        hazard_NW = true;
                    }
                }

                //  S O U T H   E A S T
                if (!hazard_SE) {
                    ChessPosition position_SouthEast = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + i);

                    if (board.getPiece(position_SouthEast) == null || board.getPiece(position_SouthEast).getTeamColor() != pieceColor) {
                        ChessMove validMove = new ChessMove(myPosition, position_SouthEast, null);
                        possibleMoves.add(validMove);
                    }
                    if (board.getPiece(position_SouthEast) != null) {
                        hazard_SE = true;
                    }
                }

                //  S O U T H   W E S T
                if (!hazard_SW) {
                    ChessPosition position_SouthWest = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - i);

                    if (board.getPiece(position_SouthWest) == null || board.getPiece(position_SouthWest).getTeamColor() != pieceColor) {
                        ChessMove validMove = new ChessMove(myPosition, position_SouthWest, null);
                        possibleMoves.add(validMove);
                    }
                    if (board.getPiece(position_SouthWest) != null) {
                        hazard_SW = true;
                    }
                }

            }

        }

        else if (type == PieceType.KNIGHT) {

            //  ++
            if (!(myPosition.getRow() + 2 > 8) && !(myPosition.getColumn() + 1 > 8)) {
                ChessPosition otherPosition = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() + 1);
                if (board.getPiece(otherPosition) == null || board.getPiece(otherPosition).getTeamColor() != pieceColor) {
                    ChessMove validMove = new ChessMove(myPosition, otherPosition, null);
                    possibleMoves.add(validMove);
                }
            }

            if (!(myPosition.getRow() + 1 > 8) && !(myPosition.getColumn() + 2 > 8)) {
                ChessPosition otherPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 2);
                if (board.getPiece(otherPosition) == null || board.getPiece(otherPosition).getTeamColor() != pieceColor) {
                    ChessMove validMove = new ChessMove(myPosition, otherPosition, null);
                    possibleMoves.add(validMove);
                }
            }

            //  +-
            if (!(myPosition.getRow() + 2 > 8) && !(myPosition.getColumn() - 1 < 1)) {
                ChessPosition otherPosition = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() - 1);
                if (board.getPiece(otherPosition) == null || board.getPiece(otherPosition).getTeamColor() != pieceColor) {
                    ChessMove validMove = new ChessMove(myPosition, otherPosition, null);
                    possibleMoves.add(validMove);
                }
            }

            if (!(myPosition.getRow() + 1 > 8) && !(myPosition.getColumn() - 2 < 1)) {
                ChessPosition otherPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 2);
                if (board.getPiece(otherPosition) == null || board.getPiece(otherPosition).getTeamColor() != pieceColor) {
                    ChessMove validMove = new ChessMove(myPosition, otherPosition, null);
                    possibleMoves.add(validMove);
                }
            }

            //  -+
            if (!(myPosition.getRow() - 2 < 1) && !(myPosition.getColumn() + 1 > 8)) {
                ChessPosition otherPosition = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() + 1);
                if (board.getPiece(otherPosition) == null || board.getPiece(otherPosition).getTeamColor() != pieceColor) {
                    ChessMove validMove = new ChessMove(myPosition, otherPosition, null);
                    possibleMoves.add(validMove);
                }
            }

            if (!(myPosition.getRow() - 1 < 1) && !(myPosition.getColumn() + 2 > 8)) {
                ChessPosition otherPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 2);
                if (board.getPiece(otherPosition) == null || board.getPiece(otherPosition).getTeamColor() != pieceColor) {
                    ChessMove validMove = new ChessMove(myPosition, otherPosition, null);
                    possibleMoves.add(validMove);
                }
            }

            //  --
            if (!(myPosition.getRow() - 2 < 1) && !(myPosition.getColumn() - 1 < 1)) {
                ChessPosition otherPosition = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() - 1);
                if (board.getPiece(otherPosition) == null || board.getPiece(otherPosition).getTeamColor() != pieceColor) {
                    ChessMove validMove = new ChessMove(myPosition, otherPosition, null);
                    possibleMoves.add(validMove);
                }
            }

            if (!(myPosition.getRow() - 1 < 1) && !(myPosition.getColumn() - 2 < 1)) {
                ChessPosition otherPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 2);
                if (board.getPiece(otherPosition) == null || board.getPiece(otherPosition).getTeamColor() != pieceColor) {
                    ChessMove validMove = new ChessMove(myPosition, otherPosition, null);
                    possibleMoves.add(validMove);
                }
            }

        }

        else if (type == PieceType.KING) {

            boolean hazard_N = false;
            boolean hazard_S = false;
            boolean hazard_E = false;
            boolean hazard_W = false;

            if (myPosition.getRow() + 1 > 8) {
                hazard_N = true;
            }
            if (myPosition.getRow() - 1 < 1) {
                hazard_S = true;
            }
            if (myPosition.getColumn() + 1 > 8) {
                hazard_E = true;
            }
            if (myPosition.getColumn() - 1 < 1) {
                hazard_W = true;
            }



        }

        else if (type == PieceType.PAWN) {}

        return possibleMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
