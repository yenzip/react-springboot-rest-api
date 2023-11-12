package org.prgms.starbooks.controller.dto.product;

import jakarta.validation.constraints.NotBlank;
import org.prgms.starbooks.model.product.Category;

public record CreateProductRequest(
        @NotBlank(message = "Name must not be blank")
        String productName,
        @NotBlank(message = "Category must not be blank")
        Category category,
        @NotBlank(message = "Price must not be blank")
        long price,
        String description,
        String imageUrl) {
}
