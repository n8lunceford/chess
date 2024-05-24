package Services;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import model.AuthData;
import model.GameData;
import model.ListGamesRequest;
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

    public ArrayList<GameData> listGames (ListGamesRequest listGamesRequest) throws DataAccessException {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        if (authDAO.getAuth(listGamesRequest.authToken()) != null) {
            return gameDAO.listGames();
        }
        else {
            return null;
        }
    }

    public int createGame(AuthData authData, GameData gameData) throws DataAccessException {
        return 0;
    }

    public void joinGame(AuthData authData, ChessGame.TeamColor playerColor, int gameID) throws DataAccessException {

    }

}
