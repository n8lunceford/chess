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

    private ArrayList<ChessMove> tresCaballeros(ChessBoard board, ChessPosition myPosition, int theRow, int theColumn) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        boolean blocked = false;
        for (int i = 1; i < 8; i++) {
            if (myPosition.getRow() + (i * theRow) > 8
                    || myPosition.getRow() + (i * theRow) < 1
                    || myPosition.getColumn() + (i * theColumn) > 8
                    || myPosition.getColumn() + (i * theColumn) < 1) {
                blocked = true;
            }
            if (!blocked) {
                ChessPosition otherPosition = new ChessPosition(myPosition.getRow() + (i * theRow), myPosition.getColumn() + (i * theColumn));
                if (board.getPiece(otherPosition) == null || board.getPiece(otherPosition).getTeamColor() != pieceColor) {
                    ChessMove validMove = new ChessMove(myPosition, otherPosition, null);
                    possibleMoves.add(validMove);
                }
                if (board.getPiece(otherPosition) != null) {
                    blocked = true;
                }
            }
        }
        return possibleMoves;
    }

    private ArrayList<ChessMove> cardinal(ChessBoard board, ChessPosition myPosition, int one, int two, int three, int four, int five, int six, int seven, int eight) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        ArrayList<ChessMove> northMoves = tresCaballeros(board, myPosition, one, two);
        ArrayList<ChessMove> southMoves = tresCaballeros(board, myPosition, three, four);
        ArrayList<ChessMove> eastMoves = tresCaballeros(board, myPosition, five, six);
        ArrayList<ChessMove> westMoves = tresCaballeros(board, myPosition, seven, eight);
        for (ChessMove thisMove : northMoves) {
            possibleMoves.add(thisMove);
        }
        for (ChessMove thisMove : southMoves) {
            possibleMoves.add(thisMove);
        }
        for (ChessMove thisMove : eastMoves) {
            possibleMoves.add(thisMove);
        }
        for (ChessMove thisMove : westMoves) {
            possibleMoves.add(thisMove);
        }
        return possibleMoves;
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
            ArrayList<ChessMove> allWays = cardinal(board, myPosition, 1, 0, -1, 0, 0, 1, 0, -1);
            for (ChessMove thisMove : allWays) {
                possibleMoves.add(thisMove);
            }
        }

        else if (type == PieceType.BISHOP) {
            ArrayList<ChessMove> allWays = cardinal(board, myPosition, 1, 1, 1, -1, -1, 1, -1, -1);
            for (ChessMove thisMove : allWays) {
                possibleMoves.add(thisMove);
            }
        }

        else if (type == PieceType.QUEEN) {


            ArrayList<ChessMove> allWays = cardinal(board, myPosition, 1, 0, -1, 0, 0, 1, 0, -1);
            for (ChessMove thisMove : allWays) {
                possibleMoves.add(thisMove);
            }
            ArrayList<ChessMove> otherWays = cardinal(board, myPosition, 1, 1, 1, -1, -1, 1, -1, -1);
            for (ChessMove thisMove : otherWays) {
                possibleMoves.add(thisMove);
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

            boolean hazardN = false;
            boolean hazardS = false;
            boolean hazardE = false;
            boolean hazardW = false;

            if (myPosition.getRow() + 1 > 8) {
                hazardN = true;
            }
            if (myPosition.getRow() - 1 < 1) {
                hazardS = true;
            }
            if (myPosition.getColumn() + 1 > 8) {
                hazardE = true;
            }
            if (myPosition.getColumn() - 1 < 1) {
                hazardW = true;
            }

            if (!hazardN && !hazardE) {
                ChessPosition otherPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
                if (board.getPiece(otherPosition) == null || board.getPiece(otherPosition).getTeamColor() != pieceColor) {
                    ChessMove validMove = new ChessMove(myPosition, otherPosition, null);
                    possibleMoves.add(validMove);
                }
            }

            if (!hazardN && !hazardW) {
                ChessPosition otherPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
                if (board.getPiece(otherPosition) == null || board.getPiece(otherPosition).getTeamColor() != pieceColor) {
                    ChessMove validMove = new ChessMove(myPosition, otherPosition, null);
                    possibleMoves.add(validMove);
                }
            }

            if (!hazardS && !hazardE) {
                ChessPosition otherPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
                if (board.getPiece(otherPosition) == null || board.getPiece(otherPosition).getTeamColor() != pieceColor) {
                    ChessMove validMove = new ChessMove(myPosition, otherPosition, null);
                    possibleMoves.add(validMove);
                }
            }

            if (!hazardS && !hazardW) {
                ChessPosition otherPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
                if (board.getPiece(otherPosition) == null || board.getPiece(otherPosition).getTeamColor() != pieceColor) {
                    ChessMove validMove = new ChessMove(myPosition, otherPosition, null);
                    possibleMoves.add(validMove);
                }
            }

            if (!hazardN) {
                ChessPosition otherPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
                if (board.getPiece(otherPosition) == null || board.getPiece(otherPosition).getTeamColor() != pieceColor) {
                    ChessMove validMove = new ChessMove(myPosition, otherPosition, null);
                    possibleMoves.add(validMove);
                }
            }

            if (!hazardS) {
                ChessPosition otherPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
                if (board.getPiece(otherPosition) == null || board.getPiece(otherPosition).getTeamColor() != pieceColor) {
                    ChessMove validMove = new ChessMove(myPosition, otherPosition, null);
                    possibleMoves.add(validMove);
                }
            }

            if (!hazardE) {
                ChessPosition otherPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1);
                if (board.getPiece(otherPosition) == null || board.getPiece(otherPosition).getTeamColor() != pieceColor) {
                    ChessMove validMove = new ChessMove(myPosition, otherPosition, null);
                    possibleMoves.add(validMove);
                }
            }

            if (!hazardW) {
                ChessPosition otherPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1);
                if (board.getPiece(otherPosition) == null || board.getPiece(otherPosition).getTeamColor() != pieceColor) {
                    ChessMove validMove = new ChessMove(myPosition, otherPosition, null);
                    possibleMoves.add(validMove);
                }
            }
        }

        else if (type == PieceType.PAWN) {

            if (pieceColor == ChessGame.TeamColor.WHITE) {

                ChessPosition otherPosition;
                ChessPosition secondPosition;

                if (myPosition.getRow() == 2) {
                    otherPosition = new ChessPosition(3, myPosition.getColumn());
                    secondPosition = new ChessPosition(4, myPosition.getColumn());
                    if (board.getPiece(otherPosition) == null) {
                        ChessMove validMove = new ChessMove(myPosition, otherPosition, null);
                        possibleMoves.add(validMove);
                        if (board.getPiece(secondPosition) == null) {
                            validMove = new ChessMove(myPosition, secondPosition, null);
                            possibleMoves.add(validMove);
                        }
                    }
                }
                else if (myPosition.getRow() == 7) {
                    otherPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
                    if (board.getPiece(otherPosition) == null) {
                        /**
                         *  C O D E   F O R   P R O M O T I O N   P I E C E
                         */
                        ChessMove validMove = new ChessMove(myPosition, otherPosition, PieceType.ROOK);
                        possibleMoves.add(validMove);
                        validMove = new ChessMove(myPosition, otherPosition, PieceType.BISHOP);
                        possibleMoves.add(validMove);
                        validMove = new ChessMove(myPosition, otherPosition, PieceType.QUEEN);
                        possibleMoves.add(validMove);
                        validMove = new ChessMove(myPosition, otherPosition, PieceType.KNIGHT);
                        possibleMoves.add(validMove);
                    }
                }
                else if (myPosition.getRow() >= 3 && myPosition.getRow() <= 6) {
                    otherPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
                    if (board.getPiece(otherPosition) == null) {
                        ChessMove validMove = new ChessMove(myPosition, otherPosition, null);
                        possibleMoves.add(validMove);
                    }
                }

                /**
                 *  S I D E   C A P T U R E
                 */
                if (myPosition.getColumn() + 1 <= 8) {
                    otherPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
                    if (board.getPiece(otherPosition) != null && board.getPiece(otherPosition).getTeamColor() != pieceColor) {
                        if (otherPosition.getRow() == 8) {
                            /**
                             *  C O D E   F O R   P R O M O T I O N   P I E C E
                             */
                            ChessMove validMove = new ChessMove(myPosition, otherPosition, PieceType.ROOK);
                            possibleMoves.add(validMove);
                            validMove = new ChessMove(myPosition, otherPosition, PieceType.BISHOP);
                            possibleMoves.add(validMove);
                            validMove = new ChessMove(myPosition, otherPosition, PieceType.QUEEN);
                            possibleMoves.add(validMove);
                            validMove = new ChessMove(myPosition, otherPosition, PieceType.KNIGHT);
                            possibleMoves.add(validMove);
                        } else {
                            ChessMove validMove = new ChessMove(myPosition, otherPosition, null);
                            possibleMoves.add(validMove);
                        }
                    }
                }
                if (myPosition.getColumn() - 1 >= 1) {
                    otherPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
                    if (board.getPiece(otherPosition) != null && board.getPiece(otherPosition).getTeamColor() != pieceColor) {
                        if (otherPosition.getRow() == 8) {
                            /**
                             *  C O D E   F O R   P R O M O T I O N   P I E C E
                             */
                            ChessMove validMove = new ChessMove(myPosition, otherPosition, PieceType.ROOK);
                            possibleMoves.add(validMove);
                            validMove = new ChessMove(myPosition, otherPosition, PieceType.BISHOP);
                            possibleMoves.add(validMove);
                            validMove = new ChessMove(myPosition, otherPosition, PieceType.QUEEN);
                            possibleMoves.add(validMove);
                            validMove = new ChessMove(myPosition, otherPosition, PieceType.KNIGHT);
                            possibleMoves.add(validMove);
                        } else {
                            ChessMove validMove = new ChessMove(myPosition, otherPosition, null);
                            possibleMoves.add(validMove);
                        }
                    }
                }
            }



            else if (pieceColor == ChessGame.TeamColor.BLACK) {

                ChessPosition otherPosition;
                ChessPosition secondPosition;

                if (myPosition.getRow() == 7) {
                    otherPosition = new ChessPosition(6, myPosition.getColumn());
                    secondPosition = new ChessPosition(5, myPosition.getColumn());
                    if (board.getPiece(otherPosition) == null) {
                        ChessMove validMove = new ChessMove(myPosition, otherPosition, null);
                        possibleMoves.add(validMove);
                        if (board.getPiece(secondPosition) == null) {
                            validMove = new ChessMove(myPosition, secondPosition, null);
                            possibleMoves.add(validMove);
                        }
                    }
                }
                else if (myPosition.getRow() == 2) {
                    otherPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
                    if (board.getPiece(otherPosition) == null) {
                        /**
                         *  C O D E   F O R   P R O M O T I O N   P I E C E
                         */
                        ChessMove validMove = new ChessMove(myPosition, otherPosition, PieceType.ROOK);
                        possibleMoves.add(validMove);
                        validMove = new ChessMove(myPosition, otherPosition, PieceType.BISHOP);
                        possibleMoves.add(validMove);
                        validMove = new ChessMove(myPosition, otherPosition, PieceType.QUEEN);
                        possibleMoves.add(validMove);
                        validMove = new ChessMove(myPosition, otherPosition, PieceType.KNIGHT);
                        possibleMoves.add(validMove);
                    }
                }
                else if (myPosition.getRow() >= 3 && myPosition.getRow() <= 6) {
                    otherPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
                    if (board.getPiece(otherPosition) == null) {
                        ChessMove validMove = new ChessMove(myPosition, otherPosition, null);
                        possibleMoves.add(validMove);
                    }
                }

                /**
                 *  S I D E   C A P T U R E
                 */
                if (myPosition.getColumn() + 1 <= 8) {
                    otherPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
                    if (board.getPiece(otherPosition) != null && board.getPiece(otherPosition).getTeamColor() != pieceColor) {
                        if (otherPosition.getRow() == 1) {
                            /**
                             *  C O D E   F O R   P R O M O T I O N   P I E C E
                             */
                            ChessMove validMove = new ChessMove(myPosition, otherPosition, PieceType.ROOK);
                            possibleMoves.add(validMove);
                            validMove = new ChessMove(myPosition, otherPosition, PieceType.BISHOP);
                            possibleMoves.add(validMove);
                            validMove = new ChessMove(myPosition, otherPosition, PieceType.QUEEN);
                            possibleMoves.add(validMove);
                            validMove = new ChessMove(myPosition, otherPosition, PieceType.KNIGHT);
                            possibleMoves.add(validMove);
                        } else {
                            ChessMove validMove = new ChessMove(myPosition, otherPosition, null);
                            possibleMoves.add(validMove);
                        }
                    }
                }
                if (myPosition.getColumn() - 1 >= 1) {
                    otherPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
                    if (board.getPiece(otherPosition) != null && board.getPiece(otherPosition).getTeamColor() != pieceColor) {
                        if (otherPosition.getRow() == 1) {
                            /**
                             *  C O D E   F O R   P R O M O T I O N   P I E C E
                             */
                            ChessMove validMove = new ChessMove(myPosition, otherPosition, PieceType.ROOK);
                            possibleMoves.add(validMove);
                            validMove = new ChessMove(myPosition, otherPosition, PieceType.BISHOP);
                            possibleMoves.add(validMove);
                            validMove = new ChessMove(myPosition, otherPosition, PieceType.QUEEN);
                            possibleMoves.add(validMove);
                            validMove = new ChessMove(myPosition, otherPosition, PieceType.KNIGHT);
                            possibleMoves.add(validMove);
                        } else {
                            ChessMove validMove = new ChessMove(myPosition, otherPosition, null);
                            possibleMoves.add(validMove);
                        }
                    }
                }
            }
        }
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
