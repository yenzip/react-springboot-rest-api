package org.prgms.starbooks.service;

import org.prgms.starbooks.model.product.Category;
import org.prgms.starbooks.model.product.Product;
import org.prgms.starbooks.repository.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product createProduct(String productName, Category category, long price, String description, String imageUrl) {
        Product product = new Product(UUID.randomUUID(), productName, category, price, description, imageUrl, LocalDateTime.now(), LocalDateTime.now());
        return productRepository.insert(product);
    }

    @Transactional
    public Product updateProduct(Product product) {
        return productRepository.update(product);
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Product getProductById(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public Product getProductByName(String productName) {
        return productRepository.findByName(productName)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public List<Product> getProductByCategory(Category category) {
        return productRepository.findByCategory(category);
    }

    @Transactional
    public void deleteProduct(UUID productId) {
        productRepository.deleteById(productId);
    }
}
