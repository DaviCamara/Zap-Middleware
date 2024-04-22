package tcc.enterprise.fakenewsbot.Model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Pricing {
    private String Category;
    private String authentication;
    private String marketing;
    private String utility;
    private String service;
    private String referral_conversion;
    private String pricing_model;
}
