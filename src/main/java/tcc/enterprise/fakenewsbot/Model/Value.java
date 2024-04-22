package tcc.enterprise.fakenewsbot.Model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
@Getter
@Setter
@ToString
public class Value {
    private String messaging_product;

    private MetaData metadata;

    private List<Contact> contacts;

    private List<Message> messages;

    private List<Statuses> statues;

}
