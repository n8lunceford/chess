package services;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.MemoryGameDAO;

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
        if (gameDAO != null && userDAO != null && authDAO != null) {
            gameDAO.clear();
            userDAO.clear();
            authDAO.clear();
        }
        else {
            throw new DataAccessException("Error: could not clear");
        }
    }
}
