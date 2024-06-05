package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import model.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;

public class ServerFacade {

    public String login(String username, String password) throws Exception {
        // Specify the desired endpoint
        URI uri = new URI("http://localhost:8080/session");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");

        // Specify that we are going to write out data
        http.setDoOutput(true);
        // Write out a header
        //http.addRequestProperty("Content-Type", "application/json");

        // Write out the body
        LoginRequest request = new LoginRequest(username, password);
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(request);
            outputStream.write(jsonBody.getBytes());
        }
        // Make the request
        http.connect();

        // Output the response body
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            AuthData myAuth = new Gson().fromJson(inputStreamReader, AuthData.class);
            System.out.println(myAuth);
            return myAuth.authToken();
        }
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

        // Write out the body

        /**
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(request);
            outputStream.write(jsonBody.getBytes());
        }
        */


        // Make the request
        http.connect();

        // Output the response body
        /**
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            System.out.println(new Gson().fromJson(inputStreamReader, String.class));
        }
        */
    }

    public void listGames(String authToken) throws Exception {
        // Specify the desired endpoint
        URI uri = new URI("http://localhost:8080/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("GET");



        // Specify that we are going to write out data
        http.setDoOutput(true);
        // Write out a header
        http.addRequestProperty("authorization", authToken);

        // Write out the body

        /**
         try (var outputStream = http.getOutputStream()) {
         var jsonBody = new Gson().toJson(request);
         outputStream.write(jsonBody.getBytes());
         }
         */


        // Make the request
        http.connect();

        // Output the response body

         try (InputStream respBody = http.getInputStream()) {
         InputStreamReader inputStreamReader = new InputStreamReader(respBody);
         System.out.println(new Gson().fromJson(inputStreamReader, GameData.class));
         }

    }

}
