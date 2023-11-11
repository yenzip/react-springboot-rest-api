package org.prgms.starbooks.repository.product;

import org.prgms.starbooks.model.product.Category;
import org.prgms.starbooks.model.product.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    Product insert(Product product);
    Product update(Product product);
    List<Product> findAll();
    Optional<Product> findById(UUID productId);
    Optional<Product> findByName(String productName);
    List<Product> findByCategory(Category category);
    void deleteAll();
    void deleteById(UUID productId);
}
