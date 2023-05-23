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
        // incomplete
    }

    @Test
    void testJoinGame_alreadyInGame(){
        // incomplete
    }

    // leaveGame()
    @Test
    void testLeaveGame_first(){
        // incomplete
    }

    @Test
    void testLeaveGame_second(){
        // incomplete
    }

    @Test
    void testLeaveGame_waitingPlayer(){
        // incomplete
    }

    @Test
    void testLeaveGame_nonexistentPlayer(){
        // incomplete
    }

    // removeGame()
    @Test
    void testRemoveGame_exists(){
        // incomplete
    }

    @Test
    void testRemoveGame_doesNotExist(){
        // incomplete
    }

    // getGame()
    @Test
    void testGetGame_exists(){
        // incomplete
    }

    @Test
    void testGetGame_doesNotExist() {
        // incomplete
    }
}