package tcc.enterprise.fakenewsbot.Model.Messages.MessageInterativa;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tcc.enterprise.fakenewsbot.Model.Text;
@Getter
@Setter
@ToString
public class MessageInterativa {
    private String messaging_product;
    private String recipient_type;
    private String to;
    private String type;
    private Interactive interactive;

}
