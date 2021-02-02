package pl.tscript3r.ner.client;

import lombok.RequiredArgsConstructor;
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

    public List<ClientEntity> search(String string) {
        return clientRepository.findAllByCompanyOrContactNameIsContaining(string, string);
    }

    public Optional<ClientEntity> getById(Long id) {
        return clientRepository.findById(id);
    }

}
