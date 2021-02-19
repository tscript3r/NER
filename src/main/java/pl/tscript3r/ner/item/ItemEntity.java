package pl.tscript3r.ner.item;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.tscript3r.ner.db.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.Lob;

@Setter
@Getter
@ToString
@Entity
public class ItemEntity extends AbstractEntity {

    private String name;
    private Integer price;
    private Integer quantity;

    @Lob
    private String description;

}
