package pl.tscript3r.ner.order;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.tscript3r.ner.client.ClientEntity;
import pl.tscript3r.ner.db.AbstractEntity;
import pl.tscript3r.ner.item.ItemEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
@Entity
public class OrderEntity extends AbstractEntity {

    @OneToOne
    private ClientEntity client;
    private LocalDate dateOfReceipt;
    private String description;
    private LocalDate dateOfCompletion;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ItemEntity> itemEntityList = new ArrayList<>();

    public void addItem(ItemEntity itemEntity) {
        itemEntityList.add(itemEntity);
    }

}
