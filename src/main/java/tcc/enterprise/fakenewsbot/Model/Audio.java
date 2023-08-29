package tcc.enterprise.fakenewsbot.Model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Audio {
    private String mime_type;
    private String sha256;
    private String id;
    private boolean voice;

}
