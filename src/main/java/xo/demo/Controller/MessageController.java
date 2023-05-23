package xo.demo.Controller;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.event.*;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.*;
import org.springframework.stereotype.*;
import org.springframework.web.socket.messaging.*;
import xo.demo.Manager.*;
import xo.demo.Model.*;
import xo.demo.Model.Message.*;

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


    @MessageMapping("/game.join")
    @SendTo("/topic/game.state")
    public Object joinGame(){
        // incomplete
        return null;
    }

    @MessageMapping("/game.leave")
    public void leaveGame(){
        // incomplete
    }

    @MessageMapping("/game.move")
    public void moveGame(){
        // incomplete
    }

    @EventListener
    public void SessionDisconnectEvent(SessionDisconnectEvent disconnectEvent){
        // incomplete
    }

    private GameMessage gameToMessage(Game game){
        GameMessage message = new GameMessage();
        // incomplete
        return message;
    }
}