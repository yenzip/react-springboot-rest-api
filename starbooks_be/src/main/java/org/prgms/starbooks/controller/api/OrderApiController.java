package org.prgms.starbooks.controller.api;

import org.prgms.starbooks.controller.dto.order.CreateOrderRequest;
import org.prgms.starbooks.model.member.Member;
import org.prgms.starbooks.model.order.Order;
import org.prgms.starbooks.service.MemberService;
import org.prgms.starbooks.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderApiController {

    private final OrderService orderService;
    private final MemberService memberService;

    public OrderApiController(OrderService orderService, MemberService memberService) {
        this.orderService = orderService;
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody @Validated CreateOrderRequest request) {
        Member member = memberService.getMemberByEmail(request.email());
        Order order = orderService.createOrder(member.getMemberId(), request.address(), request.postcode(), request.orderItems());
        return ResponseEntity.created(URI.create("/api/vi/orders/" + order.getOrderId()))
                .body(order);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable("id") UUID orderId) {
        Order order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }
}