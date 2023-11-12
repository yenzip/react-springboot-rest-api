package org.prgms.starbooks.controller.dto.order;

import org.prgms.starbooks.model.order.OrderItem;

import java.util.List;

public record CreateOrderRequest(String email, String address, String postcode, List<OrderItem> orderItems) {
}
