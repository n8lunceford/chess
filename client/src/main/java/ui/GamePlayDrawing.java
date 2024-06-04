package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static ui.EscapeSequences.*;

public class GamePlayDrawing {

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_CHARS = 1;

    private static final String WHITE_KING = EscapeSequences.WHITE_KING;
    private static final String WHITE_QUEEN = EscapeSequences.WHITE_QUEEN;
    private static final String WHITE_BISHOP = EscapeSequences.WHITE_BISHOP;
    private static final String WHITE_KNIGHT = EscapeSequences.WHITE_KNIGHT;
    private static final String WHITE_ROOK = EscapeSequences.WHITE_ROOK;
    private static final String WHITE_PAWN = EscapeSequences.WHITE_PAWN;
    private static final String BLACK_KING = EscapeSequences.BLACK_KING;
    private static final String BLACK_QUEEN = EscapeSequences.BLACK_QUEEN;
    private static final String BLACK_BISHOP = EscapeSequences.BLACK_BISHOP;
    private static final String BLACK_KNIGHT = EscapeSequences.BLACK_KNIGHT;
    private static final String BLACK_ROOK = EscapeSequences.BLACK_ROOK;
    private static final String BLACK_PAWN = EscapeSequences.BLACK_PAWN;
    private static final String EMPTY = EscapeSequences.EMPTY;

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        ChessBoard myBoard = new ChessBoard();
        myBoard.resetBoard();
        boolean whiteTurn = true;
        printBoard(out, whiteTurn, myBoard);
        spacer(out);
        printBoard(out, !whiteTurn, myBoard);
    }

    public static void printBoard(PrintStream out, boolean whiteTurn, ChessBoard myBoard) {
        out.print(ERASE_SCREEN);
        drawHeaders(out, whiteTurn);
        drawBoard(out, whiteTurn, myBoard);
        drawHeaders(out, whiteTurn);
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }

    private static String[][] whiteBoard(ChessBoard board) {
        String[][] myBoard = new String[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                myBoard[row][col] = thisPiece(board.getPiece(new ChessPosition(row + 1, col + 1)));
            }
        }
        return myBoard;
    }

    private static String[][] blackBoard(ChessBoard board) {
        String[][] myBoard = new String[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                myBoard[row][col] = thisPiece(board.getPiece(new ChessPosition(8 - row, 8 - col)));
            }
        }
        return myBoard;
    }

    private static String thisPiece(ChessPiece chessPiece) {
        if (chessPiece != null) {
            if (chessPiece.getPieceType() == ChessPiece.PieceType.KING && chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE)
                return WHITE_KING;
            else if (chessPiece.getPieceType() == ChessPiece.PieceType.QUEEN && chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE)
                return WHITE_QUEEN;
            else if (chessPiece.getPieceType() == ChessPiece.PieceType.BISHOP && chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE)
                return WHITE_BISHOP;
            else if (chessPiece.getPieceType() == ChessPiece.PieceType.KNIGHT && chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE)
                return WHITE_KNIGHT;
            else if (chessPiece.getPieceType() == ChessPiece.PieceType.ROOK && chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE)
                return WHITE_ROOK;
            else if (chessPiece.getPieceType() == ChessPiece.PieceType.PAWN && chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE)
                return WHITE_PAWN;
            else if (chessPiece.getPieceType() == ChessPiece.PieceType.KING && chessPiece.getTeamColor() == ChessGame.TeamColor.BLACK)
                return BLACK_KING;
            else if (chessPiece.getPieceType() == ChessPiece.PieceType.QUEEN && chessPiece.getTeamColor() == ChessGame.TeamColor.BLACK)
                return BLACK_QUEEN;
            else if (chessPiece.getPieceType() == ChessPiece.PieceType.BISHOP && chessPiece.getTeamColor() == ChessGame.TeamColor.BLACK)
                return BLACK_BISHOP;
            else if (chessPiece.getPieceType() == ChessPiece.PieceType.KNIGHT && chessPiece.getTeamColor() == ChessGame.TeamColor.BLACK)
                return BLACK_KNIGHT;
            else if (chessPiece.getPieceType() == ChessPiece.PieceType.ROOK && chessPiece.getTeamColor() == ChessGame.TeamColor.BLACK)
                return BLACK_ROOK;
            else if (chessPiece.getPieceType() == ChessPiece.PieceType.PAWN && chessPiece.getTeamColor() == ChessGame.TeamColor.BLACK)
                return BLACK_PAWN;
        }
        return EMPTY;
    }

    private static void drawHeaders(PrintStream out, boolean whiteTurn) {

        setLightGrey(out);
        String[] headers;
        if (whiteTurn) {
            headers = new String[]{" a" + "\u2003", " b" + "\u2003", " c" + "\u2003", " d" + "\u2003", " e" + "\u2003", " f" + "\u2003", " g" + "\u2003", " h" + "\u2003"};
        }
        else {
            headers = new String[]{" h" + "\u2003", " g" + "\u2003", " f" + "\u2003", " e" + "\u2003", " d" + "\u2003", " c" + "\u2003", " b" + "\u2003", " a" + "\u2003"};
        }
        out.print(EscapeSequences.EMPTY);
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            drawHeader(out, headers[boardCol]);
        }
        out.print(EscapeSequences.EMPTY);
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
        out.println();
    }

    private static void spacer(PrintStream out) {
        setBlack(out);
        String[] headers;
        headers = new String[]{EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY};

        out.print(EscapeSequences.EMPTY);
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            out.print(headers[boardCol]);
        }
        out.print(EscapeSequences.EMPTY);
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
        out.println();
    }

    private static void drawHeader(PrintStream out, String headerText) {
        printHeaderText(out, headerText);
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(player);
        setLightGrey(out);
    }

    private static void drawBoard(PrintStream out, boolean whiteTurn, ChessBoard boardValues) {

        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
            boolean isEven = (boardRow % 2 == 0);
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(SET_TEXT_COLOR_BLACK);
            if (whiteTurn) {
                out.print(" " + (8 - boardRow) + "\u2003");
            }
            else {
                out.print(" " + (boardRow + 1) + "\u2003");
            }
            setLightGrey(out);
            drawRowOfSquares(out, boardRow, isEven, whiteTurn, boardValues);
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(SET_TEXT_COLOR_BLACK);
            if (whiteTurn) {
                out.print(" " + (8 - boardRow) + "\u2003");
            }
            else {
                out.print(" " + (boardRow + 1) + "\u2003");
            }
            setLightGrey(out);

            out.print(RESET_BG_COLOR);
            out.print(RESET_TEXT_COLOR);

            out.println();
        }
    }

    private static void drawRowOfSquares(PrintStream out, int boardRow, boolean isEven, boolean whiteTurn, ChessBoard boardValues) {

        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_CHARS; ++squareRow) {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                boolean isWhite;
                if ((isEven && boardCol % 2 == 0) || (!isEven && boardCol % 2 == 1)) {
                    setWhite(out);
                    isWhite = true;
                }
                else {
                    setBlack(out);
                    isWhite = false;
                }
                String[][] myBoard;
                if (whiteTurn) {
                    myBoard = whiteBoard(boardValues);
                }
                else {
                    myBoard = blackBoard(boardValues);
                }
                printPlayer(out, myBoard[7 - boardRow][boardCol], isWhite);

                if (isWhite)
                    setBlack(out);
                else
                    setWhite(out);
            }
        }
    }

    private static void printPlayer(PrintStream out, String player, boolean isWhite) {

        if (isWhite) {
            out.print(SET_BG_COLOR_WHITE);
        }
        else {
            out.print(SET_BG_COLOR_BLACK);
        }
        if (Objects.equals(player, " ♔ ") || Objects.equals(player, " ♕ ")
                || Objects.equals(player, " ♗ ") || Objects.equals(player, " ♘ ")
                || Objects.equals(player, " ♖ ") || Objects.equals(player, " ♙ ")) {
            out.print(SET_TEXT_COLOR_RED);
        }
        else if (Objects.equals(player, " ♚ ") || Objects.equals(player, " ♛ ")
                || Objects.equals(player, " ♝ ") || Objects.equals(player, " ♞ ")
                || Objects.equals(player, " ♜ ") || Objects.equals(player, " ♟ ")) {
            out.print("\u001b" + "[38;5;" + "25m");
        }
        out.print(player);
    }

    private static void setLightGrey(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_LIGHT_GREY);
    }

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }
}
