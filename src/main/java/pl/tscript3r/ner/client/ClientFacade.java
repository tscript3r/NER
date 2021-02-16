package pl.tscript3r.ner.client;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientFacade {

    private final ClientRepository clientRepository;

    public void save(ClientEntity clientEntity) {
        clientRepository.save(clientEntity);
    }

    public List<ClientEntity> search(String searchFraze, Double limit) {
        return clientRepository.findByPhrase(searchFraze,
                PageRequest.of(0, limit.intValue(), Sort.by(Sort.Direction.DESC, "id")));
    }

    public Optional<ClientEntity> getById(Long id) {
        return clientRepository.findById(id);
    }

}
