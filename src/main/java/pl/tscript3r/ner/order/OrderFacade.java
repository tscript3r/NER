package pl.tscript3r.ner.order;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.tscript3r.ner.client.ClientEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class OrderFacade {

    private final OrderRepository orderRepository;

    public OrderEntity save(OrderEntity orderEntity) {
        return orderRepository.save(orderEntity);
    }

    @Cacheable("latestOrders")
    public List<OrderEntity> getLatest(Double orderCount) {
        return orderRepository.findAll(PageRequest.of(0, orderCount.intValue(), Sort.by(Sort.Direction.DESC, "id"))).toList();
    }

    @Cacheable("clientsOrders")
    public List<OrderEntity> getFromClient(ClientEntity clientEntity) {
        return orderRepository.findAllByClient(clientEntity);
    }

    public Stream<OrderEntity> getFromClientAndFromPhrase(String searchPhrase, ClientEntity clientEntity) {
        Stream<OrderEntity> fromClientStream = getFromClient(clientEntity).stream();
        return Stream.concat(fromClientStream, orderRepository.findByPhrase(searchPhrase,
                PageRequest.of(0, 200, Sort.by(Sort.Direction.DESC, "id"))).stream());
    }

    public Optional<OrderEntity> getById(Long id) {
        return orderRepository.findById(id);
    }

}
