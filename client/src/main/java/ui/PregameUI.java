package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;
import facade.ServerFacade;
import model.GameData;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static ui.EscapeSequences.*;

public class PregameUI {

    private ServerFacade fake;

    public PregameUI() {
        fake = new ServerFacade(8080);
    }

    public void beginJourney() {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
        out.print("Welcome to the C S 240 chess server. Type \"help\" to begin.");
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
        out.println();
        preLoginUI(out);
    }

    public void preLoginUI(PrintStream out) {
        Scanner scanner = new Scanner(System.in);
        out.print(SET_TEXT_COLOR_RED);
        out.print("[LOGGED OUT] >>> ");
        String input = scanner.next();
        if (!Objects.equals(input, "quit")) {
            if (Objects.equals(input, "help")) {
                tableWriter(out, "register <USERNAME> <PASSWORD> <EMAIL>", "to create an account");
                tableWriter(out, "login <USERNAME> <PASSWORD>", "to play chess");
                tableWriter(out, "quit", "playing chess");
                tableWriter(out, "help", "with possible commands");
                preLoginUI(out);
            }
            else if (input.startsWith("login")) {
                String username = scanner.next();
                String password = scanner.next();
                out.print(SET_TEXT_COLOR_WHITE);
                out.print("  Username: " + username);
                out.println();
                out.print("  Password: " + password);
                out.println();
                try {
                    //fake.login(username, password);
                    postLoginUI(out, fake.login(username, password));
                }
                catch (Exception exception) {
                    out.print(exception.getMessage());
                    out.println();
                    preLoginUI(out);
                }
            }
            else if (input.startsWith("register")) {
                String username = scanner.next();
                String password = scanner.next();
                String email = scanner.next();

                out.print(SET_TEXT_COLOR_WHITE);
                out.print("  Username: " + username);
                out.println();
                out.print("  Password: " + password);
                out.println();
                out.print("  Email: " + email);
                out.println();
                try {
                    //fake.register(username, password, email);
                    postLoginUI(out, fake.register(username, password, email));
                }
                catch (Exception exception) {
                    out.print(exception.getMessage());
                    out.println();
                    preLoginUI(out);
                }
            }
            else {
                pleaseTryAgain(out);
                preLoginUI(out);
            }
        }
    }

    public void postLoginUI(PrintStream out, String authToken) {
        Scanner scanner = new Scanner(System.in);
        out.print(SET_TEXT_COLOR_GREEN);
        out.print("[LOGGED IN] >>> ");
        String input = scanner.next();
        if (!Objects.equals(input, "quit")) {
            if (Objects.equals(input, "help")) {
                tableWriter(out, "create <NAME>", "a game");
                tableWriter(out, "list", "games");
                tableWriter(out, "join <ID> [WHITE|BLACK]", "a game");
                tableWriter(out, "observe <ID>", "a game");
                tableWriter(out, "logout", "when you are done");
                tableWriter(out, "quit", "playing chess");
                tableWriter(out, "help", "with possible commands");
                postLoginUI(out, authToken);
            }
            else if (Objects.equals(input, "logout")) {
                try {
                    fake.logout(authToken);
                    preLoginUI(out);
                }
                catch (Exception exception) {
                    out.print(exception.getMessage());
                    postLoginUI(out, authToken);
                }
                //preLoginUI(out);
            }
            else if (Objects.equals(input, "create")) {
                String gameName = scanner.next();
                try {
                    fake.createGame(authToken, gameName);
                    out.println();
                    postLoginUI(out, authToken);
                }
                catch (Exception exception) {
                    out.print(exception.getMessage());
                    postLoginUI(out, authToken);
                }
            }
            else if (Objects.equals(input, "list")) {
                out.print(SET_TEXT_COLOR_WHITE);
                try {
                    ArrayList<GameData> games = fake.listGames(authToken);
                    for (GameData game : games) {
                        out.print("  " + game);
                        out.println();
                    }
                    postLoginUI(out, authToken);
                }
                catch (Exception exception) {
                    out.print(exception.getMessage());
                    out.println();
                    postLoginUI(out, authToken);
                }
                postLoginUI(out, authToken);
            }
            else if (Objects.equals(input, "join")) {
                int gameID = scanner.nextInt();
                String teamColor = scanner.next();
                if (Objects.equals(teamColor, "WHITE") || Objects.equals(teamColor, "BLACK")) {
                    try {
                        ChessGame.TeamColor playerColor;

                        GameUI gamePlay = new GameUI();

                        if (teamColor.equals("WHITE")) {
                            playerColor = ChessGame.TeamColor.WHITE;
                            gamePlay.setColor("WHITE");
                        }
                        else {
                            playerColor = ChessGame.TeamColor.BLACK;
                            gamePlay.setColor("BLACK");
                        }
                        fake.joinGame(authToken, playerColor, gameID);

                        ArrayList<GameData> games = fake.listGames(authToken);
                        ChessGame myGame = new ChessGame();
                        for (GameData game : games) {
                            if (game.gameID() == gameID) {
                                myGame = game.game();
                            }
                        }

                        ChessBoard myBoard = myGame.getBoard();
                        out.println();
                        TwoBools[][] legalMoves = GamePlayDrawing.potentialMoves(true, myGame, new ChessPosition(2, 2));
                        GamePlayDrawing.printBoard(out, true, myBoard, legalMoves);
                        out.println();
                        legalMoves = GamePlayDrawing.potentialMoves(false, myGame, new ChessPosition(2, 2));
                        GamePlayDrawing.printBoard(out, false, myBoard, legalMoves);
                        //postLoginUI(out, authToken);

                        gamePlay.gamePlayUI(authToken);
                    } catch (Exception exception) {
                        out.print(exception.getMessage());
                        out.println();
                        postLoginUI(out, authToken);
                    }
                }
                else {
                    pleaseTryAgain(out);
                    postLoginUI(out, authToken);
                }
            }
            else if (Objects.equals(input, "observe")) {
                int gameID = scanner.nextInt();
                postLoginUI(out, authToken);
            }
            else {
                pleaseTryAgain(out);
                postLoginUI(out, authToken);
            }
        }
    }

    public static void pleaseTryAgain(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
        out.print("Please try again. Type \"help\" if you need assistance.");
        out.print(RESET_BG_COLOR);
        out.println();
    }

    public static void tableWriter(PrintStream out, String message, String explanation) {
        out.print("  ");
        out.print(SET_TEXT_COLOR_LIGHT_GREY);
        out.print(message);
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(" --> " + explanation);
        out.println();
    }
}
