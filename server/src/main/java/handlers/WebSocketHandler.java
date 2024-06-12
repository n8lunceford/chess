package handlers;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.SQLAuthDAO;
import dataaccess.SQLGameDAO;
import dataaccess.SQLUserDAO;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;

import websocket.commands.*;
import websocket.messages.*;

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
                case CONNECT -> connect(session, username, (Connect) command);
                case MAKE_MOVE -> {
                    MakeMove makeMove = new Gson().fromJson(message, MakeMove.class);
                    makeMove(session, username, makeMove);
                }
                case LEAVE -> leaveGame(session, username, (Leave) command);
                case RESIGN -> resign(session, username, (Resign) command);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            //sendMessage(session.getRemote(), new ErrorMessage("Error: " + ex.getMessage()));
            //session.getRemote().sendString("Error: " + ex.getMessage());
            session.getRemote().sendString("WebSocket response: " + new Gson().toJson(new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: " + ex.getMessage())));
        }

    }



    private void connect(Session session, String username, Connect command) throws Exception {

        saveSession(command.getGameID(), session);

        for (Session mySession : myMap.get(command.getGameID())) {
            if (session != mySession) {
                mySession.getRemote().sendString("WebSocket response: " + new Gson().toJson(new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username + " has connected.")));
            }
        }
        //session.getRemote().sendString("WebSocket response: " + new Gson().toJson(command));
    }

    private void makeMove(Session session, String username, MakeMove command) {

    }

    private void leaveGame(Session session, String username, Leave command) throws Exception {
        if (myMap.get(command.getGameID()) != null) {
            myMap.get(command.getGameID()).remove(session);
        }
        if (Objects.equals(gameDAO.getGame(command.getGameID()).whiteUsername(), username)) {
            gameDAO.updateGame(null, ChessGame.TeamColor.WHITE, gameDAO.getGame(command.getGameID()));
        }
        if (Objects.equals(gameDAO.getGame(command.getGameID()).blackUsername(), username)) {
            gameDAO.updateGame(null, ChessGame.TeamColor.BLACK, gameDAO.getGame(command.getGameID()));
        }

        for (Session mySession : myMap.get(command.getGameID())) {
            mySession.getRemote().sendString("WebSocket response: " + new Gson().toJson(new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username + " is taking a break.")));
        }
        //session.getRemote().sendString("WebSocket response: " + new Gson().toJson(command));
    }

    private void resign(Session session, String username, Resign command) {

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
