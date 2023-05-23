package xo.demo.Controller;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.event.*;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.*;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.stereotype.*;
import org.springframework.web.socket.messaging.*;
import xo.demo.Manager.*;
import xo.demo.Model.*;
import xo.demo.Model.Message.*;
import xo.demo.State.GameState;

/**
 * Controller class for handling WebSocket messages and managing the Tic-Tac-Toe games.
 */
@Controller
public class MessageController{
    // Template for sending messages to clients through the message broker.
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    //Manager for the XO games.
    private final GameManager gameManager = new GameManager();

    /**
     * Handles a request from a client to join a XO game.
     * If a game is available and the player is successfully added to the game,
     * the current state of the game is sent to all subscribers of the game's topic.
     * @param message the message from the client containing the player's name
     * @return the current state of the game, or an error message if the player was unable to join
     */
    @MessageMapping("/game.join")
    @SendTo("/topic/game.state")
    public Object joinGame(@Payload JoinMessage message, SimpMessageHeaderAccessor headerAccessor){
        Game game = gameManager.joinGame(message.getPlayer());

        if(game == null){
            GameMessage errorMessage = new GameMessage();
            errorMessage.setType("error");
            errorMessage.setContent("Unable to enter the game. Maybe the game is already full or an internal error has occurred.");
            return errorMessage;
        }
        headerAccessor.getSessionAttributes().put("gameID ", game.getID());
        headerAccessor.getSessionAttributes().put("player ", message.getPlayer());

        GameMessage gameMessage = gameToMessage(game);
        gameMessage.setTurn("game.joined");
        return gameMessage;
    }

    /**
     * Handles a request from a client to leave a XO game.
     * If the player is successfully removed from the game, a message is sent to subscribers
     * of the game's topic indicating that the player has left.
     * @param message the message from the client containing the player's name
     */
    @MessageMapping("/game.leave")
    public void leaveGame(@Payload PlayerMessage message){
        Game game = gameManager.leaveGame(message.getPlayer());
        if(game != null){
            GameMessage gameMessage = gameToMessage(game);
            gameMessage.setType("game.left");
            messagingTemplate.convertAndSend("/topic/game." + game.getID(), gameMessage);
        }
    }

    /**
     * Handles a request from a client to make a move in a XO game.
     * If the move is valid, the game state is updated and sent to all subscribers of the game's topic.
     * If the game is over, a message is sent indicating the result of the game.
     * @param message the message from the client containing the player's name, game ID, and move
     */
    @MessageMapping("/game.move")
    public void moveGame(@Payload GameMessage message){
        int move = message.getMove();
        String player = message.getSender();
        String gameID = message.getGameID();
        Game game = gameManager.getGame(gameID);

        if(game == null || game.isGameOver()){
            GameMessage errorMessage = new GameMessage();
            errorMessage.setType("error");
            errorMessage.setContent("Game not found or is already over.");
            this.messagingTemplate.convertAndSend("/topic/game." + gameID, errorMessage);
        }

        if(game.getGameState().equals(GameState.waitingPlayer)){
            GameMessage errorMessage = new GameMessage();
            errorMessage.setType("error");
            errorMessage.setContent("Game is waiting for another player to join.");
            this.messagingTemplate.convertAndSend("/topic/game." + gameID, errorMessage);
        }

        if(game.getTurn().equals(player)){
            game.Move(player, move);

            GameMessage gameStateMessage = new GameMessage(game);
            gameStateMessage.setType("game.move");
            gameStateMessage.setContent("Game is waiting for another player to join.");
            this.messagingTemplate.convertAndSend("/topic/game." + gameID, gameStateMessage);

            if(game.isGameOver()){
                GameMessage gameOverMessage = gameToMessage(game);
                gameOverMessage.setType("game.gameOver");
                this.messagingTemplate.convertAndSend("/topic/game." + gameID, gameOverMessage);
            }
        }
    }

    @EventListener
    public void SessionDisconnectEvent(SessionDisconnectEvent disconnectEvent){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(disconnectEvent.getMessage());
        String gameID = headerAccessor.getSessionAttributes().get("gameID").toString();
        String player = headerAccessor.getSessionAttributes().get("player").toString();
        Game game = gameManager.getGame(gameID);

        if(game != null){
            if(game.getPlayer1().equals(player)){
                game.setPlayer1(null);
                if(game.getPlayer2() != null){
                    game.setGameState(GameState.player2Win);
                    game.setWinner(game.getPlayer2());
                }else{
                    gameManager.removeGame(gameID);
                }
            }else if(game.getPlayer2() != null
                     && game.getPlayer2().equals(player)){
                game.setPlayer2(null);
                if(game.getPlayer1() != null){
                    game.setGameState(GameState.player1Win);
                    game.setWinner(game.getPlayer1());
                }else{
                    gameManager.removeGame(gameID);
                }
            }
            GameMessage gameMessage = gameToMessage(game);
            gameMessage.setType("game.gameOver");
            messagingTemplate.convertAndSend("/topic/game." + gameID, gameMessage);
            gameManager.removeGame(gameID);
        }
        // incomplete
    }

    private GameMessage gameToMessage(Game game){
        GameMessage message = new GameMessage();
        message.setPlayer1(game.getPlayer1());
        message.setPlayer2(game.getPlayer2());
        message.setGameID(game.getID());
        message.setBoard(game.getBoard());
        message.setTurn(game.getTurn());
        message.setWinner(game.getWinner());
        message.setGameState(game.getGameState());
        return message;
    }
}