package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import model.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;

public class ServerFacade {

    public String login(String username, String password) throws Exception {
        // Specify the desired endpoint
        URI uri = new URI("http://localhost:8080/session");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");
        // Specify that we are going to write out data
        http.setDoOutput(true);
        // Write out the body
        LoginRequest request = new LoginRequest(username, password);
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(request);
            outputStream.write(jsonBody.getBytes());
        }
        // Make the request
        return openDoors(http);
    }

    public String register(String username, String password, String email) throws Exception {
        // Specify the desired endpoint
        URI uri = new URI("http://localhost:8080/user");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        UserData request = new UserData(username, password, email);
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(request);
            outputStream.write(jsonBody.getBytes());
        }
        // Make the request
        return openDoors(http);
    }

    private String openDoors(HttpURLConnection http) throws Exception {
        http.connect();
        // Output the response body
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            AuthData myAuth = new Gson().fromJson(inputStreamReader, AuthData.class);
            System.out.println(myAuth);
            return myAuth.authToken();
        }
    }

    public void logout(String authToken) throws Exception {
        // Specify the desired endpoint
        URI uri = new URI("http://localhost:8080/session");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("DELETE");
        // Specify that we are going to write out data
        http.setDoOutput(true);
        // Write out a header
        http.addRequestProperty("authorization", authToken);
        // Make the request
        http.connect();

        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            //System.out.println(new Gson().fromJson(inputStreamReader, int.class));
            //System.out.print(inputStreamReader);
        }

    }

    public ArrayList<GameData> listGames(String authToken) throws Exception {
        // Specify the desired endpoint
        URI uri = new URI("http://localhost:8080/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("GET");
        // Specify that we are going to write out data
        http.setDoOutput(true);
        // Write out a header
        http.addRequestProperty("authorization", authToken);
        // Make the request
        http.connect();
        // Output the response body
         try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            ListGamesResult games = new Gson().fromJson(inputStreamReader, ListGamesResult.class);
            //System.out.println();
            return games.games();
         }
    }

    public int createGame(String authToken, String gameName) throws Exception {
        // Specify the desired endpoint
        URI uri = new URI("http://localhost:8080/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");
        // Specify that we are going to write out data
        http.setDoOutput(true);
        // Write out a header
        http.addRequestProperty("authorization", authToken);
        // Write out the body
        CreateGameRequest request = new CreateGameRequest(null, gameName);
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(request);
            outputStream.write(jsonBody.getBytes());
        }
        //comeIn(http);

        http.connect();
        // Output the response body
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            CreateGameResult gameID = new Gson().fromJson(inputStreamReader, CreateGameResult.class);
            //System.out.print(inputStreamReader);
            return gameID.gameID();
        }
    }

    public void joinGame(String authToken, ChessGame.TeamColor playerColor, int gameID) throws Exception {
        // Specify the desired endpoint
        URI uri = new URI("http://localhost:8080/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("PUT");
        // Specify that we are going to write out data
        http.setDoOutput(true);
        // Write out a header
        http.addRequestProperty("authorization", authToken);
        // Write out the body
        JoinGameRequest request = new JoinGameRequest(null, playerColor, gameID);
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(request);
            outputStream.write(jsonBody.getBytes());
        }
        // Make the request
        comeIn(http);
    }

    private void comeIn(HttpURLConnection http) throws Exception {
        http.connect();
        // Output the response body
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            //System.out.println(new Gson().fromJson(inputStreamReader, int.class));
            System.out.print(inputStreamReader);
        }
    }

    public void clear() throws Exception {
        URI uri = new URI("http://localhost:8080/db");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("DELETE");
        http.setDoOutput(true);
        http.connect();
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            //System.out.println(new Gson().fromJson(inputStreamReader, int.class));
            //System.out.print(inputStreamReader);
        }
    }
}
