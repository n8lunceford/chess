package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;

public class MemoryGameDAO implements GameDAO {

    private ArrayList<GameData> games;

    public MemoryGameDAO() {
        games = new ArrayList<>();
    }

    @Override
    public void clear() throws DataAccessException {
        games = new ArrayList<>();
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        boolean isThere = false;
        for (GameData game : games) {
            if (game.gameName() == gameName) {
                isThere = true;
            }
        }
        if (!isThere) {
            int gameID = games.size() + 1;
            GameData newGame = new GameData(gameID, null, null, gameName, new ChessGame());
            games.add(newGame);
            return newGame.gameID();
        }
        else {
            return 0;
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        GameData myGame = new GameData(0, null, null, null, new ChessGame());
        for (GameData game : games) {
            if (game.gameID() == gameID) {
                myGame = game;
            }
        }
        return myGame;
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        return games;
    }

    @Override
    public void updateGame(int gameID) throws DataAccessException {                                                             /**         NEEDS IMPLEMENTATION    */

    }
}
