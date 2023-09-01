package tcc.enterprise.fakenewsbot.Model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.bind.annotation.ModelAttribute;

@Getter
@Setter
@ToString
public class Message {
    private String from;
    private String id;
    private String timestamp;
    private String type;
    private Audio audio;
}
