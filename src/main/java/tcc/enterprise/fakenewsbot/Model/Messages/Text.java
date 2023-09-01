package tcc.enterprise.fakenewsbot.Model.Messages;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Text {
    private Boolean preview_url;
    private String body;
}
