package pl.tscript3r.ner.client;

import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
public class ClientEntity {

    private Integer externalId;
    private String company;
    private String contactName;
    private String street;
    private String city;
    private String state;
    private String postcode;
    private String country;

}
