package org.prgms.starbooks.controller.view;

import org.prgms.starbooks.model.order.Order;
import org.prgms.starbooks.model.order.OrderItem;
import org.prgms.starbooks.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@RequestMapping("/orders")
@Controller
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String getAllOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "order/order-list";
    }

    @GetMapping("/{id}")
    public String getOrderItemsById(@PathVariable("id") UUID orderId, Model model) {
        List<OrderItem> orderItems = orderService.getOrderItemsById(orderId);
        model.addAttribute("orderItems", orderItems);
        return "order/order-detail";
    }
}
