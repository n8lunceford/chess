package dataaccess;

import model.UserData;

public interface UserDAO {
    public void clear() throws DataAccessException;

    public void createUser(UserData userData) throws DataAccessException;

    public void getUser(String username) throws DataAccessException;

}
