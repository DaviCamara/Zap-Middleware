package tcc.enterprise.fakenewsbot.Model.Messages.MessageInterativa;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Interactive {
    private String type;
    private  Body body;

    private Action action;

}
