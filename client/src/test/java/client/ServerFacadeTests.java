package client;

import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    private ServerFacade fake;

    public ServerFacadeTests() throws Exception {
        fake = new ServerFacade();
    }

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }



    @Test
    @Order(1)
    @DisplayName("Good Registration")
    public void goodRegistration() throws Exception {
        fake.clear();
        Assertions.assertDoesNotThrow(() -> fake.register("myUser", "myPassword", "myEmail"));
    }



    @Test
    @Order(2)
    @DisplayName("Bad Registration")
    public void badRegistration() throws Exception {
        Assertions.assertThrows(Exception.class, () -> fake.register(null, "myPassword", "myEmail"));
        Assertions.assertThrows(Exception.class, () -> fake.register("myUser", null, "myEmail"));
        Assertions.assertThrows(Exception.class, () -> fake.register("myUser", "myPassword", null));
    }



    @Test
    @Order(3)
    @DisplayName("Good Login")
    public void goodLogin() throws Exception {
        fake.clear();
        String authToken = fake.register("myUser", "myPassword", "myEmail");
        fake.logout(authToken);
        Assertions.assertDoesNotThrow(() -> fake.login("myUser", "myPassword"));
    }



    @Test
    @Order(4)
    @DisplayName("Bad Login")
    public void badLogin() throws Exception {
        fake.clear();
        String authToken = fake.register("myUser", "myPassword", "myEmail");
        fake.logout(authToken);
        Assertions.assertThrows(Exception.class, () -> fake.login("User", "myPassword"));
        Assertions.assertThrows(Exception.class, () -> fake.login("myUser", "Password"));
        Assertions.assertThrows(Exception.class, () -> fake.login("myUser", null));
        fake.login("myUser", "myPassword");
    }

    @Test
    @Order(5)
    @DisplayName("Good Logout")
    public void goodLogout() throws Exception {
        fake.clear();
        String authToken = fake.register("myUser", "myPassword", "myEmail");
        Assertions.assertDoesNotThrow(() -> fake.logout(authToken));
        String secondToken = fake.login("myUser", "myPassword");
        Assertions.assertDoesNotThrow(() -> fake.logout(secondToken));
    }



    @Test
    @Order(6)
    @DisplayName("Bad Logout")
    public void badLogout() throws Exception {
        fake.clear();
        String authToken = fake.register("myUser", "myPassword", "myEmail");
        Assertions.assertDoesNotThrow(() -> fake.logout(authToken));
        String secondToken = fake.login("myUser", "myPassword");
        fake.logout(secondToken);
        Assertions.assertThrows(Exception.class, () -> fake.logout(secondToken));
    }



    @Test
    @Order(13)
    @DisplayName("Good Registration")
    public void goodClear() throws Exception {
        Assertions.assertDoesNotThrow(() -> fake.clear());
    }

}
