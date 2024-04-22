package tcc.enterprise.fakenewsbot.Model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ButtonReplyCallBack {
    private String text;
    private String payload;
}
