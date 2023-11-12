package org.prgms.starbooks.model.order;

import org.prgms.starbooks.model.product.Category;

import java.util.UUID;

public record OrderItem(UUID productId, Category category, long price, long quantity) {
}