package org.prgms.starbooks.repository.order;

import org.prgms.starbooks.exception.DatabaseException;
import org.prgms.starbooks.model.order.Order;
import org.prgms.starbooks.model.order.OrderItem;
import org.prgms.starbooks.model.order.OrderStatus;
import org.prgms.starbooks.model.product.Category;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

import static org.prgms.starbooks.utils.JdbcUtils.toLocalDateTime;
import static org.prgms.starbooks.utils.JdbcUtils.toUUID;

@Repository
public class JdbcOrderRepository implements OrderRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcOrderRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Order insert(Order order) {
        int update = jdbcTemplate.update("INSERT INTO orders(order_id, member_id, order_status, address, postcode, created_at, updated_at) " +
                        "VALUES (UUID_TO_BIN(:orderId), UUID_TO_BIN(:memberId), :orderStatus, :address, :postcode, :createdAt, :updatedAt)",
                toOrderParamMap(order));

        if (update != 1) {
            throw new DatabaseException("주문 insert 중 에러가 발생했습니다.");
        }

        order.getOrderItems()
                .forEach(item -> jdbcTemplate.update("INSERT INTO order_items(order_id, product_id, category, price, quantity) " +
                                "VALUES (UUID_TO_BIN(:orderId), UUID_TO_BIN(:productId), :category, :price, :quantity)",
                        toOrderItemParamMap(order.getOrderId(), item)));
        return order;
    }

    @Override
    public Order update(Order order) {
        int update = jdbcTemplate.update(
                "UPDATE orders SET order_status = :orderStatus, address = :address, postcode = :postcode, updated_at = :updatedAt " +
                        "WHERE order_id = UUID_TO_BIN(:orderId)",
                toOrderParamMap(order)
        );
        if (update != 1) {
            throw new DatabaseException("주문 update 중 에러가 발생했습니다.");
        }
        return order;
    }

    @Override
    public List<Order> findAll() {
        return jdbcTemplate.query("SELECT * FROM orders", orderRowMapper);
    }

    @Override
    public Optional<Order> findById(UUID orderId) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject("SELECT * FROM orders WHERE order_id = UUID_TO_BIN(:orderId)",
                            Collections.singletonMap("orderId", orderId.toString().getBytes()),
                            orderRowMapper)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<OrderItem> findItemsById(UUID orderId) {
        return jdbcTemplate.query(
                "SELECT * FROM order_items WHERE order_id = UUID_TO_BIN(:orderId)",
                Collections.singletonMap("orderId", orderId.toString().getBytes()),
                orderItemRowMapper
        );
    }

    @Override
    public List<Order> findByMemberId(UUID memberId) {
        return jdbcTemplate.query(
                "SELECT * FROM orders WHERE member_id = UUID_TO_BIN(:memberId)",
                Collections.singletonMap("memberId", memberId.toString().getBytes()),
                orderRowMapper
        );
    }

    @Override
    public List<Order> findByStatus(OrderStatus orderStatus) {
        return jdbcTemplate.query(
                "SELECT * FROM orders WHERE order_status = :orderStatus",
                Collections.singletonMap("orderStatus", orderStatus.toString()),
                orderRowMapper
        );
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM orders", Collections.emptyMap());
    }

    @Override
    public void deleteById(UUID orderId) {
        jdbcTemplate.update("DELETE FROM order_items WHERE order_id = UUID_TO_BIN(:orderId)", Collections.singletonMap("orderId", orderId.toString().getBytes()));
        jdbcTemplate.update("DELETE FROM orders WHERE order_id = UUID_TO_BIN(:orderId)", Collections.singletonMap("orderId", orderId.toString().getBytes()));
    }

    private static final RowMapper<OrderItem> orderItemRowMapper = (resultSet, i) -> {
        UUID productId = toUUID(resultSet.getBytes("product_id"));
        Category category = Category.valueOf(resultSet.getString("category"));
        long price = resultSet.getLong("price");
        long quantity = resultSet.getLong("quantity");
        return new OrderItem(productId, category, price, quantity);
    };

    private static final RowMapper<Order> orderRowMapper = (resultSet, i) -> {
        UUID orderId = toUUID(resultSet.getBytes("order_id"));
        UUID memberId = toUUID(resultSet.getBytes("member_id"));
        OrderStatus orderStatus = OrderStatus.valueOf(resultSet.getString("order_status"));
        String address = resultSet.getString("address");
        String postcode = resultSet.getString("postcode");
        LocalDateTime createdAt = toLocalDateTime(resultSet.getTimestamp("created_at"));
        LocalDateTime updatedAt = toLocalDateTime(resultSet.getTimestamp("updated_at"));
        return new Order(orderId, memberId, orderStatus, address, postcode, createdAt, updatedAt);
    };

    private Map<String, Object> toOrderParamMap(Order order) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("orderId", order.getOrderId().toString().getBytes());
        paramMap.put("memberId", order.getMemberId().toString().getBytes());
        paramMap.put("orderStatus", order.getOrderStatus().toString());
        paramMap.put("address", order.getAddress());
        paramMap.put("postcode", order.getPostcode());
        paramMap.put("createdAt", order.getCreatedAt());
        paramMap.put("updatedAt", order.getUpdatedAt());
        return paramMap;
    }

    private Map<String, Object> toOrderItemParamMap(UUID orderId, OrderItem item) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("orderId", orderId.toString().getBytes());
        paramMap.put("productId", item.productId().toString().getBytes());
        paramMap.put("category", item.category().toString());
        paramMap.put("price", item.price());
        paramMap.put("quantity", item.quantity());
        return paramMap;
    }
}
