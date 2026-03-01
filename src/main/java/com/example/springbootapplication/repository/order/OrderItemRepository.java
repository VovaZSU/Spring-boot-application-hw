package com.example.springbootapplication.repository.order;

import com.example.springbootapplication.model.order.OrderItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @EntityGraph(attributePaths = "book")
    List<OrderItem> findAllByOrderId(Long orderId, Pageable pageable);

    @EntityGraph(attributePaths = "book")
    Optional<OrderItem> findByIdAndOrderId(Long id, Long orderId);
}
