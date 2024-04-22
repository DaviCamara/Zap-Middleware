package tcc.enterprise.fakenewsbot.Model.Messages.MessageInterativa;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tcc.enterprise.fakenewsbot.Model.button_reply;

@Getter
@Setter
@ToString
public class Interactive {
    private String type;
    private Body body;
    private Action action;
    private button_reply buttonReply;

}
