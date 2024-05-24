package Services;

import dataaccess.*;
import model.*;

import java.util.Objects;


public class ProfileService {

    private MemoryUserDAO userDAO;
    private MemoryAuthDAO authDAO;

    public ProfileService(MemoryUserDAO userDAO, MemoryAuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData registration(UserData userData) throws DataAccessException {
        UserData dupeChecker = userDAO.getUser(userData.username());
        if (dupeChecker != null) {
            userDAO.createUser(userData);
            return authDAO.createAuth(userData.username());
        }
        else {
            return null;
        }
    }

    public AuthData login(LoginRequest loginRequest) throws DataAccessException {
        UserData candidate;
        if (Objects.equals(userDAO.getUser(loginRequest.username()).password(), loginRequest.password())) {
            candidate = userDAO.getUser(loginRequest.username());
            return authDAO.createAuth(candidate.username());
        }
        else {
            return null;
        }
    }
    public void logout(LogoutRequest logoutRequest) throws DataAccessException {
        if (authDAO.getAuth(logoutRequest.authToken()) != null) {
            authDAO.deleteAuth(logoutRequest.authToken());
        }
    }

}
