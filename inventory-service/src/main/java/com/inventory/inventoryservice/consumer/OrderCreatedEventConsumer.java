package com.inventory.inventoryservice.consumer;

import com.inventory.inventoryservice.event.OrderCreatedEvent;
import com.inventory.inventoryservice.service.InventoryService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedEventConsumer {

    private final InventoryService inventoryService;

    public OrderCreatedEventConsumer(InventoryService inventoryService){
        this.inventoryService = inventoryService;
    }

    @KafkaListener(
            topics = "order.created",
            groupId = "inventory-service"
    )
    public void consume(OrderCreatedEvent event){
        inventoryService.handleOrderCreated(event);
    }
}
