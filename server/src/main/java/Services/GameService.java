package Services;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.ArrayList;

public class GameService {

    public AuthData createGame(UserData userData) {
        return null;
    }

    /**
    public AuthData login(UserData userData) {
        return null;
    }
    public void logout(AuthData authData) {
        AuthData placeHolder = authData;
        //return "{}";
    }
    */

    public ArrayList<GameData> listGames (AuthData authData) {
        return null;
    }

    public int createGame(AuthData authData, GameData gameData) {
        return 0;
    }

    public void joinGame(AuthData authData, ChessGame.TeamColor playerColor, int gameID) {

    }

}
