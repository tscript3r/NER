package pl.tscript3r.ner.client;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@ToString
@Entity
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    private Integer externalId;
    private String company;
    private String contactName;
    private String street;
    private String city;
    private String state;
    private String postcode;
    private String country;

    public String toSearchBoxItem() {
        return company + ", " + contactName + ", " + street + ", " + city;
    }

}
