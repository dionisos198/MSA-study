package org.Orderservice.service;

import java.util.List;
import org.Orderservice.dto.OrderDto;
import org.Orderservice.entity.OrderEntity;

public interface OrderService {

    OrderDto createOrder(OrderDto orderDetails);
    OrderDto getOrderByOrderId(String orderId);

    List<OrderEntity> getOrdersByUserId(String userId);





}
