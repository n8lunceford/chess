package services;

import dataaccess.DataAccessException;
import dataaccess.*;

public class ClearService {

    private SQLAuthDAO authDAO;
    private SQLUserDAO userDAO;
    private SQLGameDAO gameDAO;

    public ClearService(SQLAuthDAO authDAO, SQLUserDAO userDAO, SQLGameDAO gameDAO) {
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
