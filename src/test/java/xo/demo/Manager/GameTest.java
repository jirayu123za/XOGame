package xo.demo.Manager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;
import xo.demo.Model.Game;
import xo.demo.State.GameState;

@ExtendWith(MockitoExtension.class)
public class GameTest{
    @Mock GameManager gameManager;

    // joinGame()
    @Test
    void testJoinGame_first(){
        GameManager manager = new GameManager();
        Game game = manager.joinGame("Player1");
        Assertions.assertEquals("Player1", game.getPlayer1());
        Assertions.assertNull(game.getPlayer2());
        Assertions.assertEquals(GameState.waitingPlayer, game.getGameState());
    }

    @Test
    void testJoinGame_second(){
        GameManager manager = new GameManager();
        manager.joinGame("Player1");
        Game game = manager.joinGame("Player2");
        Assertions.assertEquals("Player1", game.getPlayer1());
        Assertions.assertEquals("Player2", game.getPlayer2());
        Assertions.assertEquals(GameState.player1Turn, game.getGameState());
    }

    @Test
    void testJoinGame_alreadyInGame(){
        GameManager manager = new GameManager();
        Game game = manager.joinGame("Player1");
        Game game2 = manager.joinGame("Player1");
        Assertions.assertEquals(game, game2);
        Assertions.assertEquals("Player1", game2.getPlayer1());
        Assertions.assertNull(game2.getPlayer2());
        Assertions.assertEquals(GameState.waitingPlayer, game2.getGameState());
    }

    // leaveGame()
    @Test
    void testLeaveGame_first(){
        GameManager manager = new GameManager();
        Game game = manager.joinGame("Player1");
        manager.leaveGame("Player1");
        Assertions.assertNull(manager.getGame(game.getID()));
    }

    @Test
    void testLeaveGame_second(){
        GameManager manager = new GameManager();
        Game game = manager.joinGame("Player1");
        manager.joinGame("Player2");
        manager.leaveGame("Player2");
        Assertions.assertNotNull(manager.getGame(game.getID()));
        Assertions.assertEquals("Player1", manager.getGame(game.getID()).getPlayer1());
        Assertions.assertNull(manager.getGame(game.getID()).getPlayer2());
    }

    @Test
    void testLeaveGame_waitingPlayer(){
        GameManager manager = new GameManager();
        manager.joinGame("Player1");
        manager.leaveGame("Player1");
        Assertions.assertTrue(manager.waitingPlayers.isEmpty());
    }

    @Test
    void testLeaveGame_nonexistentPlayer(){
        GameManager manager = new GameManager();
        Game game = manager.joinGame("Player1");
        manager.leaveGame("Player2");
        Assertions.assertNotNull(manager.getGame(game.getID()));
        Assertions.assertEquals("Player1", manager.getGame(game.getID()).getPlayer1());
        Assertions.assertNull(manager.getGame(game.getID()).getPlayer2());
    }

    // removeGame()
    @Test
    void testRemoveGame_exists(){
        GameManager manager = new GameManager();
        Game game = manager.joinGame("Player1");
        manager.removeGame(game.getID());
        Assertions.assertNull(manager.getGame(game.getID()));
    }

    @Test
    void testRemoveGame_doesNotExist(){
        GameManager manager = new GameManager();
        Game game = manager.joinGame("Player1");
        manager.removeGame("Invalid game's ID");
        Assertions.assertNotNull(manager.getGame(game.getID()));
    }

    // getGame()
    @Test
    void testGetGame_exists(){
        GameManager manager = new GameManager();
        Game game = manager.joinGame("Player1");
        Assertions.assertEquals(game, manager.getGame(game.getID()));
    }

    @Test
    void testGetGame_doesNotExist() {
        GameManager manager = new GameManager();
        Game game = manager.joinGame("Player1");
        Assertions.assertNull(manager.getGame("Invalid game's ID"));
    }
}