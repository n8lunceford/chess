package service;

import chess.ChessGame;
import dataaccess.*;
import model.*;
import services.GameService;
import services.ProfileService;

import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameServiceTests {

    private GameService service;
    private ProfileService myProfile;
    private AuthData myAuth;
    private AuthData secondAuth;
    private SQLUserDAO userDAO = new SQLUserDAO();
    private SQLAuthDAO authDAO = new SQLAuthDAO();

    public GameServiceTests() throws DataAccessException {
    }

    @BeforeEach
    public void setUp() throws DataAccessException {
        userDAO.clear();
        authDAO.clear();
        myProfile = new ProfileService(userDAO, authDAO);
        myProfile.registration(new UserData("myUsername", "myPassword", "myGmail"));
        myProfile.registration(new UserData("secondUsername", "secondPassword", "secondGmail"));
        myAuth = myProfile.login(new LoginRequest("myUsername", "myPassword"));
        secondAuth = myProfile.login(new LoginRequest("secondUsername", "secondPassword"));
        service = new GameService(new SQLGameDAO(), myProfile.getAuthDAO());
    }

    @Test
    @Order(1)
    @DisplayName("Good ListGames")
    public void goodListGames() throws DataAccessException {
        service.createGame(new CreateGameRequest(myAuth.authToken(), "Game 1"));
        service.createGame(new CreateGameRequest(myAuth.authToken(), "Game 2"));
        Assertions.assertDoesNotThrow(() -> (service.listGames(new ListGamesRequest(myAuth.authToken()))));
    }

    @Test
    @Order(2)
    @DisplayName("Bad ListGames")
    public void badListGames() throws DataAccessException {
        service.createGame(new CreateGameRequest(myAuth.authToken(), "Game 1"));
        service.createGame(new CreateGameRequest(myAuth.authToken(), "Game 2"));
        Assertions.assertThrows(DataAccessException.class, () -> service.listGames(new ListGamesRequest("Wrong token")));
    }

    @Test
    @Order(3)
    @DisplayName("Good CreateGame")
    public void goodCreateGame() {
        Assertions.assertDoesNotThrow(() -> service.createGame(new CreateGameRequest(myAuth.authToken(), "Game 1")));
    }

    @Test
    @Order(4)
    @DisplayName("Bad CreateGame")
    public void badCreateGame() {
        Assertions.assertThrows(DataAccessException.class, () -> service.createGame(new CreateGameRequest("Wrong token", "Game 1")));
    }

    @Test
    @Order(5)
    @DisplayName("Good JoinGame")
    public void goodJoinGame() throws DataAccessException {
        int myID = service.createGame(new CreateGameRequest(myAuth.authToken(), "Game 1"));
        Assertions.assertDoesNotThrow(() -> service.joinGame(new JoinGameRequest(myAuth.authToken(), ChessGame.TeamColor.WHITE, myID)));
    }

    @Test
    @Order(6)
    @DisplayName("Bad JoinGame")
    public void badJoinGame() throws DataAccessException {
        int myID = service.createGame(new CreateGameRequest(myAuth.authToken(), "Game 1"));
        Assertions.assertThrows(DataAccessException.class, () -> service.joinGame(new JoinGameRequest("Wrong token", ChessGame.TeamColor.BLACK, myID)));
        Assertions.assertThrows(DataAccessException.class, () -> service.joinGame(new JoinGameRequest(myAuth.authToken(), ChessGame.TeamColor.BLACK, 0)));
        service.joinGame(new JoinGameRequest(myAuth.authToken(), ChessGame.TeamColor.WHITE, myID));
        Assertions.assertThrows(DataAccessException.class, () -> service.joinGame(new JoinGameRequest(secondAuth.authToken(), ChessGame.TeamColor.WHITE, myID)));
    }



}
