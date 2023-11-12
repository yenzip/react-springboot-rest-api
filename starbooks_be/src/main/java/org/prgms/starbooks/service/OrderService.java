package org.prgms.starbooks.service;

import org.prgms.starbooks.model.order.Order;
import org.prgms.starbooks.model.order.OrderItem;
import org.prgms.starbooks.model.order.OrderStatus;
import org.prgms.starbooks.repository.order.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order createOrder(UUID memberId, String address, String postcode, List<OrderItem> orderItems) {
        Order order = new Order(UUID.randomUUID(), memberId, OrderStatus.ACCEPTED, address, postcode, orderItems, LocalDateTime.now(), LocalDateTime.now());
        return orderRepository.insert(order);
    }

    @Transactional
    public Order updateOrder(Order order) {
        return orderRepository.update(order);
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Order getOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문 내역이 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public List<OrderItem> getOrderItemsById(UUID orderId) {
        return orderRepository.findItemsById(orderId);
    }

    @Transactional(readOnly = true)
    public List<Order> getOrderByMemberId(UUID memberId) {
        return orderRepository.findByMemberId(memberId);
    }

    @Transactional(readOnly = true)
    public List<Order> getOrderByStatus(OrderStatus orderStatus) {
        return orderRepository.findByStatus(orderStatus);
    }

    @Transactional
    public void deleteOrder(UUID orderId) {
        orderRepository.deleteById(orderId);
    }
}
