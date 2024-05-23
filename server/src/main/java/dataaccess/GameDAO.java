package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    void clear() throws DataAccessException;
    void createGame(String gameName) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    ArrayList<GameData> listGames() throws DataAccessException;
    void updateGame(int gameID) throws DataAccessException;
}
