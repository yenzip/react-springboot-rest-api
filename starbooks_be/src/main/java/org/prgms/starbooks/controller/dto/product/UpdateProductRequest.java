package org.prgms.starbooks.controller.dto.product;

import org.prgms.starbooks.model.product.Category;
import org.prgms.starbooks.model.product.Product;

import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateProductRequest(UUID productId, String productName, Category category, long price,
                                   String description,
                                   String imageUrl) {
    public Product toProduct() {
        return new Product(productId, productName, category, price, description, imageUrl, LocalDateTime.now(), LocalDateTime.now());
    }
}
