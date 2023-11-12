package org.prgms.starbooks.controller.dto.order;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.prgms.starbooks.model.order.OrderItem;

import java.util.List;

public record CreateOrderRequest(
        @NotBlank(message = "Email must not be blank")
        @Email(message = "Invalid email address")
        String email,
        @NotBlank(message = "Address must not be blank")
        String address,
        @NotBlank(message = "Postcode must not be blank")
        String postcode,
        List<OrderItem> orderItems) {
}
