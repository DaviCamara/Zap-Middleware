package tcc.enterprise.fakenewsbot.Model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Conversation {
    private String id;
    private Origin origin;
    private String expiration_timestamp;
}
