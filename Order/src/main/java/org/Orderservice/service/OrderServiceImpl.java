package org.Orderservice.service;

import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.Orderservice.dto.OrderDto;
import org.Orderservice.entity.OrderEntity;
import org.Orderservice.repository.OrderRepository;
import org.bouncycastle.math.raw.Mod;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;

    @Override
    public OrderDto createOrder(OrderDto orderDetails) {

        orderDetails.setOrderId(UUID.randomUUID().toString());
        orderDetails.setTotalPrice(orderDetails.getQty()*orderDetails.getUnitPrice());
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        System.out.println(orderDetails.getOrderId());

        OrderEntity orderEntity = modelMapper.map(orderDetails,OrderEntity.class);

        System.out.println(orderEntity);

        orderRepository.save(orderEntity);



        OrderDto returnValue = modelMapper.map(orderEntity,OrderDto.class);
        return returnValue;
    }

    @Override
    public OrderDto getOrderByOrderId(String orderId) {
        OrderEntity orderEntity = orderRepository.findByOrderId(orderId).orElseThrow(()->new NotFoundException("없음"));

        OrderDto orderDto = new ModelMapper().map(orderEntity,OrderDto.class);

        return orderDto;
    }

    @Override
    public List<OrderEntity> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }
}
