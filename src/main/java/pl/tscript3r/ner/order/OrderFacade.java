package pl.tscript3r.ner.order;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderFacade {

    private final OrderRepository orderRepository;

    public void save(OrderEntity orderEntity) {
        orderRepository.save(orderEntity);
    }

    public List<OrderEntity> getAll() {
        return orderRepository.findAll(PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, "id"))).toList();
    }

}
