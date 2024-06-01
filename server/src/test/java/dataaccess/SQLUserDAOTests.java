package dataaccess;

import model.UserData;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SQLUserDAOTests {

    private SQLUserDAO userDAO;

    SQLUserDAOTests() {
        try {
            userDAO = new SQLUserDAO();
        }
        catch (DataAccessException ignore) {}
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        userDAO.clear();
    }

    @Test
    @Order(1)
    @DisplayName("Good Clear")
    public void goodClear() throws DataAccessException {
        userDAO.createUser(new UserData("myUser", "myPassword", "myEmail"));
        userDAO.createUser(new UserData("secondUser", "secondPassword", "secondEmail"));
        Assertions.assertDoesNotThrow(() -> userDAO.clear());
        userDAO.clear();
        Assertions.assertDoesNotThrow(() -> userDAO.clear());
    }

    @Test
    @Order(2)
    @DisplayName("Good Create User")
    public void goodCreateUser() throws DataAccessException {
        Assertions.assertDoesNotThrow(() -> userDAO.createUser(new UserData("myUser", "myPassword", "myEmail")));
        Assertions.assertDoesNotThrow(() -> userDAO.createUser(new UserData("secondUser", "secondPassword", "secondEmail")));
    }

    @Test
    @Order(3)
    @DisplayName("Bad Create User")
    public void badCreateUser() throws DataAccessException {
        Assertions.assertThrows(DataAccessException.class, () -> userDAO.createUser(new UserData("myUser", "myPassword", null)));
        Assertions.assertThrows(DataAccessException.class, () -> userDAO.createUser(new UserData(null, "secondPassword", "secondEmail")));
        Assertions.assertThrows(DataAccessException.class, () -> userDAO.createUser(new UserData("thirdUser", null, "thirdEmail")));
    }

    @Test
    @Order(4)
    @DisplayName("Good Get User")
    public void goodGetUser() throws DataAccessException {
        userDAO.createUser(new UserData("myUser", "myPassword", "myEmail"));
        userDAO.createUser(new UserData("secondUser", "secondPassword", "secondEmail"));
        Assertions.assertNotNull(userDAO.getUser("myUser"));
        Assertions.assertNotNull(userDAO.getUser("secondUser"));
    }

    @Test
    @Order(5)
    @DisplayName("Bad Get User")
    public void badGetUser() throws DataAccessException {
        userDAO.createUser(new UserData("myUser", "myPassword", "myEmail"));
        userDAO.createUser(new UserData("secondUser", "secondPassword", "secondEmail"));
        Assertions.assertNull(userDAO.getUser("my user"));
        Assertions.assertNull(userDAO.getUser("doesNotExist"));
    }

}
