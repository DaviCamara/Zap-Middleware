package tcc.enterprise.fakenewsbot.Model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Origin {
    private String type;
    private String authentication;
    private String marketing;
    private String utility;
    private String service;
    private String referral_conversion;

}
