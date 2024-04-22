package tcc.enterprise.fakenewsbot.Model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Context {
    private String message_id;
    private String from;
}
