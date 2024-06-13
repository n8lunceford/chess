package ui;

import facade.Observer;
import facade.WebSocketClient;
import model.GameData;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGame;
import websocket.messages.Notification;

import java.io.PrintStream;
import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.SET_TEXT_COLOR_RED;
import static ui.EscapeSequences.SET_TEXT_COLOR_WHITE;

public class GameUI implements Observer {

    WebSocketClient client;
    PrintStream out;
    String color;

    GameUI() throws Exception {
        client = new WebSocketClient(this);
    }

    public void setColor(String greyArea) {
        color = greyArea;
    }

    public void gamePlayUI(/**PrintStream out, */String authToken) {
        Scanner scanner = new Scanner(System.in);
        out.print(SET_TEXT_COLOR_RED);
        out.print("[LOGGED OUT] >>> ");
        String input = scanner.next();
        if (!Objects.equals(input, "quit")) {
            if (Objects.equals(input, "help")) {
                PregameUI.tableWriter(out, "redraw", "chessboard");
                PregameUI.tableWriter(out, "leave", "game");
                PregameUI.tableWriter(out, "move", "piece");
                PregameUI.tableWriter(out, "resign", "from game");
                PregameUI.tableWriter(out, "highlight", "legal moves");
                PregameUI.tableWriter(out, "help", "with possible commands");
                gamePlayUI(authToken);
            }
            else if (input.equals("redraw")) {
                try {

                }
                catch (Exception exception) {
                    out.print(exception.getMessage());
                    out.println();
                    gamePlayUI(authToken);
                }
            }
            else if (input.startsWith("leave")) {
                try {

                }
                catch (Exception exception) {
                    out.print(exception.getMessage());
                    out.println();
                    //gamePlayUI(out);
                }
            }
            else if (input.startsWith("move")) {
                try {

                }
                catch (Exception exception) {
                    out.print(exception.getMessage());
                    out.println();
                    //gamePlayUI(out);
                }
            }
            else if (input.startsWith("resign")) {
                try {

                }
                catch (Exception exception) {
                    out.print(exception.getMessage());
                    out.println();
                    //gamePlayUI(out);
                }
            }
            else if (input.startsWith("highlight")) {
                String pos = scanner.next();
                int row;
                int col;
                if (pos.startsWith("a")) {
                    row = converter(pos, "a");
                    col = 1;
                }
                else if (pos.startsWith("b")) {
                    row = converter(pos, "b");
                    col = 2;
                }
                else if (pos.startsWith("c")) {
                    row = converter(pos, "c");
                    col = 3;
                }
                else if (pos.startsWith("d")) {
                    row = converter(pos, "d");
                    col = 4;
                }
                else if (pos.startsWith("e")) {
                    row = converter(pos, "e");
                    col = 5;
                }
                else if (pos.startsWith("f")) {
                    row = converter(pos, "f");
                    col = 6;
                }
                else if (pos.startsWith("g")) {
                    row = converter(pos, "g");
                    col = 7;
                }
                else if (pos.startsWith("h")) {
                    row = converter(pos, "h");
                    col = 8;
                }
                else {
                    row = 0;
                    col = 0;
                }
                try {
                    /**
                     * makeMove via WebSocketHandler
                     */
                }
                catch (Exception exception) {
                    out.print(exception.getMessage());
                    out.println();
                    gamePlayUI(authToken);
                }
            }
            else {
                PregameUI.pleaseTryAgain(out);
                //gamePlayUI(out);
            }
        }
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
        if (Objects.equals(color, "BLACK")) {
            GamePlayDrawing.printBoard(out, false, loadGame.getGame(), GamePlayDrawing.cleanLook());
        }
        else {
            GamePlayDrawing.printBoard(out, true, loadGame.getGame(), GamePlayDrawing.cleanLook());
        }
    }

    @Override
    public void errorMessage(ErrorMessage errorMessage) {
        out.print(errorMessage.getErrorMessage());
        out.println();
    }

    @Override
    public void notification(Notification notification) {
        out.print(notification.getMessage());
        out.println();
    }
}
