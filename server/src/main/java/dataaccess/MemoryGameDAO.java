package dataaccess;

import chess.ChessGame;
import chess.ChessGame.TeamColor;
import model.GameData;

import java.util.ArrayList;
import java.util.Objects;

public class MemoryGameDAO implements GameDAO {

    private ArrayList<GameData> games;

    public MemoryGameDAO() {
        games = new ArrayList<>();
    }

    public int size() {
        return games.size();
    }

    @Override
    public void clear() throws DataAccessException {
        games = new ArrayList<>();
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        boolean isThere = false;
        for (GameData game : games) {
            if (Objects.equals(game.gameName(), gameName)) {
                isThere = true;
                break;
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
    public void updateGame(String username, TeamColor teamColor, GameData gameData) throws DataAccessException {
        GameData placeHolder = gameData;
        if (teamColor == TeamColor.WHITE) {
            gameData = new GameData(placeHolder.gameID(), username, placeHolder.blackUsername(), placeHolder.gameName(), placeHolder.game());
        }
        else if (teamColor == TeamColor.BLACK) {
            gameData = new GameData(placeHolder.gameID(), placeHolder.whiteUsername(), username, placeHolder.gameName(), placeHolder.game());
        }
        for (GameData game : games) {
            if (game.gameID() == gameData.gameID()) {
                //game = new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), gameData.game());
                games.remove(game);
                games.add(new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), gameData.game()));
                break;
            }
        }
        //createGame(gameData.gameName());
    }
}
