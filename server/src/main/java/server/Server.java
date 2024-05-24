package server;

/**
import com.google.gson.Gson;
import exception.ResponseException;
import model.Pet;
import server.websocket.WebSocketHandler;
import service.PetService;
*/

import Services.GameService;
import Services.ProfileService;
import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
//import Handlers.LogoutHandler;
import chess.ChessGame.TeamColor;

import model.*;
import spark.*;

import java.util.ArrayList;

public class Server {

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
        //Spark.delete("/db", this::clearApplication);

        //Spark.get("/pet", this::listPets);
        //Spark.delete("/pet/:id", this::deletePet);
        //Spark.delete("/pet", this::deleteAllPets);
        //Spark.exception(DataAccessException.class, this::exceptionHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }




    private Object registration(Request req, Response res) throws DataAccessException {
        Gson jason = new Gson();
        UserData profile = jason.fromJson(req.body(), UserData.class);
        ProfileService service = new ProfileService();
        AuthData authData = service.registration(profile);
        return jason.toJson(authData);
        //return "{}";
    }

    private Object login(Request req, Response res) throws DataAccessException {
        Gson jason = new Gson();
        LoginRequest request = jason.fromJson(req.body(), LoginRequest.class);
        ProfileService service = new ProfileService();
        AuthData authData = service.login(request);
        return jason.toJson(authData);
    }

    private Object logout(Request req, Response res) throws DataAccessException {
        //Gson jason = new Gson();
        //AuthData authData = jason.fromJson(req.body(), AuthData.class);
        ProfileService service = new ProfileService();
        /**
        if (authData != null) {
            service.logout(authData);
            res.status(200);
        } else {
            res.status(401);
        }
        */
        return "{}";
        //return jason.toJson(authData);
    }

    private Object listGames(Request req, Response res) throws DataAccessException {
        Gson jason = new Gson();
        AuthData authData = jason.fromJson(req.body(), AuthData.class);
        GameService service = new GameService();
        ArrayList<GameData> games = service.listGames(authData);
        return jason.toJson(games);
    }

    private Object createGame(Request req, Response res) throws DataAccessException {
        Gson jason = new Gson();
        AuthData authData = jason.fromJson(req.body(), AuthData.class);
        GameData gameData = jason.fromJson(req.body(), GameData.class);
        GameService service = new GameService();
        int gameID = service.createGame(authData, gameData);
        return jason.toJson(gameID);
    }




    private Object joinGame(Request req, Response res) throws DataAccessException {

        Gson jason = new Gson();
        AuthData authData = jason.fromJson(req.body(), AuthData.class);
        ChessGame.TeamColor playerColor = jason.fromJson(req.body(), ChessGame.TeamColor.class);


        return "{}";
    }

/**
    private Object deletePet(Request req, Response res) throws DataAccessException {
        var id = Integer.parseInt(req.params(":id"));
        var pet = service.getPet(id);
        if (pet != null) {
            service.deletePet(id);
            webSocketHandler.makeNoise(pet.name(), pet.sound());
            res.status(204);
        } else {
            res.status(404);
        }
        return "";
    }

    private Object deleteAllPets(Request req, Response res) throws DataAccessException {
        service.deleteAllPets();
        res.status(204);
        return "";
    }
    */



}
