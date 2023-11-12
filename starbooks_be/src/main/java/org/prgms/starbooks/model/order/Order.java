package org.prgms.starbooks.model.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Order {
    private final UUID orderId;
    private final UUID memberId;
    private OrderStatus orderStatus;
    private String address;
    private String postcode;
    private final List<OrderItem> orderItems;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Order(UUID orderId, UUID memberId, OrderStatus orderStatus, String address, String postcode, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.orderId = orderId;
        this.memberId = memberId;
        this.orderStatus = orderStatus;
        this.address = address;
        this.postcode = postcode;
        this.orderItems = null;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Order(UUID orderId, UUID memberId, OrderStatus orderStatus, String address, String postcode, List<OrderItem> orderItems, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.orderId = orderId;
        this.memberId = memberId;
        this.orderStatus = orderStatus;
        this.address = address;
        this.postcode = postcode;
        this.orderItems = orderItems;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getMemberId() {
        return memberId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public String getAddress() {
        return address;
    }

    public String getPostcode() {
        return postcode;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setAddress(String address) {
        this.address = address;
        this.updatedAt = LocalDateTime.now();
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
        this.updatedAt = LocalDateTime.now();
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        this.updatedAt = LocalDateTime.now();
    }

}
