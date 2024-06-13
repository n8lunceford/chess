package handlers;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.SQLAuthDAO;
import dataaccess.SQLGameDAO;
import dataaccess.SQLUserDAO;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;

import websocket.commands.*;
import websocket.messages.*;

import java.io.IOException;
import java.util.*;


@WebSocket
public class WebSocketHandler {

    private SQLAuthDAO authDAO;
    private SQLUserDAO userDAO;
    private SQLGameDAO gameDAO;

    public WebSocketHandler(SQLAuthDAO authDAO, SQLUserDAO userDAO, SQLGameDAO gameDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
    }

    private Map<Integer, HashSet<Session>> myMap = new HashMap<>();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        /**
        System.out.printf("Received: %s", message);
        Object games = new Gson().fromJson(message, Object.class);
        session.getRemote().sendString("WebSocket response: " + message);
        */
        try {
            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
            String username = getUsername(command.getAuthString());
            /**     saveSession(command.getGameID(), session);      */
            switch (command.getCommandType()) {
                case CONNECT -> {
                    Connect connect = new Gson().fromJson(message, Connect.class);
                    connect(session, username, connect);
                }
                case MAKE_MOVE -> {
                    MakeMove makeMove = new Gson().fromJson(message, MakeMove.class);
                    makeMove(session, username, makeMove);
                }
                case LEAVE -> {
                    Leave leave = new Gson().fromJson(message, Leave.class);
                    leaveGame(session, username, leave);
                }
                case RESIGN -> {
                    Resign resign = new Gson().fromJson(message, Resign.class);
                    resign(session, username, resign);
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            //sendMessage(session.getRemote(), new ErrorMessage("Error: " + ex.getMessage()));
            //session.getRemote().sendString("Error: " + ex.getMessage());
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: " + ex.getMessage())));
        }

    }



    private void connect(Session session, String username, Connect command) throws Exception {

        saveSession(command.getGameID(), session);

        if (gameDAO.getGame(command.getGameID()) != null && myMap.get(command.getGameID()) != null) {

            for (Session mySession : myMap.get(command.getGameID())) {
                if (mySession.isOpen()) {
                    if (session != mySession) {
                        mySession.getRemote().sendString(new Gson().toJson(new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username + " has connected.")));
                    } else {
                        mySession.getRemote().sendString(new Gson().toJson(new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, gameDAO.getGame(command.getGameID()).game().getBoard())));
                    }
                }
            }

        }
        else {
            throw new Exception();
        }
        //session.getRemote().sendString("WebSocket response: " + new Gson().toJson(command));
    }

    private void makeMove(Session session, String username, MakeMove command) throws DataAccessException, InvalidMoveException, Exception {

        if (gameDAO.getGame(command.getGameID()) != null && myMap.get(command.getGameID()) != null) {
            GameData myGameData = gameDAO.getGame(command.getGameID());
            ChessGame myGame = myGameData.game();
            if (!myGame.resigned()) {
                if (((myGameData.whiteUsername() != null
                        && Objects.equals(myGameData.whiteUsername(), username)
                        && myGame.getTeamTurn() == ChessGame.TeamColor.WHITE)
                        || (myGameData.blackUsername() != null
                        && Objects.equals(myGameData.blackUsername(), username)
                        && myGame.getTeamTurn() == ChessGame.TeamColor.BLACK))) {
                    try {
                        myGame.makeMove(command.getMove());
                        gameDAO.renewGame(myGame, myGameData);
                        for (Session mySession : myMap.get(command.getGameID())) {
                            if (mySession.isOpen()) {
                                mySession.getRemote().sendString(new Gson().toJson(new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, myGame.getBoard())));
                                if (session != mySession) {
                                    mySession.getRemote().sendString(new Gson().toJson(new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username + " has moved.")));
                                }
                            }
                        }

                    }
                    catch (InvalidMoveException ex) {
                        //ex.printStackTrace();
                        //throw ex;
                        if (session.isOpen()) {
                            session.getRemote().sendString(new Gson().toJson(new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: " + ex.getMessage())));
                        }
                    }
                }
                else {
                    if (session.isOpen()) {
                        session.getRemote().sendString(new Gson().toJson(new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: " + "it wasn\'t " + username + "\'s turn.")));
                    }
                }
            }
            else {
                if (session.isOpen()) {
                    session.getRemote().sendString(new Gson().toJson(new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: " + "the game is over.")));
                }
            }
        }
        else {
            throw new DataAccessException("The game does not exist.");
        }
    }

    private void leaveGame(Session session, String username, Leave command) throws Exception {
        if (myMap.get(command.getGameID()) != null) {
            if (session.isOpen()) {
                myMap.get(command.getGameID()).remove(session);
            }
        }
        if (Objects.equals(gameDAO.getGame(command.getGameID()).whiteUsername(), username)) {
            gameDAO.updateGame(null, ChessGame.TeamColor.WHITE, gameDAO.getGame(command.getGameID()));
        }
        if (Objects.equals(gameDAO.getGame(command.getGameID()).blackUsername(), username)) {
            gameDAO.updateGame(null, ChessGame.TeamColor.BLACK, gameDAO.getGame(command.getGameID()));
        }

        for (Session mySession : myMap.get(command.getGameID())) {
            if (mySession.isOpen()) {
                mySession.getRemote().sendString(new Gson().toJson(new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username + " is taking a break.")));
            }
        }
        //session.getRemote().sendString("WebSocket response: " + new Gson().toJson(command));
    }

    private void resign(Session session, String username, Resign command) throws DataAccessException, IOException {

        if (gameDAO.getGame(command.getGameID()) != null && myMap.get(command.getGameID()) != null) {
            GameData myGameData = gameDAO.getGame(command.getGameID());
            ChessGame myGame = myGameData.game();
            if (!myGame.resigned()) {
                if (Objects.equals(username, myGameData.whiteUsername()) || Objects.equals(username, myGameData.blackUsername())) {
                    myGame.resign();
                    gameDAO.renewGame(myGame, myGameData);
                    for (Session mySession : myMap.get(command.getGameID())) {
                        if (mySession.isOpen()) {
                            mySession.getRemote().sendString(new Gson().toJson(new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username + " has resigned.")));
                        }
                    }
                }
                else {
                    if (session.isOpen()) {
                        session.getRemote().sendString(new Gson().toJson(new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: " + username + " cannot resign due to not being a player.")));
                    }
                }
            }
            else {
                if (session.isOpen()) {
                    session.getRemote().sendString(new Gson().toJson(new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: " + "the game was already over.")));
                }
            }
        }
        else {
            throw new DataAccessException("The game does not exist.");
        }
    }

    private void saveSession(int gameID, Session session) {
        if (myMap.get(gameID) != null) {
            myMap.get(gameID).add(session);
        }
        else {
            myMap.put(gameID, new HashSet<>());
            myMap.get(gameID).add(session);
        }
    }

    private String getUsername(String authString) throws DataAccessException {
        return authDAO.getAuth(authString).username();
    }


}
