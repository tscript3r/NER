package pl.tscript3r.ner.item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ItemImported {

    private final Integer externalId;
    private final ItemEntity itemEntity;

}
