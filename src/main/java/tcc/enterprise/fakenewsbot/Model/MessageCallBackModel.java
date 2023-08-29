package tcc.enterprise.fakenewsbot.Model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MessageCallBackModel {

    private String object;
    private List<Entry> entry;

}
