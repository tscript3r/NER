package pl.tscript3r.ner.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ClientImported {

    private final Long externalId;
    private final ClientEntity clientEntity;

}
