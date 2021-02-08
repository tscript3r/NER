package pl.tscript3r.ner.item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ItemExternalRelations {

    private final Integer orderExternalId;
    private final Integer itemExternalId;
    private final Integer quantity;

}
