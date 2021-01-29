package pl.tscript3r.ner.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientFacade {

    private final ClientRepository clientRepository;

    public void save(ClientEntity clientEntity) {
        clientRepository.save(clientEntity);
    }

    public List<String> search(String string) {
        return clientRepository.findAllByCompanyOrContactNameIsContaining(string, string)
                .stream()
                .map(ClientEntity::toSearchBoxItem)
                .collect(Collectors.toList());
    }

}
