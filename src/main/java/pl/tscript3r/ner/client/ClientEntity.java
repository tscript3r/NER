package pl.tscript3r.ner.client;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.tscript3r.ner.db.AbstractEntity;

import javax.persistence.Entity;

@Setter
@Getter
@ToString
@Entity
public class ClientEntity extends AbstractEntity {

    private String company;
    private String contactName;
    private String street;
    private String city;
    private String state;
    private String postcode;
    private String country;

}
