package service;

import dataaccess.DataAccessException;
import dataaccess.SQLAuthDAO;
import dataaccess.SQLUserDAO;
import model.AuthData;
import model.LoginRequest;
import model.LogoutRequest;
import model.UserData;
import services.ProfileService;

import org.junit.jupiter.api.*;

import java.util.ArrayList;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProfileServiceTests {

    private ProfileService potential;
    private ArrayList<UserData> idealStorage = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        try {
            potential = new ProfileService(new SQLUserDAO(), new SQLAuthDAO());
            idealStorage.add(new UserData("myUser", "myPassword", "myGmail"));
            idealStorage.add(new UserData("otherUser", "otherPassword", "otherGmail"));
        }
        catch (DataAccessException exception) {
            exception.printStackTrace();
        }
    }

    @Test
    @Order(1)
    @DisplayName("Good Registration")
    public void goodRegistration() throws DataAccessException {
        Assertions.assertNotNull(potential.registration(new UserData("myUser", "myPassword", "myGmail")), "returned null for registration()");
    }

    @Test
    @Order(2)
    @DisplayName("Bad Registration")
    public void badRegistration() throws DataAccessException {
        potential.registration(new UserData("myUser", "myPassword", "myGmail"));
        potential.registration(new UserData("otherUser", "otherPassword", "otherGmail"));
        Assertions.assertThrows(DataAccessException.class, () -> potential.registration(new UserData("failedUser", "failedPassword", null)));
        try {
            potential.registration(new UserData("failedUser", "failedPassword", null));
        }
        catch (DataAccessException ignored) {}
        Assertions.assertEquals(idealStorage.size(), potential.userSize(), "returned not equals");
    }

    @Test
    @Order(3)
    @DisplayName("Good Login")
    public void goodLogin() throws DataAccessException {
        potential.registration(new UserData("myUser", "myPassword", "myGmail"));
        AuthData goodLogin = potential.login(new LoginRequest("myUser", "myPassword"));
        Assertions.assertNotNull(goodLogin, "returned null");
    }

    @Test
    @Order(4)
    @DisplayName("Bad Login")
    public void badLogin() throws DataAccessException {
        potential.registration(new UserData("myUser", "myPassword", "myGmail"));
        Assertions.assertThrows(DataAccessException.class, () -> potential.login(new LoginRequest("myUser", "wrongPassword")));
    }

    @Test
    @Order(5)
    @DisplayName("Good Logout")
    public void goodLogout() throws DataAccessException {
        potential.registration(new UserData("myUser", "myPassword", "myGmail"));
        AuthData myAuth = potential.login(new LoginRequest("myUser", "myPassword"));
        Assertions.assertDoesNotThrow(() -> potential.logout(new LogoutRequest(myAuth.authToken())));
    }

    @Test
    @Order(6)
    @DisplayName("Bad Logout")
    public void badLogout() throws DataAccessException {
        potential.registration(new UserData("myUser", "myPassword", "myGmail"));
        potential.login(new LoginRequest("myUser", "myPassword"));
        Assertions.assertThrows(DataAccessException.class, () -> potential.logout(new LogoutRequest("wrong token")));
    }

}
