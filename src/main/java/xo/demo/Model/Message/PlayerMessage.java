package xo.demo.Model.Message;
import lombok.*;

@Setter
@Getter
public class PlayerMessage{
    private String type;
    private String player;
    private String gameID;
    private String content;
}