package services;

import dataaccess.*;
import model.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Objects;


public class ProfileService {

    private SQLUserDAO userDAO;
    private SQLAuthDAO authDAO;

    public int userSize() {
        try {
            return userDAO.size();
        }
        catch (DataAccessException exception) {
            return 0;
        }
    }

    public SQLAuthDAO getAuthDAO() {
        return authDAO;
    }

    public SQLUserDAO getUserDAO() {
        return userDAO;
    }

    public ProfileService(SQLUserDAO userDAO, SQLAuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData registration(UserData userData) throws DataAccessException {
        UserData dupeChecker = userDAO.getUser(userData.username());
        if (dupeChecker == null) {
            if (userData.username() == null || userData.password() == null || userData.email() == null) {
                throw new DataAccessException("Error: bad request");
            }
            else {
                userDAO.createUser(userData);
                return authDAO.createAuth(userData.username());
            }
        }
        else {
            throw new DataAccessException("Error: already taken");
        }
    }

    public AuthData login(LoginRequest loginRequest) throws DataAccessException {
        UserData candidate;
        if (loginRequest.username() != null && loginRequest.password() != null
                && userDAO.getUser(loginRequest.username()) != null && BCrypt.checkpw(loginRequest.password(), userDAO.getUser(loginRequest.username()).password())) {
            candidate = userDAO.getUser(loginRequest.username());
            return authDAO.createAuth(candidate.username());
        }
        else {
            throw new DataAccessException("Error: unauthorized");
        }
    }
    public void logout(LogoutRequest logoutRequest) throws DataAccessException {
        if (logoutRequest.authToken() == null
                || authDAO.getAuth(logoutRequest.authToken()) == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        else {
            authDAO.deleteAuth(logoutRequest.authToken());
        }
    }

}
