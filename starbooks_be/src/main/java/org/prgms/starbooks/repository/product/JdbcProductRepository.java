package org.prgms.starbooks.repository.product;

import org.prgms.starbooks.exception.DatabaseException;
import org.prgms.starbooks.model.product.Category;
import org.prgms.starbooks.model.product.Product;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

import static org.prgms.starbooks.utils.JdbcUtils.*;

@Repository
public class JdbcProductRepository implements ProductRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcProductRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Product insert(Product product) {
        int update = jdbcTemplate.update("INSERT INTO products(product_id, product_name, category, price, description, image_url, created_at, updated_at) " +
                        "VALUES(UUID_TO_BIN(:productId), :productName, :category, :price, :description, :imageUrl, :createdAt, :updatedAt)",
                toParamMap(product));
        if(update != 1) {
            throw new DatabaseException("상품 insert 중 에러가 발생했습니다.");
        }
        return product;
    }

    @Override
    public Product update(Product product) {
        int update = jdbcTemplate.update("UPDATE products SET product_name = :productName, category = :category, price = :price, description = :description, image_url = :imageUrl, updated_at = :updatedAt " +
                        "WHERE product_id = UUID_TO_BIN(:productId)",
                toParamMap(product));
        if(update != 1) {
            throw new DatabaseException("상품 update 중 에러가 발생했습니다.");
        }
        return product;
    }

    @Override
    public List<Product> findAll() {
        return jdbcTemplate.query("SELECT * FROM products",productRowMapper);
    }

    @Override
    public Optional<Product> findById(UUID productId) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject("SELECT * FROM products WHERE product_id = UUID_TO_BIN(:productId)",
                            Collections.singletonMap("productId", productId.toString().getBytes()),
                            productRowMapper)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Product> findByName(String productName) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject("SELECT * FROM products WHERE product_name = :productName",
                            Collections.singletonMap("productName", productName),
                            productRowMapper)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Product> findByCategory(Category category) {
        return jdbcTemplate.query(
                "SELECT * FROM products WHERE category = :category",
                Collections.singletonMap("category", category.toString()),
                productRowMapper
        );
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM products", Collections.emptyMap());
    }

    @Override
    public void deleteById(UUID productId) {
        jdbcTemplate.update("DELETE FROM products WHERE product_id = UUID_TO_BIN(:productId)",
                Collections.singletonMap("productId", productId.toString().getBytes()));
    }

    private static final RowMapper<Product>productRowMapper= (resultSet, i) -> {
        UUID productId =toUUID(resultSet.getBytes("product_id"));
        String productName = resultSet.getString("product_name");
        Category category = Category.valueOf(resultSet.getString("category"));
        long price = Long.parseLong(resultSet.getString("price"));
        String description = resultSet.getString("description");
        String imageUrl = resultSet.getString("image_url");
        LocalDateTime createdAt =toLocalDateTime(resultSet.getTimestamp("created_at"));
        LocalDateTime updatedAt =toLocalDateTime(resultSet.getTimestamp("updated_at"));
        return new Product(productId, productName, category, price, description, imageUrl, createdAt, updatedAt);
    };

    private Map<String, Object> toParamMap(Product product) {
        Map<String, Object> param = new HashMap<>();
        param.put("productId", product.getProductId().toString().getBytes());
        param.put("productName", product.getProductName());
        param.put("category", product.getCategory().toString());
        param.put("price", product.getPrice());
        param.put("description", product.getDescription());
        param.put("imageUrl", product.getImageUrl());
        param.put("createdAt", product.getCreatedAt());
        param.put("updatedAt", product.getUpdatedAt());
        return param;
    }
}