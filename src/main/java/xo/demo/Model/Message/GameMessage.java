package xo.demo.Model.Message;
import lombok.*;
import xo.demo.Model.Game;
import xo.demo.State.GameState;

@Setter @Getter
public class GameMessage{
    private String type;
    private String player1, player2;
    private String gameID;
    private String content;
    private String winner;
    private String turn;
    private String sender;
    private String[][] board;
    private int move;
    private GameState gameState;

    public GameMessage(){}
    public GameMessage(Game game){
        this.gameID = game.getID();
        this.player1 = game.getPlayer1();
        this.player2 = game.getPlayer2();
        this.winner = game.getWinner();
        this.turn = game.getTurn();
        this.board = game.getBoard();
        this.gameState = game.getGameState();
    }
}