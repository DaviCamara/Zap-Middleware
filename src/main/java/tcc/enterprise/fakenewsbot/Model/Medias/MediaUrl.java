package tcc.enterprise.fakenewsbot.Model.Medias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MediaUrl {
    private String url;
    private String mime_type;

    private String sha256;

    private String file_size;

    private String id;

    private String messaging_product;
}
