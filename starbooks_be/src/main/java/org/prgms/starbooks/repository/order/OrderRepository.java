package org.prgms.starbooks.repository.order;

import org.prgms.starbooks.model.order.Order;
import org.prgms.starbooks.model.order.OrderItem;
import org.prgms.starbooks.model.order.OrderStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Order insert(Order order);

    Order update(Order order);

    List<Order> findAll();

    Optional<Order> findById(UUID orderId);

    List<OrderItem> findItemsById(UUID orderId);

    List<Order> findByMemberId(UUID memberId);

    List<Order> findByStatus(OrderStatus orderStatus);

    void deleteAll();

    void deleteById(UUID orderId);
}
