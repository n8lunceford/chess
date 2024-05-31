package server;

import services.*;
import com.google.gson.Gson;

import dataaccess.*;

import model.*;
import spark.*;

import java.util.ArrayList;
import java.util.Map;

public class Server {

    private SQLAuthDAO authDAO;
    private SQLUserDAO userDAO;
    private SQLGameDAO gameDAO;

    public Server() {
        try {
            authDAO = new SQLAuthDAO();
            userDAO = new SQLUserDAO();
            gameDAO = new SQLGameDAO();
        }
        catch (DataAccessException exception) {}
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

    private Object registration(Request req, Response res) {
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
            return jason.toJson(Map.of("message", exception.getMessage()));
        }
    }

    private Object login(Request req, Response res) {
        Gson jason = new Gson();
        try {
            LoginRequest request = jason.fromJson(req.body(), LoginRequest.class);
            ProfileService service = new ProfileService(userDAO, authDAO);
            AuthData authData = service.login(request);
            res.status(200);
            return jason.toJson(authData);
        }
        catch (DataAccessException exception) {
            statusSetter(exception, res);
            return jason.toJson(Map.of("message", exception.getMessage()));
        }
    }

    private Object logout(Request req, Response res) {
        try {
            LogoutRequest request = new LogoutRequest(req.headers("authorization"));
            ProfileService service = new ProfileService(userDAO, authDAO);
            service.logout(request);
            res.status(200);
            return "{}";
        }
        catch (DataAccessException exception) {
            statusSetter(exception, res);
            exception.printStackTrace();
            return new Gson().toJson(Map.of("message", exception.getMessage()));
        }
    }

    private Object listGames(Request req, Response res) {
        Gson jason = new Gson();
        try {
            ListGamesRequest request = new ListGamesRequest(req.headers("authorization"));
            GameService service = new GameService(gameDAO, authDAO);
            ArrayList<GameData> games = service.listGames(request);
            ListGamesResult result = new ListGamesResult(games);
            res.status(200);
            return jason.toJson(result);
        }
        catch (DataAccessException exception) {
            statusSetter(exception, res);
            return jason.toJson(Map.of("message", exception.getMessage()));
        }
    }

    private Object createGame(Request req, Response res) {
        Gson jason = new Gson();
        try {
            CreateGameRequest request = jason.fromJson(req.body(), CreateGameRequest.class);
            CreateGameRequest requestTwo = new CreateGameRequest(req.headers("authorization"), request.gameName());
            GameService service = new GameService(gameDAO, authDAO);
            int gameID = service.createGame(requestTwo);
            CreateGameResult result = new CreateGameResult(gameID);
            res.status(200);
            return jason.toJson(result);
        }
        catch (DataAccessException exception) {
            if (exception.getMessage().equalsIgnoreCase("Error: bad request")) {
                res.status(400);
            }
            else if (exception.getMessage().equalsIgnoreCase("Error: unauthorized")) {
                res.status(401);
            }
            else {
                res.status(500);
            }
            return jason.toJson(Map.of("message", exception.getMessage()));
        }
    }

    private Object joinGame(Request req, Response res) {
        Gson jason = new Gson();
        try {
            JoinGameRequest request = jason.fromJson(req.body(), JoinGameRequest.class);
            JoinGameRequest requestTwo = new JoinGameRequest(req.headers("authorization"), request.playerColor(), request.gameID());
            GameService service = new GameService(gameDAO, authDAO);
            service.joinGame(requestTwo);
            res.status(200);
            return "{}";
        }
        catch (DataAccessException exception) {
            if (exception.getMessage().equalsIgnoreCase("Error: bad request")) {
                res.status(400);
            }
            else if (exception.getMessage().equalsIgnoreCase("Error: unauthorized")) {
                res.status(401);
            }
            else if (exception.getMessage().equalsIgnoreCase("Error: already taken")) {
                res.status(403);
            }
            else {
                res.status(500);
            }
            return jason.toJson(Map.of("message", exception.getMessage()));
        }
    }

    private Object clearApplication(Request req, Response res) {
        try {
            ClearService service = new ClearService(authDAO, userDAO, gameDAO);
            service.clear();
            res.status(200);
            return "{}";
        }
        catch (DataAccessException exception) {
            res.status(500);
            return new Gson().toJson(Map.of("message", exception.getMessage()));
        }
    }

    private void statusSetter(DataAccessException exception, Response res) {
        if (exception.getMessage().equalsIgnoreCase("Error: unauthorized")) {
            res.status(401);
        }
        else {
            res.status(500);
        }
    }

}
