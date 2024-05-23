package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.UserData;

import java.util.ArrayList;

public class GameDAOIMP implements GameDAO {

    private ArrayList<GameData> games;

    GameDAOIMP() {
        games = new ArrayList<>();
    }

    @Override
    public void clear() throws DataAccessException {
        games = new ArrayList<>();
    }

    @Override
    public void createGame(String gameName) throws DataAccessException {
        boolean isThere = false;
        for (GameData game : games) {
            if (game.gameName() == gameName) {
                isThere = true;
            }
        }
        if (!isThere) {
            int gameID = Integer.parseInt(null);
            GameData newGame = new GameData(gameID, null, null, gameName, new ChessGame());             /**         NULL gameID         */
            games.add(newGame);
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
