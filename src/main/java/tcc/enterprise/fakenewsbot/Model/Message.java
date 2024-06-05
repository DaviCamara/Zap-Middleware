package tcc.enterprise.fakenewsbot.Model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.bind.annotation.ModelAttribute;
import tcc.enterprise.fakenewsbot.Model.Messages.MessageInterativa.Button;
import tcc.enterprise.fakenewsbot.Model.Messages.MessageInterativa.Interactive;

@Getter
@Setter
@ToString
public class Message {
    private String from;
    private String id;
    private String timestamp;
    private String type;
    private Audio audio;
    private Text text;
    private InteractiveReply interactive;
    private ButtonReplyCallBack button;
    private Context context;
    private Document document;
    private String video;
}
