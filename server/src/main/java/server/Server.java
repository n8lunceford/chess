package server;

/**
import com.google.gson.Gson;
import exception.ResponseException;
import model.Pet;
import server.websocket.WebSocketHandler;
import service.PetService;
*/

import Services.ClearService;
import Services.GameService;
import Services.ProfileService;
import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
//import Handlers.LogoutHandler;
import chess.ChessGame.TeamColor;

import dataaccess.*;

import model.*;
import spark.*;

import java.util.ArrayList;

public class Server {

    private MemoryAuthDAO authDAO;
    private MemoryUserDAO userDAO;
    private MemoryGameDAO gameDAO;

    public Server() {
        authDAO = new MemoryAuthDAO();
        userDAO = new MemoryUserDAO();
        gameDAO = new MemoryGameDAO();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("/web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::registration);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.delete("/db", this::clearApplication);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }




    private Object registration(Request req, Response res) throws DataAccessException {
        Gson jason = new Gson();
        try {
            UserData profile = jason.fromJson(req.body(), UserData.class);
            ProfileService service = new ProfileService(userDAO, authDAO);
            AuthData authData = service.registration(profile);

            res.status(200);
            return jason.toJson(authData);
        }
        catch (DataAccessException exception) {
            if (exception.getMessage().equalsIgnoreCase("Error: bad request")) {
                res.status(400);
            }
            else if (exception.getMessage().equalsIgnoreCase("Error: already taken")) {
                res.status(403);
            }
            else {
                res.status(500);
            }
            return jason.toJson(exception);
        }
    }

    private Object login(Request req, Response res) throws DataAccessException {
        Gson jason = new Gson();
        LoginRequest request = jason.fromJson(req.body(), LoginRequest.class);
        ProfileService service = new ProfileService(userDAO, authDAO);
        AuthData authData = service.login(request);

        res.status(200);
        return jason.toJson(authData);
    }

    private Object logout(Request req, Response res) throws DataAccessException {
        //Gson jason = new Gson();
        LogoutRequest request = new LogoutRequest(req.headers("authorization"));
        ProfileService service = new ProfileService(userDAO, authDAO);

        service.logout(request);

        /**
        if (authData != null) {
            service.logout(request);
            res.status(200);
        } else {
            res.status(401);
        }
        */

        res.status(200);
        return "{}";
    }

    private Object listGames(Request req, Response res) throws DataAccessException {
        Gson jason = new Gson();
        //ListGamesRequest request = jason.fromJson(req.body(), ListGamesRequest.class);
        ListGamesRequest request = new ListGamesRequest(req.headers("authorization"));
        GameService service = new GameService(gameDAO, authDAO);
        ArrayList<GameData> games = service.listGames(request);
        ListGamesResult result = new ListGamesResult(games);
        res.status(200);
        System.out.println(result);
        return jason.toJson(result);
    }

    private Object createGame(Request req, Response res) throws DataAccessException {
        Gson jason = new Gson();
        CreateGameRequest request = jason.fromJson(req.body(), CreateGameRequest.class);
        GameService service = new GameService(gameDAO, authDAO);
        int gameID = service.createGame(request);
        CreateGameResult result = new CreateGameResult(gameID);
        res.status(200);
        return jason.toJson(result);
    }




    private Object joinGame(Request req, Response res) throws DataAccessException {
        Gson jason = new Gson();
        JoinGameRequest request = jason.fromJson(req.body(), JoinGameRequest.class);
        JoinGameRequest requestTwo = new JoinGameRequest(req.headers("authorization"), request.teamColor(), request.gameID());
        //request.authToken() = req.headers("authorization");
        GameService service = new GameService(gameDAO, authDAO);
        service.joinGame(requestTwo);

        res.status(200);
        return "{}";
    }

    private Object clearApplication(Request req, Response res) throws DataAccessException {
        ClearService service = new ClearService(authDAO, userDAO, gameDAO);
        service.clear();

        res.status(200);
        return "{}";
    }

}
