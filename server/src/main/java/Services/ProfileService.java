package Services;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
//import model.Pet;
//import exception.ResponseException;
import model.AuthData;
import model.LogoutRequest;
import model.UserData;


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

    public AuthData login(UserData userData) {
        return null;
    }
    public void logout(LogoutRequest logoutRequest) {

        //AuthData placeHolder = authData;
        //return "{}";
    }

}
