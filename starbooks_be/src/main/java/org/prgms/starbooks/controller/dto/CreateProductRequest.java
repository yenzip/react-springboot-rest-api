package org.prgms.starbooks.controller.dto;

import org.prgms.starbooks.model.product.Category;

public record CreateProductRequest(String productName, Category category, long price,
                                   String description,
                                   String imageUrl) {
}
