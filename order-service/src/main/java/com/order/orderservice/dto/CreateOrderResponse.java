package com.order.orderservice.dto;

import com.order.orderservice.enums.OrderStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateOrderResponse {

    private UUID orderId;
    private String status;
}
