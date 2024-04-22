package tcc.enterprise.fakenewsbot.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

}
