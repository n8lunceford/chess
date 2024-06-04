package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
//import java.util.Random;

import static ui.EscapeSequences.*;

public class GamePlayDrawing {

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_CHARS = 1;
    private static final int LINE_WIDTH_IN_CHARS = 0;
    /**
    private static final String WHITE_KING = " ♔ ";
    private static final String WHITE_QUEEN = " ♕ ";
    private static final String WHITE_BISHOP = " ♗ ";
    private static final String WHITE_KNIGHT = " ♘ ";
    private static final String WHITE_ROOK = " ♖ ";
    private static final String WHITE_PAWN = " ♙ ";
    private static final String BLACK_KING = " ♚ ";
    private static final String BLACK_QUEEN = " ♛ ";
    private static final String BLACK_BISHOP = " ♝ ";
    private static final String BLACK_KNIGHT = " ♞ ";
    private static final String BLACK_ROOK = " ♜ ";
    private static final String BLACK_PAWN = " ♟ ";
    private static final String EMPTY = " \u2003 ";
    */
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

        out.print(ERASE_SCREEN);

        drawHeaders(out);

        drawBoard(out);

        drawHeaders(out);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private String[][] whiteBoard(ChessBoard board) {
        String[][] myBoard = new String[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                myBoard[row][col] = thisPiece(board.getPiece(new ChessPosition(row + 1, col + 1)));
            }
        }
        return myBoard;
    }

    private String[][] blackBoard(ChessBoard board) {
        String[][] myBoard = new String[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                myBoard[row][col] = thisPiece(board.getPiece(new ChessPosition(8 - row, 8 - col)));
            }
        }
        return myBoard;
    }

    private String thisPiece(ChessPiece chessPiece) {
        if (chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            if (chessPiece.getPieceType() == ChessPiece.PieceType.KING)
                return WHITE_KING;
            else if (chessPiece.getPieceType() == ChessPiece.PieceType.QUEEN)
                return WHITE_QUEEN;
            else if (chessPiece.getPieceType() == ChessPiece.PieceType.BISHOP)
                return WHITE_BISHOP;
            else if (chessPiece.getPieceType() == ChessPiece.PieceType.KNIGHT)
                return WHITE_KNIGHT;
            else if (chessPiece.getPieceType() == ChessPiece.PieceType.ROOK)
                return WHITE_ROOK;
            else if (chessPiece.getPieceType() == ChessPiece.PieceType.PAWN)
                return WHITE_PAWN;
        }
        else if (chessPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            if (chessPiece.getPieceType() == ChessPiece.PieceType.KING)
                return BLACK_KING;
            else if (chessPiece.getPieceType() == ChessPiece.PieceType.QUEEN)
                return BLACK_QUEEN;
            else if (chessPiece.getPieceType() == ChessPiece.PieceType.BISHOP)
                return BLACK_BISHOP;
            else if (chessPiece.getPieceType() == ChessPiece.PieceType.KNIGHT)
                return BLACK_KNIGHT;
            else if (chessPiece.getPieceType() == ChessPiece.PieceType.ROOK)
                return BLACK_ROOK;
            else if (chessPiece.getPieceType() == ChessPiece.PieceType.PAWN)
                return BLACK_PAWN;
        }
        return EMPTY;
    }

    private static void drawHeaders(PrintStream out) {

        setLightGrey(out);

        String[] headers = { " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h " };
        out.print("   ");
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            drawHeader(out, headers[boardCol]);

            /**
            if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                out.print(EMPTY.repeat(LINE_WIDTH_IN_CHARS));
            }
            */
        }
        out.print("   ");
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

    private static void drawBoard(PrintStream out) {

        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
            boolean isEven = (boardRow % 2 == 0);
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(SET_TEXT_COLOR_BLACK);
            out.print(" " + (8 - boardRow) + " ");
            setLightGrey(out);
            drawRowOfSquares(out, isEven);
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(SET_TEXT_COLOR_BLACK);
            out.print(" " + (8 - boardRow) + " ");
            setLightGrey(out);

            out.print(RESET_BG_COLOR);
            out.print(RESET_TEXT_COLOR);

            out.println();
        }
    }

    private static void drawRowOfSquares(PrintStream out, boolean isEven) {

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

                if (squareRow == SQUARE_SIZE_IN_CHARS / 2) {
                    printPlayer(out, " N ", isWhite);
                }

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
            out.print(SET_TEXT_COLOR_BLACK);
        }
        else {
            out.print(SET_BG_COLOR_BLACK);
            out.print(SET_TEXT_COLOR_WHITE);
        }

        out.print(player);
        if (isWhite)
            setWhite(out);
        else
            setBlack(out);
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
