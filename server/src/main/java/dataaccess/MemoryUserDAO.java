package dataaccess;

import model.UserData;

import java.util.ArrayList;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO {

    private ArrayList<UserData> users;

    public MemoryUserDAO() {
        users = new ArrayList<>();
    }

    public int size() {
        return users.size();
    }

    @Override
    public void clear() throws DataAccessException {
        users = new ArrayList<>();
    }

    @Override
    public void createUser(UserData userData) {
        boolean isThere = false;
        for (UserData user : users) {
            if (Objects.equals(user.username(), userData.username())) {
                isThere = true;
                break;
            }
        }
        if (!isThere) {
            users.add(userData);
        }
    }

    @Override
    public UserData getUser(String username) {
        UserData flynn = null;
        for (UserData userData : users) {
            if (Objects.equals(username, userData.username())) {
                flynn = userData;
            }
        }
        return flynn;
    }
}
