package pl.tscript3r.ner.client;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

    List<ClientEntity> findAllByCompanyOrContactNameIsContaining(String company, String contactName);

}
