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
            return null;
        }
    }

    public int createGame(CreateGameRequest createGameRequest) throws DataAccessException {
        if (authDAO.getAuth(createGameRequest.authToken()) != null) {
            return gameDAO.createGame(createGameRequest.gameName());
        }
        else {
            return 0;
        }
    }

    public void joinGame(JoinGameRequest joinGameRequest) throws DataAccessException {
        if (authDAO.getAuth(joinGameRequest.authToken()) != null) {
            gameDAO.updateGame(joinGameRequest.gameID());
        }
    }

}
