package tcc.enterprise.fakenewsbot.Model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class Document {
    private String id;
    private String link;
    private String caption;
    private String filename;
    private String provider;
    private String mime_type;
    private String sha256;

}
