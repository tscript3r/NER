package pl.tscript3r.ner.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderImported {

    private final Integer externalId;
    private final Long clientId;
    private final OrderEntity orderEntity;

}
