package pl.tscript3r.ner.client;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

    @Query("SELECT c FROM ClientEntity c WHERE UPPER(c.city) LIKE CONCAT('%', UPPER(:phrase),'%') OR " +
            " UPPER(c.country) LIKE CONCAT('%', UPPER(:phrase),'%') OR " +
            " UPPER(c.contactName) LIKE CONCAT('%', UPPER(:phrase),'%') OR " +
            " UPPER(c.company) LIKE CONCAT('%', UPPER(:phrase),'%') OR " +
            " UPPER(c.street) LIKE CONCAT('%', UPPER(:phrase),'%') OR " +
            " UPPER(c.state) LIKE CONCAT('%', UPPER(:phrase),'%')")
    List<ClientEntity> findByPhrase(@Param("phrase") String phrase, Pageable pageable);

}
