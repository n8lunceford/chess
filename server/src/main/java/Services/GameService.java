package Services;

import dataaccess.*;
import model.*;

import java.util.ArrayList;

public class GameService {

    private MemoryGameDAO gameDAO;
    private MemoryAuthDAO authDAO;

    public GameService(MemoryGameDAO gameDAO, MemoryAuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public ArrayList<GameData> listGames (ListGamesRequest listGamesRequest) throws DataAccessException {
        if (authDAO.getAuth(listGamesRequest.authToken()) != null) {
            return gameDAO.listGames();
        }
        else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public int createGame(CreateGameRequest createGameRequest) throws DataAccessException {
        if (authDAO.getAuth(createGameRequest.authToken()) != null) {
            return gameDAO.createGame(createGameRequest.gameName());
        }
        else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public void joinGame(JoinGameRequest joinGameRequest) throws DataAccessException {
        if (joinGameRequest.authToken() != null && authDAO.getAuth(joinGameRequest.authToken()) != null) {
            String username = authDAO.getAuth(joinGameRequest.authToken()).username();
            if (joinGameRequest.gameID() != 0 && gameDAO.getGame(joinGameRequest.gameID()) != null && joinGameRequest.playerColor() != null) {
                gameDAO.updateGame(username, joinGameRequest.playerColor(), gameDAO.getGame(joinGameRequest.gameID()));
            }
            else {
                throw new DataAccessException("Error: bad request");
            }
        }
        else {
            throw new DataAccessException("Error: unauthorized");
        }
    }
}
