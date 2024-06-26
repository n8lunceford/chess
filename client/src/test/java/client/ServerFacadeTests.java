package client;

import chess.ChessGame;
import org.junit.jupiter.api.*;
import server.Server;
import facade.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade fake;

    public ServerFacadeTests() throws Exception {
        //fake = new ServerFacade();
    }

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        fake = new ServerFacade(port);
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
    public void badRegistration() {
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
    @Order(7)
    @DisplayName("Good List Games")
    public void goodListGames() throws Exception {
        fake.clear();
        String authToken = fake.register("myUser", "myPassword", "myEmail");
        fake.createGame(authToken, "firstGame");
        fake.createGame(authToken, "secondGame");
        Assertions.assertDoesNotThrow(() -> fake.listGames(authToken));
        fake.listGames(authToken);
        Assertions.assertEquals(2, fake.listGames(authToken).size());
    }

    @Test
    @Order(8)
    @DisplayName("Bad List Games")
    public void badListGames() throws Exception {
        fake.clear();
        String authToken = fake.register("myUser", "myPassword", "myEmail");
        Assertions.assertDoesNotThrow(() -> fake.listGames(authToken));
        Assertions.assertThrows(Exception.class, () -> fake.createGame(authToken, null));
        Assertions.assertThrows(Exception.class, () -> fake.createGame(authToken + "something else", "myGame"));
        Assertions.assertEquals(0, fake.listGames(authToken).size());
        Assertions.assertThrows(Exception.class, () -> fake.listGames("not the auth token"));
    }

    @Test
    @Order(9)
    @DisplayName("Good Create Game")
    public void goodCreateGame() throws Exception {
        fake.clear();
        String authToken = fake.register("myUser", "myPassword", "myEmail");
        Assertions.assertDoesNotThrow(() -> fake.createGame(authToken, "firstGame"));
        Assertions.assertDoesNotThrow(() -> fake.createGame(authToken, "firstGame"));
        Assertions.assertEquals(2, fake.listGames(authToken).size());
    }

    @Test
    @Order(10)
    @DisplayName("Bad Create Game")
    public void badCreateGame() throws Exception {
        fake.clear();
        String authToken = fake.register("myUser", "myPassword", "myEmail");
        Assertions.assertThrows(Exception.class, () -> fake.createGame(authToken + "yes", "firstGame"));
        Assertions.assertDoesNotThrow(() -> fake.createGame(authToken, "firstGame"));
        Assertions.assertEquals(1, fake.listGames(authToken).size());
    }

    @Test
    @Order(11)
    @DisplayName("Good Join Game")
    public void goodJoinGame() throws Exception {
        fake.clear();
        String authToken = fake.register("myUser", "myPassword", "myEmail");
        int gameID = fake.createGame(authToken, "firstGame");
        Assertions.assertDoesNotThrow(() -> fake.joinGame(authToken, ChessGame.TeamColor.BLACK, gameID));
        Assertions.assertEquals("myUser", fake.listGames(authToken).getFirst().blackUsername());
    }

    @Test
    @Order(12)
    @DisplayName("Good Join Game")
    public void badJoinGame() throws Exception {
        fake.clear();
        String authToken = fake.register("myUser", "myPassword", "myEmail");
        fake.createGame(authToken, "firstGame");
        fake.createGame(authToken, "secondGame");
        Assertions.assertThrows(Exception.class, () -> fake.joinGame(authToken, null, fake.listGames(authToken).getFirst().gameID()));
        Assertions.assertThrows(Exception.class, () -> fake.joinGame(authToken, ChessGame.TeamColor.BLACK, 0));
        Assertions.assertNotEquals("myUser", fake.listGames(authToken).getFirst().blackUsername());
    }

    @Test
    @Order(13)
    @DisplayName("Good Registration")
    public void goodClear() throws Exception {
        Assertions.assertDoesNotThrow(() -> fake.clear());
        fake.clear();
        Assertions.assertDoesNotThrow(() -> fake.clear());
        String authToken = fake.register("myUser", "myPassword", "myEmail");
        fake.createGame(authToken, "firstGame");
        fake.createGame(authToken, "secondGame");
        fake.listGames(authToken);
        Assertions.assertEquals(2, fake.listGames(authToken).size());
        fake.clear();
        Assertions.assertThrows(Exception.class, () -> fake.listGames(authToken).size());
    }

}
