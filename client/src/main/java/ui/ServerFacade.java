package ui;

import com.google.gson.Gson;
import model.AuthData;
import model.LoginRequest;
import model.LogoutRequest;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;

public class ServerFacade {

    public void login(String username, String password) throws Exception {
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
            System.out.println(new Gson().fromJson(inputStreamReader, AuthData.class));
        }
    }

}
