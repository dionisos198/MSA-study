package org.Orderservice.repository;

import java.util.List;
import java.util.Optional;
import org.Orderservice.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity,Long> {

    Optional<OrderEntity> findByOrderId(String orderId);
    List<OrderEntity> findByUserId(String userId);


}
