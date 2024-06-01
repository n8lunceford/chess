package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.ArrayList;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() throws DataAccessException {
        configureDatabase();
    }

    public int size() throws DataAccessException {
        try (var connection = DatabaseManager.getConnection();
             var preparedStatement = connection.prepareStatement("SELECT * FROM user");
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
        try (var connection = DatabaseManager.getConnection(); var preparedStatement = connection.prepareStatement("DELETE FROM user")) {

            preparedStatement.executeUpdate();

        }
        catch (SQLException exception) {
            throw new DataAccessException(exception.getMessage());
        }
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection(); var preparedStatement = connection.prepareStatement("INSERT INTO user (username, password, email) VALUES (?, ?, ?)")) {
            preparedStatement.setString(1, userData.username());
            if (userData.password() != null) {
                preparedStatement.setString(2, BCrypt.hashpw(userData.password(), BCrypt.gensalt()));
            }
            else {
                throw new DataAccessException("Error: bad request");
            }
            preparedStatement.setString(3, userData.email());
            preparedStatement.executeUpdate();
        }
        catch (SQLException exception) {
            throw new DataAccessException(exception.getMessage());
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection(); var preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE username = ?")) {
            preparedStatement.setString(1, username);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new UserData(username, resultSet.getString("password"), resultSet.getString("email"));
            }
            else {
                return null;
            }
        }
        catch (SQLException exception) {
            throw new DataAccessException(exception.getMessage());
        }
    }



    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  user (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`)
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
