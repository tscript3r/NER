package pl.tscript3r.ner.order;

import org.springframework.data.repository.PagingAndSortingRepository;

interface OrderRepository extends PagingAndSortingRepository<OrderEntity, Long> {
}
