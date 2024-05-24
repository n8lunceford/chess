package Services;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.*;
//import model.Pet;
//import exception.ResponseException;
import model.AuthData;


public class ProfileService {

    public AuthData registration(UserData userData) throws DataAccessException {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        UserData dupeChecker = userDAO.getUser(userData.username());
        if (dupeChecker != null) {
            userDAO.createUser(userData);
            MemoryAuthDAO authDAO = new MemoryAuthDAO();
            return authDAO.createAuth(userData.username());
        }
        else {
            return null;
        }
    }

    public AuthData login(LoginRequest loginRequest) throws DataAccessException {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        UserData candidate = new UserData(null, null, null);
        if (userDAO.getUser(loginRequest.username()).password() == loginRequest.password()) {
            candidate = userDAO.getUser(loginRequest.username());
            return new MemoryAuthDAO().createAuth(candidate.username());
        }
        else {
            return null;
        }
    }
    public void logout(LogoutRequest logoutRequest) {

        //AuthData placeHolder = authData;
        //return "{}";
    }

}
