package pl.tscript3r.ner.order;

import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@Setter
public class OrderEntity {

    private Integer clientId;
    private LocalDate dateOfReceipt;
    private String description;
    private LocalDate dateOfCompletion;

}
