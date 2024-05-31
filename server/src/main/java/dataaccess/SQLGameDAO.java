package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;

public class SQLGameDAO implements GameDAO {

    public SQLGameDAO() throws DataAccessException {
        configureDatabase();
    }

    public int size() throws DataAccessException {
        try (var connection = DatabaseManager.getConnection();
             var preparedStatement = connection.prepareStatement("SELECT * FROM game");
             var resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            else {
                return 0;
            }
        }
        catch (SQLException exception) {
            //throw new DataAccessException(exception.getMessage());
            return 0;
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try (var connection = DatabaseManager.getConnection();
             var preparedStatement = connection.prepareStatement("DELETE game")) {

            preparedStatement.executeUpdate();
        }
        catch (SQLException exception) {
            throw new DataAccessException(exception.getMessage());
        }
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection();
             var preparedStatement = connection.prepareStatement("INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)")) {

            Gson jason = new Gson();
            int gameID = listGames().size() + 1;
            preparedStatement.setString(1, String.valueOf(gameID));
            preparedStatement.setString(2, null);
            preparedStatement.setString(3, null);
            preparedStatement.setString(4, gameName);
            preparedStatement.setString(5, jason.toJson(new ChessGame()));
            preparedStatement.executeUpdate();
            return gameID;
        }
        catch (SQLException exception) {
            //throw new DataAccessException(exception.getMessage());
            return 0;
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection();
             var preparedStatement = connection.prepareStatement("SELECT VALUE (?) FROM user")) {

            preparedStatement.setString(1, String.valueOf(gameID));
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new GameData(gameID, resultSet.getString("whiteUsername"), resultSet.getString("blackUsername"),
                        resultSet.getString("gameName"), (ChessGame) resultSet.getObject("game"));
            }
            else {
                throw new DataAccessException("exception.getMessage()");
            }
        }
        catch (SQLException exception) {
            throw new DataAccessException(exception.getMessage());
        }
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        try (var connection = DatabaseManager.getConnection();
             var preparedStatement = connection.prepareStatement("SELECT * FROM game");
             var resultSet = preparedStatement.executeQuery()) {

            ArrayList<GameData> games = new ArrayList<>();
            //  Code where games takes in values of preparedStatement
            while (resultSet.next()) {
                games.add(new GameData(resultSet.getInt("gameID"), resultSet.getString("whiteUsername"), resultSet.getString("blackUsername"),
                        resultSet.getString("gameName"), (ChessGame) resultSet.getObject("game")));
            }
            return games;
        }
        catch (SQLException exception) {
            throw new DataAccessException(exception.getMessage());
        }
    }

    @Override
    public void updateGame(String username, ChessGame.TeamColor teamColor, GameData gameData) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection();
             var preparedStatement = connection.prepareStatement("UPDATE game SET ? = ? WHERE gameID = ?")) {

            Gson jason = new Gson();
            if (teamColor == ChessGame.TeamColor.WHITE) {
                preparedStatement.setString(1, "whiteUsername");
            }
            else if (teamColor == ChessGame.TeamColor.BLACK) {
                preparedStatement.setString(1, "blackUsername");
            }
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, jason.toJson(gameData.gameID()));
            preparedStatement.executeUpdate();
        }
        catch (SQLException exception) {
            throw new DataAccessException(exception.getMessage());
        }
    }



    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  game (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256) NOT NULL,
              `blackUsername` varchar(256) NOT NULL,
              `gameName` varchar(256) NOT NULL,
              `game` text NOT NULL,
              PRIMARY KEY (`gameID`)
            )
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            //throw new DataAccessException(500, String.format("Unable to configure database: %s", ex.getMessage()));
            throw new DataAccessException(ex.getMessage());
        }
    }
}
