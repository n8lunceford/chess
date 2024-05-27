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

    private ArrayList<ChessMove> cardinal(ChessBoard board, ChessPosition myPosition, boolean isRook, boolean isKing, boolean isKnight) {
        int one = 1;
        int two = 0;
        int three = -1;
        int four = 0;
        int five = 0;
        int six = 1;
        int seven = 0;
        int eight = -1;
        if (!isRook && !isKing && !isKnight) {
            two = 1;
            three = 1;
            four = -1;
            five = -1;
            seven = -1;
        }
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        if (!isKing && !isKnight) {
            ArrayList<ChessMove> northMoves = tresCaballeros(board, myPosition, one, two);
            ArrayList<ChessMove> southMoves = tresCaballeros(board, myPosition, three, four);
            ArrayList<ChessMove> eastMoves = tresCaballeros(board, myPosition, five, six);
            ArrayList<ChessMove> westMoves = tresCaballeros(board, myPosition, seven, eight);
            possibleMoves.addAll(northMoves);
            possibleMoves.addAll(southMoves);
            possibleMoves.addAll(eastMoves);
            possibleMoves.addAll(westMoves);
        }
        else {
            ArrayList<ChessMove> positive = knightCollection(board, myPosition, true, isKnight);
            ArrayList<ChessMove> negative = knightCollection(board, myPosition, false, isKnight);
            possibleMoves.addAll(positive);
            possibleMoves.addAll(negative);
        }
        return possibleMoves;
    }

    private ArrayList<ChessMove> sirThomas(ChessBoard board, ChessPosition myPosition, int i, int j) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        if (!(myPosition.getRow() + i > 8) && !(myPosition.getColumn() + j > 8)
                && !(myPosition.getRow() + i < 1) && !(myPosition.getColumn() + j < 1)) {
            ChessPosition otherPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j);
            if (board.getPiece(otherPosition) == null || board.getPiece(otherPosition).getTeamColor() != pieceColor) {
                ChessMove validMove = new ChessMove(myPosition, otherPosition, null);
                possibleMoves.add(validMove);
            }
        }
        return possibleMoves;
    }

    private ArrayList<ChessMove> knightCollection(ChessBoard board, ChessPosition myPosition, boolean positive, boolean isKnight) {
        int multiplier = 1;
        if (!positive) {
            multiplier = -1;
        }
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();

        ArrayList<ChessMove> neOne;
        ArrayList<ChessMove> neTwo;
        ArrayList<ChessMove> nwOne;
        ArrayList<ChessMove> nwTwo;

        if (isKnight) {
            neOne = sirThomas(board, myPosition, 2 * multiplier, 1);       //      two/one
            neTwo = sirThomas(board, myPosition, multiplier, 2);              //      one/two
            nwOne = sirThomas(board, myPosition, 2 * multiplier, -1);      //      two/-one
            nwTwo = sirThomas(board, myPosition, multiplier, -2);             //      one/-two
        }
        else {
            neOne = sirThomas(board, myPosition, multiplier, multiplier);
            neTwo = sirThomas(board, myPosition, multiplier, 0);
            nwOne = sirThomas(board, myPosition, multiplier, multiplier * -1);
            nwTwo = sirThomas(board, myPosition, 0, multiplier);
        }
        possibleMoves.addAll(neOne);
        possibleMoves.addAll(neTwo);
        possibleMoves.addAll(nwOne);
        possibleMoves.addAll(nwTwo);


        return possibleMoves;

    }

    private ArrayList<ChessMove> pawnShop(ChessBoard board, ChessPosition myPosition, boolean isWhite) {

        int startRow = 2;
        int multiplier = 1;
        if (!isWhite) {
            startRow = 7;
            multiplier = -1;
        }

        ArrayList<ChessMove> possibleMoves = new ArrayList<>();


        ChessPosition otherPosition;
        ChessPosition secondPosition;

        if (myPosition.getRow() == startRow) {
            otherPosition = new ChessPosition(startRow + multiplier, myPosition.getColumn());
            secondPosition = new ChessPosition(startRow + (multiplier * 2), myPosition.getColumn());
            if (board.getPiece(otherPosition) == null) {
                ChessMove validMove = new ChessMove(myPosition, otherPosition, null);
                possibleMoves.add(validMove);
                if (board.getPiece(secondPosition) == null) {
                    validMove = new ChessMove(myPosition, secondPosition, null);
                    possibleMoves.add(validMove);
                }
            }
        }
        else if (myPosition.getRow() == startRow + (multiplier * 5)) {
            otherPosition = new ChessPosition(myPosition.getRow() + multiplier, myPosition.getColumn());
            if (board.getPiece(otherPosition) == null) {
                /**
                 *  C O D E   F O R   P R O M O T I O N   P I E C E
                 */
                ArrayList<ChessMove> capture = pawnCapture(myPosition, otherPosition);
                possibleMoves.addAll(capture);
            }
        }
        else if (myPosition.getRow() >= 3 && myPosition.getRow() <= 6) {
            otherPosition = new ChessPosition(myPosition.getRow() + multiplier, myPosition.getColumn());
            if (board.getPiece(otherPosition) == null) {
                ChessMove validMove = new ChessMove(myPosition, otherPosition, null);
                possibleMoves.add(validMove);
            }
        }

        /**
         *  S I D E   C A P T U R E
         */
        if (myPosition.getColumn() + 1 <= 8) {
            otherPosition = new ChessPosition(myPosition.getRow() + multiplier, myPosition.getColumn() + 1);
            if (board.getPiece(otherPosition) != null && board.getPiece(otherPosition).getTeamColor() != pieceColor) {
                if (otherPosition.getRow() == startRow + (multiplier * 6)) {
                    /**
                     *  C O D E   F O R   P R O M O T I O N   P I E C E
                     */
                    ArrayList<ChessMove> capture = pawnCapture(myPosition, otherPosition);
                    possibleMoves.addAll(capture);
                } else {
                    ChessMove validMove = new ChessMove(myPosition, otherPosition, null);
                    possibleMoves.add(validMove);
                }
            }
        }
        if (myPosition.getColumn() - 1 >= 1) {
            otherPosition = new ChessPosition(myPosition.getRow() + multiplier, myPosition.getColumn() - 1);
            if (board.getPiece(otherPosition) != null && board.getPiece(otherPosition).getTeamColor() != pieceColor) {
                if (otherPosition.getRow() == startRow + (multiplier * 6)) {
                    /**
                     *  C O D E   F O R   P R O M O T I O N   P I E C E
                     */
                    ArrayList<ChessMove> capture = pawnCapture(myPosition, otherPosition);
                    possibleMoves.addAll(capture);
                } else {
                    ChessMove validMove = new ChessMove(myPosition, otherPosition, null);
                    possibleMoves.add(validMove);
                }
            }
        }
        return possibleMoves;
    }

    ArrayList<ChessMove> pawnCapture(ChessPosition myPosition, ChessPosition otherPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        possibleMoves.add(new ChessMove(myPosition, otherPosition, PieceType.ROOK));
        possibleMoves.add(new ChessMove(myPosition, otherPosition, PieceType.BISHOP));
        possibleMoves.add(new ChessMove(myPosition, otherPosition, PieceType.QUEEN));
        possibleMoves.add(new ChessMove(myPosition, otherPosition, PieceType.KNIGHT));
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
            ArrayList<ChessMove> allWays = cardinal(board, myPosition, true, false, false);
            possibleMoves.addAll(allWays);
        }
        else if (type == PieceType.BISHOP) {
            ArrayList<ChessMove> allWays = cardinal(board, myPosition, false, false, false);
            possibleMoves.addAll(allWays);
        }
        else if (type == PieceType.QUEEN) {


            ArrayList<ChessMove> allWays = cardinal(board, myPosition, true, false, false);
            possibleMoves.addAll(allWays);
            ArrayList<ChessMove> otherWays = cardinal(board, myPosition, false, false, false);
            possibleMoves.addAll(otherWays);


        }
        else if (type == PieceType.KNIGHT) {
            ArrayList<ChessMove> allWays = cardinal(board, myPosition, false, false, true);
            possibleMoves.addAll(allWays);
        }
        else if (type == PieceType.KING) {
            ArrayList<ChessMove> allWays = cardinal(board, myPosition, false, true, false);
            possibleMoves.addAll(allWays);
        }
        else if (type == PieceType.PAWN) {
            ArrayList<ChessMove> allWays;
            if (pieceColor == ChessGame.TeamColor.WHITE) {
                allWays = pawnShop(board, myPosition, true);
            }
            else {
                allWays = pawnShop(board, myPosition, false);
            }
            possibleMoves.addAll(allWays);
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
