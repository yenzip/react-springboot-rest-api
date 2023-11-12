package org.prgms.starbooks.controller.dto.product;

import jakarta.validation.constraints.NotBlank;
import org.prgms.starbooks.model.product.Category;
import org.prgms.starbooks.model.product.Product;

import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateProductRequest(
        UUID productId,
        @NotBlank(message = "Name must not be blank")
        String productName,
        @NotBlank(message = "Category must not be blank")
        Category category,
        @NotBlank(message = "Price must not be blank")
        long price,
        String description,
        String imageUrl) {
    public Product toProduct() {
        return new Product(productId, productName, category, price, description, imageUrl, LocalDateTime.now(), LocalDateTime.now());
    }
}
