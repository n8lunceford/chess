package dataaccess;

import model.GameData;
import chess.ChessGame.TeamColor;

import java.util.ArrayList;

public interface GameDAO {
    void clear() throws DataAccessException;
    int createGame(String gameName) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    ArrayList<GameData> listGames() throws DataAccessException;
    void updateGame(String username, TeamColor teamColor, GameData gameData) throws DataAccessException;
}
