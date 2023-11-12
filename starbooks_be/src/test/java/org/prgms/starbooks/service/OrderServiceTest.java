package org.prgms.starbooks.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgms.starbooks.model.member.Member;
import org.prgms.starbooks.model.order.Order;
import org.prgms.starbooks.model.order.OrderItem;
import org.prgms.starbooks.model.order.OrderStatus;
import org.prgms.starbooks.model.product.Category;
import org.prgms.starbooks.model.product.Product;
import org.prgms.starbooks.repository.order.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ProductService productService;

    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
    }

    @Test
    @DisplayName("주문을 추가(생성)할 수 있다.")
    void createOrder() {
        // given
        Member member = memberService.createMember("test", "test@naver.com", "서울 노원구", "000111");
        Product product = productService.createProduct("book", Category.IT, 10000, null, null);
        List<OrderItem> orderItems = List.of(new OrderItem(product.getProductId(), Category.IT, 10000, 2));

        // when
        Order order = orderService.createOrder(member.getMemberId(), "서울 노원구", "000111", orderItems);

        // then
        Optional<Order> findOrder = orderRepository.findById(order.getOrderId());
        assertThat(findOrder).isPresent();
    }

    @Test
    @DisplayName("주문 정보(주소)를 수정할 수 있다.")
    void updateOrder() {
        // given
        Member member = memberService.createMember("test", "test@naver.com", "서울 노원구", "000111");
        Product product = productService.createProduct("book", Category.IT, 10000, null, null);
        List<OrderItem> orderItems = List.of(new OrderItem(product.getProductId(), Category.IT, 10000, 2));
        Order order = orderService.createOrder(member.getMemberId(), "서울 노원구", "000111", orderItems);

        // when
        order.setAddress("서울 광진구");
        order.setPostcode("111222");
        orderService.updateOrder(order);

        // then
        Order findOrder = orderService.getOrderById(order.getOrderId());
        assertThat(findOrder.getAddress()).isEqualTo("서울 광진구");
        assertThat(findOrder.getPostcode()).isEqualTo("111222");
    }

    @Test
    @DisplayName("모든 주문을 조회할 수 있다.")
    void getAllOrders() {
        // given
        Member member = memberService.createMember("test", "test@naver.com", "서울 노원구", "000111");
        Product product = productService.createProduct("book", Category.IT, 10000, null, null);
        List<OrderItem> orderItems = List.of(new OrderItem(product.getProductId(), Category.IT, 10000, 2));
        orderService.createOrder(member.getMemberId(), "서울 노원구", "000111", orderItems);
        orderService.createOrder(member.getMemberId(), "서울 노원구", "000111", orderItems);

        // when
        List<Order> orders = orderService.getAllOrders();

        // then
        assertThat(orders).hasSize(2);
    }

    @Test
    @DisplayName("ID로 주문을 조회할 수 있다.")
    void getOrderById() {
        // given
        Member member = memberService.createMember("test", "test@naver.com", "서울 노원구", "000111");
        Product product = productService.createProduct("book", Category.IT, 10000, null, null);
        List<OrderItem> orderItems = List.of(new OrderItem(product.getProductId(), Category.IT, 10000, 2));
        Order order = orderService.createOrder(member.getMemberId(), "서울 노원구", "000111", orderItems);
        UUID orderId = order.getOrderId();

        // when
        Order findOrder = orderService.getOrderById(orderId);

        // then
        assertThat(findOrder.getOrderId()).isEqualTo(orderId);
    }

    @Test
    @DisplayName("존재하지 않은 ID로 주문을 조회할 때, 에러 메세지를 던진다.")
    void getOrderByNonExistentId() {
        // given
        UUID nonExistentId = UUID.randomUUID();

        // then
        assertThatThrownBy(() -> orderService.getOrderById(nonExistentId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 내역이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("주문 ID로 주문한 상품 리스트를 조회할 수 있다.")
    void getOrderItemsById() {
        // given
        Member member = memberService.createMember("test", "test@naver.com", "서울 노원구", "000111");
        Product product = productService.createProduct("book", Category.IT, 10000, null, null);
        List<OrderItem> orderItems = List.of(new OrderItem(product.getProductId(), Category.IT, 10000, 2));
        Order order = orderService.createOrder(member.getMemberId(), "서울 노원구", "000111", orderItems);

        // when
        List<OrderItem> findOrderItems = orderService.getOrderItemsById(order.getOrderId());

        // then
        assertThat(findOrderItems).hasSize(1);
    }

    @Test
    @DisplayName("고객 ID로 주문 내역을 조회할 수 있다.")
    void getOrderByMemberId() {
        // given
        Member member = memberService.createMember("test", "test@naver.com", "서울 노원구", "000111");
        Product product = productService.createProduct("book", Category.IT, 10000, null, null);
        List<OrderItem> orderItems = List.of(new OrderItem(product.getProductId(), Category.IT, 10000, 2));
        Order order = orderService.createOrder(member.getMemberId(), "서울 노원구", "000111", orderItems);

        // when
        List<Order> findOrder = orderService.getOrderByMemberId(member.getMemberId());

        // then
        assertThat(findOrder).hasSize(1);
    }

    @Test
    @DisplayName("주문 상태로 주문 내역을 조회할 수 있다.")
    void getOrderByStatus() {
        // given
        Member member = memberService.createMember("test", "test@naver.com", "서울 노원구", "000111");
        Product product = productService.createProduct("book", Category.IT, 10000, null, null);
        List<OrderItem> orderItems = List.of(new OrderItem(product.getProductId(), Category.IT, 10000, 2));
        Order order = orderService.createOrder(member.getMemberId(), "서울 노원구", "000111", orderItems);

        // when
        List<Order> findOrder = orderService.getOrderByStatus(OrderStatus.ACCEPTED);

        // then
        assertThat(findOrder).hasSize(1);
    }

    @Test
    @DisplayName("주문 내역을 삭제할 수 있다.")
    void deleteOrder() {
        // given
        Member member = memberService.createMember("test", "test@naver.com", "서울 노원구", "000111");
        Product product = productService.createProduct("book", Category.IT, 10000, null, null);
        List<OrderItem> orderItems = List.of(new OrderItem(product.getProductId(), Category.IT, 10000, 2));
        Order order = orderService.createOrder(member.getMemberId(), "서울 노원구", "000111", orderItems);
        UUID orderId = order.getOrderId();

        // when
        orderService.deleteOrder(orderId);

        // then
        Optional<Order> findOrder = orderRepository.findById(orderId);
        assertThat(findOrder).isNotPresent();
    }
}