package dataaccess;

import model.AuthData;
import model.UserData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO {

    public SQLAuthDAO() throws DataAccessException {
        configureDatabase();
    }

    public ArrayList<AuthData> getAuthList() throws DataAccessException {
        try (var connection = DatabaseManager.getConnection();
             var preparedStatement = connection.prepareStatement("SELECT * FROM auth");
             var resultSet = preparedStatement.executeQuery()) {

            ArrayList<AuthData> authList = new ArrayList<>();
            //  Code where authList takes in values of preparedStatement
            while (resultSet.next()) {
                authList.add(new AuthData(resultSet.getString("authToken"), resultSet.getString("username")));
            }
            return authList;
        }
        catch (SQLException exception) {
            throw new DataAccessException(exception.getMessage());
        }
    }

    public int size() throws DataAccessException {
        try (var connection = DatabaseManager.getConnection();
             var preparedStatement = connection.prepareStatement("SELECT * FROM auth");
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
             var preparedStatement = connection.prepareStatement("DELETE auth")) {

            preparedStatement.executeUpdate();

        }
        catch (SQLException exception) {
            throw new DataAccessException(exception.getMessage());
        }
    }

    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection();
             var preparedStatement = connection.prepareStatement("INSERT INTO user (authToken, username) VALUES (?, ?)")) {

            AuthData myAuth = new AuthData(UUID.randomUUID().toString(), username);
            preparedStatement.setString(1, myAuth.authToken());
            preparedStatement.setString(2, myAuth.username());
            preparedStatement.executeUpdate();
            return myAuth;
        }
        catch (SQLException exception) {
            throw new DataAccessException(exception.getMessage());
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection();
             var preparedStatement = connection.prepareStatement("SELECT VALUE (?) FROM auth")) {

            preparedStatement.setString(1, authToken);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new AuthData(authToken, resultSet.getString("username"));
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
    public void deleteAuth(String authToken) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection();
             var preparedStatement = connection.prepareStatement("DELETE FROM auth WHERE ?")) {

            preparedStatement.setString(1, authToken);
            preparedStatement.executeUpdate();
        }
        catch (SQLException exception) {
            throw new DataAccessException(exception.getMessage());
        }
    }



    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  auth (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`)
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
