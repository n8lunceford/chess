package Services;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.*;
//import model.Pet;
//import exception.ResponseException;
import model.AuthData;


public class ProfileService {

    private MemoryUserDAO userDAO;
    private MemoryAuthDAO authDAO;

    public ProfileService(MemoryUserDAO userDAO, MemoryAuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData registration(UserData userData) throws DataAccessException {
        //MemoryUserDAO userDAO = new MemoryUserDAO();
        UserData dupeChecker = userDAO.getUser(userData.username());
        if (dupeChecker != null) {
            userDAO.createUser(userData);
            //MemoryAuthDAO authDAO = new MemoryAuthDAO();
            return authDAO.createAuth(userData.username());
        }
        else {
            return null;
        }
    }

    public AuthData login(LoginRequest loginRequest) throws DataAccessException {
        //MemoryUserDAO userDAO = new MemoryUserDAO();
        UserData candidate;
        if (userDAO.getUser(loginRequest.username()).password() == loginRequest.password()) {
            candidate = userDAO.getUser(loginRequest.username());
            return authDAO.createAuth(candidate.username());
        }
        else {
            return null;
        }
    }
    public void logout(LogoutRequest logoutRequest) throws DataAccessException {
        //MemoryAuthDAO authDAO = new MemoryAuthDAO();
        //AuthData authData;
        if (authDAO.getAuth(logoutRequest.authToken()) != null) {
            //authData = authDAO.getAuth(logoutRequest.authToken());
            authDAO.deleteAuth(logoutRequest.authToken());
        }
        //AuthData placeHolder = authData;
        //return "{}";
    }

}
