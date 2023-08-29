package tcc.enterprise.fakenewsbot.Model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class Value {
    private String messaging_product;

    private MetaData metadata;

    private List<Contact> contacts;

    private List<Message> messages;

}
