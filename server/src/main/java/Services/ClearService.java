package Services;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.MemoryGameDAO;
import model.*;
import model.AuthData;

public class ClearService {

    private MemoryAuthDAO authDAO;
    private MemoryUserDAO userDAO;
    private MemoryGameDAO gameDAO;

    public ClearService(MemoryAuthDAO authDAO, MemoryUserDAO userDAO, MemoryGameDAO gameDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
    }


    public void clear() throws DataAccessException {
//        if (gameDAO.size() == 0 && userDAO.size() == 0 && authDAO.size() == 0) {
//            throw new DataAccessException("Error: unauthorized");
//        }
        gameDAO.clear();
        userDAO.clear();
        authDAO.clear();
    }
}
