package dataaccess;

import model.AuthData;

import java.util.ArrayList;

public class AuthDAOIMP implements AuthDAO {

    private ArrayList<AuthData> authList;

    AuthDAOIMP() {
        authList = new ArrayList<>();
    }

    @Override
    public void clear() throws DataAccessException {
        authList = new ArrayList<>();
    }

    @Override
    public void createAuth(String username) throws DataAccessException {                                                        /**         needs authToken         */
        AuthData myAuth = new AuthData(null, username);
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
