package Services;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.MemoryGameDAO;
import model.*;
import model.AuthData;

public class ClearService {
    public void clear() throws DataAccessException {
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        gameDAO.clear();
        userDAO.clear();
        authDAO.clear();
    }
}
