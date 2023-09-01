package tcc.enterprise.fakenewsbot.Model.Messages;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MessageAvulsa {
    private String messaging_product;
    private String recipient_type;
    private String to;
    private String type;
    private Text text;
}
