package dataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {

    private ArrayList<AuthData> authList;

    public MemoryAuthDAO() {
        authList = new ArrayList<>();
    }

    @Override
    public void clear() throws DataAccessException {
        authList = new ArrayList<>();
    }

    @Override
    public AuthData createAuth(String username) throws DataAccessException {                                                        /*         needs authToken         */
        AuthData myAuth = new AuthData(UUID.randomUUID().toString(), username);
        return myAuth;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        AuthData myAuth = new AuthData(null, null);
        for (AuthData auth : authList) {
            if (auth.authToken() == authToken) {
                myAuth = auth;
            }
        }
        return myAuth;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        for (AuthData auth : authList) {
            if (auth.authToken() == authToken) {
                authList.remove(auth);
            }
        }
    }
}
