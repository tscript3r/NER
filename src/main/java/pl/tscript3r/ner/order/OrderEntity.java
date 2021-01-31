package pl.tscript3r.ner.order;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.tscript3r.ner.client.ClientEntity;

import javax.persistence.*;
import java.time.LocalDate;

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

}
