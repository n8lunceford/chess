package ui;

import chess.*;
import com.google.gson.Gson;
import facade.Observer;
import facade.WebSocketClient;
import model.GameData;
import websocket.commands.*;
import websocket.messages.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class GameUI implements Observer {

    private WebSocketClient client;
    private PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    private String color;
    private int gameID;
    private ChessBoard board;
    private ChessGame chessGame;
    String authToken;

    GameUI() throws Exception {
        client = new WebSocketClient(this);
    }

    public void setValues(String greyArea) {
        color = greyArea;
    }

    public void startGame(String authToken, int myGameID) throws Exception {
        Connect connect = new Connect(authToken, myGameID);
        out.println();
        connect.setCommandType(UserGameCommand.CommandType.CONNECT);
        client.send(new Gson().toJson(connect));
        gameID = myGameID;
        this.authToken = authToken;
        gamePlayUI();
    }

    public void gamePlayUI() {
        Scanner scanner = new Scanner(System.in);
        out.print(SET_TEXT_COLOR_GREEN);
        out.print(SET_TEXT_BOLD);
        out.print(SET_TEXT_BLINKING);
        out.print("[IN GAME] >>> ");
        out.print(RESET_TEXT_BOLD_FAINT);
        out.print(RESET_TEXT_BLINKING);
        out.print(SET_TEXT_COLOR_WHITE);
        String input = scanner.next();
        if (!Objects.equals(input, "quit")) {
            if (Objects.equals(input, "help")) {
                PregameUI.tableWriter(out, "redraw", "chessboard");
                PregameUI.tableWriter(out, "leave", "game");
                PregameUI.tableWriter(out, "move", "piece");
                PregameUI.tableWriter(out, "resign", "from game");
                PregameUI.tableWriter(out, "highlight", "legal moves");
                PregameUI.tableWriter(out, "help", "with possible commands");
                gamePlayUI();
            } else if (input.equals("redraw")) {
                try {
                    if (Objects.equals(color, "BLACK")) {
                        GamePlayDrawing.printBoard(out, false, board, GamePlayDrawing.cleanLook());
                    }
                    else {
                        GamePlayDrawing.printBoard(out, true, board, GamePlayDrawing.cleanLook());
                    }
                    gamePlayUI();
                } catch (Exception exception) {
                    out.print(exception.getMessage());
                    out.println();
                    gamePlayUI();
                }
            }
            else if (input.startsWith("leave")) {
                try {
                    Leave leave = new Leave(authToken, gameID);
                    leave.setCommandType(UserGameCommand.CommandType.LEAVE);
                    client.send(new Gson().toJson(leave));
                } catch (Exception exception) {
                    out.print(exception.getMessage());
                    out.println();
                    gamePlayUI();
                }
            } else if (input.startsWith("move")) {
                theMove(out, scanner);
            }
            else if (input.startsWith("resign")) {
                try {
                    Resign resign = new Resign(authToken, gameID);
                    resign.setCommandType(UserGameCommand.CommandType.RESIGN);
                    client.send(new Gson().toJson(resign));
                    gamePlayUI();
                } catch (Exception exception) {
                    out.print(exception.getMessage());
                    out.println();
                    gamePlayUI();
                }
            } else if (input.startsWith("highlight")) {
                String pos = scanner.next();
                ChessPosition position = myPlace(pos);
                try {
                    if (Objects.equals(color, "BLACK")) {
                        TwoBools[][] legalMoves = GamePlayDrawing.potentialMoves(false, chessGame, position);
                        GamePlayDrawing.printBoard(out, false, board, legalMoves);
                    } else {
                        TwoBools[][] legalMoves = GamePlayDrawing.potentialMoves(true, chessGame, position);
                        GamePlayDrawing.printBoard(out, true, board, legalMoves);
                    }
                    gamePlayUI();
                } catch (Exception exception) {
                    out.print(exception.getMessage());
                    out.println();
                    gamePlayUI();
                }
            } else {
                PregameUI.pleaseTryAgain(out);
                gamePlayUI();
            }
        }
    }

    private void theMove(PrintStream out, Scanner scanner) {
        String one = scanner.next();
        String two = scanner.next();
        String three = "";
        ChessPosition start = myPlace(one);
        ChessPosition end = myPlace(two);
        ChessPiece.PieceType promotion = null;
        if (board.getPiece(start).getPieceType() == ChessPiece.PieceType.PAWN && (end.getRow() == 1 || end.getRow() == 8)) {
            three = scanner.next();
        }
        if (Objects.equals(three, "queen")) {
            promotion = ChessPiece.PieceType.QUEEN;
        } else if (Objects.equals(three, "knight")) {
            promotion = ChessPiece.PieceType.KNIGHT;
        } else if (Objects.equals(three, "bishop")) {
            promotion = ChessPiece.PieceType.BISHOP;
        } else if (Objects.equals(three, "rook")) {
            promotion = ChessPiece.PieceType.ROOK;
        }
        try {
            MakeMove makeMove = new MakeMove(authToken, gameID, new ChessMove(start, end, promotion));
            makeMove.setCommandType(UserGameCommand.CommandType.MAKE_MOVE);
            client.send(new Gson().toJson(makeMove));
            gamePlayUI();
        } catch (Exception exception) {
            out.print(exception.getMessage());
            out.println();
            gamePlayUI();
        }
    }



    private ChessPosition myPlace(String position) {
        int col;
        int row;
        if (position.startsWith("a")) {
            col = converter(position, "a");
            row = 1;
        }
        else if (position.startsWith("b")) {
            col = converter(position, "b");
            row = 2;
        }
        else if (position.startsWith("c")) {
            col = converter(position, "c");
            row = 3;
        }
        else if (position.startsWith("d")) {
            col = converter(position, "d");
            row = 4;
        }
        else if (position.startsWith("e")) {
            col = converter(position, "e");
            row = 5;
        }
        else if (position.startsWith("f")) {
            col = converter(position, "f");
            row = 6;
        }
        else if (position.startsWith("g")) {
            col = converter(position, "g");
            row = 7;
        }
        else if (position.startsWith("h")) {
            col = converter(position, "h");
            row = 8;
        }
        else {
            col = 0;
            row = 0;
        }
        return new ChessPosition(col, row);
    }



    private int converter(String input, String letter) {
        if (Objects.equals(input, letter + "1")) {
            return 1;
        }
        else if (Objects.equals(input, letter + "2")) {
            return 2;
        }
        else if (Objects.equals(input, letter + "3")) {
            return 3;
        }
        else if (Objects.equals(input, letter + "4")) {
            return 4;
        }
        else if (Objects.equals(input, letter + "5")) {
            return 5;
        }
        else if (Objects.equals(input, letter + "6")) {
            return 6;
        }
        else if (Objects.equals(input, letter + "7")) {
            return 7;
        }
        else if (Objects.equals(input, letter + "8")) {
            return 8;
        }
        else {
            return 0;
        }
    }

    @Override
    public void loadGame(LoadGame loadGame) {
        out.println();
        if (Objects.equals(color, "BLACK")) {
            GamePlayDrawing.printBoard(out, false, loadGame.getGame().getBoard(), GamePlayDrawing.cleanLook());
        }
        else {
            GamePlayDrawing.printBoard(out, true, loadGame.getGame().getBoard(), GamePlayDrawing.cleanLook());
        }
        chessGame = loadGame.getGame();
        board = chessGame.getBoard();
    }

    @Override
    public void errorMessage(ErrorMessage errorMessage) {
        out.println();
        out.print(errorMessage.getErrorMessage());
        out.println();
    }

    @Override
    public void notification(Notification notification) {
        out.println();
        out.print(notification.getMessage());
        out.println();
    }
}
