package dataaccess;

import chess.ChessGame;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SQLGameDAOTests {

    private SQLGameDAO gameDAO;

    SQLGameDAOTests() {
        try {
            gameDAO = new SQLGameDAO();
        }
        catch (DataAccessException ignore) {}
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        gameDAO.clear();
    }

    @Test
    @Order(1)
    @DisplayName("Good Clear")
    public void goodClear() throws DataAccessException {
        gameDAO.createGame("myGame");
        gameDAO.createGame("secondGame");
        Assertions.assertDoesNotThrow(() -> gameDAO.clear());
        gameDAO.clear();
        Assertions.assertDoesNotThrow(() -> gameDAO.clear());
    }

    @Test
    @Order(2)
    @DisplayName("Good Create Game")
    public void goodCreateGame() throws DataAccessException {
        Assertions.assertDoesNotThrow(() -> gameDAO.createGame("myGame"));
        gameDAO.createGame("myGame");
        Assertions.assertDoesNotThrow(() -> gameDAO.createGame("myGame"));
        Assertions.assertDoesNotThrow(() -> gameDAO.createGame("secondGame"));
    }

    @Test
    @Order(3)
    @DisplayName("Bad Create Game")
    public void badCreateGame() {
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.createGame(null));
    }

    @Test
    @Order(4)
    @DisplayName("Good Get Game")
    public void goodGetGame() throws DataAccessException {
        int gameOne = gameDAO.createGame("myGame");
        int gameTwo = gameDAO.createGame("secondGame");
        int gameThree = gameDAO.createGame("thirdGame");
        Assertions.assertNotNull(gameDAO.getGame(gameOne));
        Assertions.assertNotNull(gameDAO.getGame(gameTwo));
        Assertions.assertNotNull(gameDAO.getGame(gameThree));
    }

    @Test
    @Order(5)
    @DisplayName("Bad Get Game")
    public void badGetGame() throws DataAccessException {
        int gameOne = gameDAO.createGame("myGame");
        int gameTwo = gameDAO.createGame("secondGame");
        int gameThree = gameDAO.createGame("thirdGame");
        int nullID = gameOne + gameTwo + gameThree;
        Assertions.assertNull(gameDAO.getGame(nullID));
    }

    @Test
    @Order(6)
    @DisplayName("Good List Games")
    public void goodListGames() throws DataAccessException {
        gameDAO.createGame("myGame");
        gameDAO.createGame("secondGame");
        gameDAO.createGame("thirdGame");
        Assertions.assertDoesNotThrow(() -> gameDAO.listGames());
        Assertions.assertEquals(3, gameDAO.listGames().size());
    }

    @Test
    @Order(7)
    @DisplayName("Bad List Games")
    public void badListGames() throws DataAccessException {
        gameDAO.createGame("myGame");
        gameDAO.createGame("secondGame");
        gameDAO.createGame("thirdGame");
        gameDAO.clear();
        Assertions.assertEquals(0, gameDAO.listGames().size());
    }

    @Test
    @Order(8)
    @DisplayName("Good Update Games")
    public void goodUpdateGames() throws DataAccessException {
        int myGameID = gameDAO.createGame("myGame");
        Assertions.assertDoesNotThrow(() -> gameDAO.updateGame("myUser", ChessGame.TeamColor.WHITE, gameDAO.getGame(myGameID)));
    }

    @Test
    @Order(9)
    @DisplayName("Bad Update Games")
    public void badUpdateGames() throws DataAccessException {
        int myGameID = gameDAO.createGame("myGame");
        gameDAO.updateGame("myUser", ChessGame.TeamColor.WHITE, gameDAO.getGame(myGameID));
        //Assertions.assertThrows(DataAccessException.class, () -> gameDAO.updateGame(null, ChessGame.TeamColor.BLACK, gameDAO.getGame(myGameID)));
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.updateGame("otherUser", ChessGame.TeamColor.BLACK, null));
    }

}
