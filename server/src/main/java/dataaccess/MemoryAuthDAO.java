package dataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {

    private ArrayList<AuthData> authList;

    public MemoryAuthDAO() {
        authList = new ArrayList<>();
    }

    public ArrayList<AuthData> getAuthList() {
        return authList;
    }

    public int size() {
        return authList.size();
    }

    @Override
    public void clear() throws DataAccessException {
        authList = new ArrayList<>();
    }

    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        AuthData myAuth = new AuthData(UUID.randomUUID().toString(), username);
        authList.add(myAuth);
        return myAuth;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        AuthData myAuth = new AuthData(null, null);
        for (AuthData auth : authList) {
            if (Objects.equals(auth.authToken(), authToken)) {
                myAuth = auth;
            }
        }
        if (myAuth.authToken() != null && myAuth.username() != null) {
            return myAuth;
        }
        else {
            return null;
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
            if (getAuth(authToken) != null) {
                authList.remove(getAuth(authToken));
            }
            else {
                throw new DataAccessException("Error: unauthorized");
            }
    }
}
