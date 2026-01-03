package com.inventory.inventoryservice.service;

import com.inventory.inventoryservice.entity.Inventory;
import com.inventory.inventoryservice.event.OrderCreatedEvent;
import com.inventory.inventoryservice.repository.InventoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository){
        this.inventoryRepository = inventoryRepository;
    }

    public void handleOrderCreated(OrderCreatedEvent event){
        Inventory inventory = inventoryRepository.findById(event.getProductId())
                .orElseThrow(
                        ()->new RuntimeException("Product not found")
                );

        if(inventory.getAvailableQuantity()<event.getQuantity()){
            throw new RuntimeException("Insufficient stock");
        }

        inventory.setAvailableQuantity(
                inventory.getAvailableQuantity()-event.getQuantity()
        );

        inventoryRepository.save(inventory);
    }
}
