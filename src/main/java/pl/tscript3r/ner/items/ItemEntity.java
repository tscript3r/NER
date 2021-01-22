package pl.tscript3r.ner.items;

import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
public class ItemEntity {

    private Integer externalId;
    private String name;
    private Integer price;
    private String description;

}
