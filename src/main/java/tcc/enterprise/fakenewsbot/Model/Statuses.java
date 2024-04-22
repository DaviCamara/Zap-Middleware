package tcc.enterprise.fakenewsbot.Model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class Statuses {
    private String biz_opaque_callback_data;
    private Conversation conversation;
    private String id;
    private Pricing pricing;
    private String recipient_id;
    private String status;
    private Long timestamp;


}
