package com.order.orderservice.service;

import com.order.orderservice.dto.CreateOrderRequest;
import com.order.orderservice.dto.CreateOrderResponse;
import com.order.orderservice.entity.Order;
import com.order.orderservice.enums.OrderStatus;
import com.order.orderservice.event.OrderCreatedEvent;
import com.order.orderservice.producer.OrderEventProducer;
import com.order.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    private final OrderEventProducer orderEventProducer;

    public OrderService(OrderRepository orderRepository, OrderEventProducer orderEventProducer) {
        this.orderRepository = orderRepository;
        this.orderEventProducer = orderEventProducer;
    }

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest createOrderRequest){
        Order order = new Order();

        order.setOrderId(UUID.randomUUID());
        order.setStatus(OrderStatus.CREATED);
        order.setProductId(createOrderRequest.getProductId());
        order.setQuantity(createOrderRequest.getQuantity());
        order.setCreatedAt(LocalDateTime.now());

        orderRepository.save(order);

        // 🔥 PUBLISH EVENT AFTER DB SAVE
        OrderCreatedEvent event = new OrderCreatedEvent(
                order.getOrderId(),
                order.getProductId(),
                order.getQuantity(),
                order.getStatus().name()
        );

        orderEventProducer.publishOrderCreatedEvent(event);

        CreateOrderResponse response = new CreateOrderResponse();

        response.setOrderId(order.getOrderId());
        response.setStatus(order.getStatus().name());

        return response;
    }
}
