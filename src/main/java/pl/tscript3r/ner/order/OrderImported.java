package pl.tscript3r.ner.order;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class OrderImported extends OrderEntity {

    private String clientName;

}
