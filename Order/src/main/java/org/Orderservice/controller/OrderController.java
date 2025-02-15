package org.Orderservice.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.Orderservice.dto.OrderDto;
import org.Orderservice.dto.RequestOrder;
import org.Orderservice.dto.ResponseOrder;
import org.Orderservice.service.KafkaProducer;
import org.Orderservice.service.OrderProducer;
import org.Orderservice.service.OrderService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final KafkaProducer kafkaProducer;
    private final OrderProducer orderProducer;


    @PostMapping(value = "/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId") String userId,
            @RequestBody RequestOrder orderDetails){

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        OrderDto orderDto = modelMapper.map(orderDetails, OrderDto.class);
        orderDto.setUserId(userId);
        /*jpa
        OrderDto createDto = orderService.createOrder(orderDto);

        ResponseOrder returnValue = modelMapper.map(createDto,ResponseOrder.class);
        */

        /*kafka*/
        orderDto.setOrderId(UUID.randomUUID().toString());
        orderDto.setTotalPrice(orderDetails.getQty()*orderDetails.getUnitPrice());
        ResponseOrder returnValue = modelMapper.map(orderDto,ResponseOrder.class);



        kafkaProducer.send("example-order-topic",orderDto);
        orderProducer.send("orders",orderDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);

    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrder(@PathVariable("userId") String userId){

        log.info("Before retrieve orders data");

        System.out.println("hi");

        List<ResponseOrder> collect = orderService.getOrdersByUserId(userId).stream()
                .map(orderEntity -> new ModelMapper().map(orderEntity, ResponseOrder.class))
                .collect(
                        Collectors.toList());

        log.info("Add retrieved orders data");

        return ResponseEntity.ok(collect);


    }

}
