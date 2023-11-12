package org.prgms.starbooks.controller.api;

import org.prgms.starbooks.model.product.Category;
import org.prgms.starbooks.model.product.Product;
import org.prgms.starbooks.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class ProductApiController {

    private final ProductService productService;

    public ProductApiController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/api/v1/products")
    public List<Product> getAllProducts(@RequestParam Optional<Category> category) {
        return category
                .map(productService::getProductByCategory)
                .orElse(productService.getAllProducts());
    }
}
