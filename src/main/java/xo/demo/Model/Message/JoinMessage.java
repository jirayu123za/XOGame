package xo.demo.Model.Message;
import lombok.*;

@Setter @Getter
public class JoinMessage{
    private String type;
    private String player;
    private String gameID;
    private String content;
}