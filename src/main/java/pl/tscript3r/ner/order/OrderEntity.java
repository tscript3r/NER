package pl.tscript3r.ner.order;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
    private Integer externalId;
    private Integer clientId;
    private LocalDate dateOfReceipt;
    private String description;
    private LocalDate dateOfCompletion;

}
