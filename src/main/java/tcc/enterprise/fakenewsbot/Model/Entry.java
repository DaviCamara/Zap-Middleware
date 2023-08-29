package tcc.enterprise.fakenewsbot.Model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class Entry {
    private String id;
    private List<Change> changes;

}
