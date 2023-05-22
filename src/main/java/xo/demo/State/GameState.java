package xo.demo.State;
import lombok.Getter;
import lombok.Setter;

public enum GameState{
    waitingPlayer("Waiting for player"),
    player1Turn("Turn of Player1"),
    player2Turn("Turn of Player2"),
    player1Win("Player1 win!!"),
    player2Win("Player2 win!!"),
    tie("Tie");

    @Setter
    @Getter
    String text;

    GameState(String text){
        this.text = text;
    }
}