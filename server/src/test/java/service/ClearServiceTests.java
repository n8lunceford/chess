package service;

import chess.ChessGame;
import dataaccess.*;
import model.*;
import services.*;

import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClearServiceTests {

    private ProfileService profileService;
    private GameService gameService;
    private AuthData myAuth;
    private AuthData secondAuth;
    private ClearService garbageMan;
    private SQLUserDAO userDAO = new SQLUserDAO();
    private SQLGameDAO gameDAO = new SQLGameDAO();
    private SQLAuthDAO authDAO = new SQLAuthDAO();

    public ClearServiceTests() throws DataAccessException {
    }

    @BeforeEach
    public void setUp() throws DataAccessException {
        //garbageMan.clear();
        userDAO.clear();
        gameDAO.clear();
        authDAO.clear();
        profileService = new ProfileService(new SQLUserDAO(), new SQLAuthDAO());
        profileService.registration(new UserData("myUsername", "myPassword", "myGmail"));
        profileService.registration(new UserData("secondUsername", "secondPassword", "secondGmail"));
        myAuth = profileService.login(new LoginRequest("myUsername", "myPassword"));
        secondAuth = profileService.login(new LoginRequest("secondUsername", "secondPassword"));
        gameService = new GameService(new SQLGameDAO(), profileService.getAuthDAO());
        int gameID = gameService.createGame(new CreateGameRequest(myAuth.authToken(), "My new game"));
        gameService.joinGame(new JoinGameRequest(myAuth.authToken(), ChessGame.TeamColor.WHITE, gameID));
        gameService.joinGame(new JoinGameRequest(secondAuth.authToken(), ChessGame.TeamColor.BLACK, gameID));
        garbageMan = new ClearService(gameService.getAuthDAO(), profileService.getUserDAO(), gameService.getGameDAO());
    }

    @Test
    @Order(1)
    @DisplayName("Good ClearService")
    public void goodClearService() throws DataAccessException {
        Assertions.assertDoesNotThrow(() -> garbageMan.clear());
        garbageMan.clear();
        Assertions.assertDoesNotThrow(() -> garbageMan.clear());
    }

    @Test
    @Order(2)
    @DisplayName("Bad ClearService")
    public void badClearService() throws DataAccessException {
        ClearService badService = new ClearService(null, null, null);
        Assertions.assertThrows(DataAccessException.class, () -> badService.clear());
    }

}
