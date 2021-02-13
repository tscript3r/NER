package pl.tscript3r.ner.order;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.tscript3r.ner.client.ClientEntity;
import pl.tscript3r.ner.item.ItemEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@ToString
@Entity
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;
    @OneToOne
    private ClientEntity client;
    private LocalDate dateOfReceipt;
    private String description;
    private LocalDate dateOfCompletion;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ItemEntity> itemEntityList = new ArrayList<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderEntity that = (OrderEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void addItem(ItemEntity itemEntity) {
        itemEntityList.add(itemEntity);
    }

}
