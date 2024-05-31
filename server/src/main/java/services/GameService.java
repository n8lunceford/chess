package services;

import chess.ChessGame;
import dataaccess.*;
import model.*;

import java.util.ArrayList;

public class GameService {

    private SQLGameDAO gameDAO;
    private SQLAuthDAO authDAO;

    public GameService(SQLGameDAO gameDAO, SQLAuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public SQLGameDAO getGameDAO() {
        return gameDAO;
    }

    public SQLAuthDAO getAuthDAO() {
        return authDAO;
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
                if ((joinGameRequest.playerColor() == ChessGame.TeamColor.WHITE && gameDAO.getGame(joinGameRequest.gameID()).whiteUsername() == null)
                        || (joinGameRequest.playerColor() == ChessGame.TeamColor.BLACK && gameDAO.getGame(joinGameRequest.gameID()).blackUsername() == null)) {
                    gameDAO.updateGame(username, joinGameRequest.playerColor(), gameDAO.getGame(joinGameRequest.gameID()));
                }
                else {
                    throw new DataAccessException("Error: already taken");
                }
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
