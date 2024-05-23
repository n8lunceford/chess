package dataaccess;

import model.UserData;

import java.util.ArrayList;

public class MemoryUserDAO implements UserDAO {

    private ArrayList<UserData> users;

    public MemoryUserDAO() {
        users = new ArrayList<>();
    }

    @Override
    public void clear() throws DataAccessException {
        users = new ArrayList<>();
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        boolean isThere = false;
        for (UserData user : users) {
            if (user.username() == userData.username()) {
                isThere = true;
            }
        }
        if (!isThere) {
            users.add(userData);
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        UserData flynn = new UserData(null,null,null);
        for (UserData userData : users) {
            if (username == userData.username()) {
                flynn = userData;
            }
        }
        return flynn;
    }
}
