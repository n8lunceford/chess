package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SQLAuthDAOTests {

    private SQLAuthDAO authDAO;

    SQLAuthDAOTests() {
        try {
            authDAO = new SQLAuthDAO();
        }
        catch (DataAccessException ignore) {}
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        authDAO.clear();
    }

    @Test
    @Order(1)
    @DisplayName("Good Clear")
    public void goodClear() throws DataAccessException {
        authDAO.createAuth("myAuth");
        authDAO.createAuth("secondAuth");
        Assertions.assertDoesNotThrow(() -> authDAO.clear());
        authDAO.clear();
        Assertions.assertDoesNotThrow(() -> authDAO.clear());
    }

    @Test
    @Order(2)
    @DisplayName("Good Create Auth")
    public void goodCreateAuth() {
        Assertions.assertDoesNotThrow(() -> authDAO.createAuth("myUsername"));
    }

    @Test
    @Order(3)
    @DisplayName("Bad Create Auth")
    public void badCreateAuth() {
        Assertions.assertThrows(DataAccessException.class, () -> authDAO.createAuth(null));
    }

    @Test
    @Order(4)
    @DisplayName("Good Get Auth")
    public void goodGetAuth() throws DataAccessException {
        authDAO.createAuth("myUsername");
        Assertions.assertDoesNotThrow(() -> authDAO.getAuth("myUsername"));
    }

    @Test
    @Order(5)
    @DisplayName("Bad Get Auth")
    public void badGetAuth() {
        Assertions.assertThrows(DataAccessException.class, () -> authDAO.getAuth(null));
    }

    @Test
    @Order(6)
    @DisplayName("Good Delete Auth")
    void goodDeleteAuth() throws DataAccessException {
        AuthData myAuth = authDAO.createAuth("myUsername");
        Assertions.assertDoesNotThrow(() -> authDAO.deleteAuth(myAuth.authToken()));
    }

    @Test
    @Order(7)
    @DisplayName("Bad Delete Auth")
    void badDeleteAuth() throws DataAccessException {
        AuthData myAuth = authDAO.createAuth("myUsername");
        authDAO.deleteAuth(myAuth.authToken());
        Assertions.assertThrows(DataAccessException.class, () -> authDAO.deleteAuth(null));
    }

}
