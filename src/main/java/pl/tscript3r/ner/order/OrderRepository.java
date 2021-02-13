package pl.tscript3r.ner.order;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import pl.tscript3r.ner.client.ClientEntity;

import java.util.List;

interface OrderRepository extends PagingAndSortingRepository<OrderEntity, Long> {

    List<OrderEntity> findAllByClient(ClientEntity clientEntity);

    @Query("SELECT o FROM OrderEntity o WHERE UPPER(o.description) LIKE CONCAT('%', UPPER(:phrase),'%')")
    List<OrderEntity> findByPhrase(String phrase, Pageable pageable);

}
