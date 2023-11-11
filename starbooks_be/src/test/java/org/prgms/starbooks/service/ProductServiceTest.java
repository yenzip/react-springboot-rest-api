package org.prgms.starbooks.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgms.starbooks.model.product.Category;
import org.prgms.starbooks.model.product.Product;
import org.prgms.starbooks.repository.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("상품을 추가(생성)할 수 있다.")
    void createProduct() {
        // when
        Product product = productService.createProduct("Test Product", Category.IT, 10000, "Test Description", "test-image.jpg");

        // then
        Optional<Product> findProduct = productRepository.findById(product.getProductId());
        assertThat(findProduct).isPresent();
    }

    @Test
    @DisplayName("상품 정보를 수정할 수 있다.")
    void updateProduct() {
        // given
        Product product = productService.createProduct("Test Product", Category.IT, 10000, "Test Description", "test-image.jpg");

        // when
        product.setProductName("Update Product");
        product.setPrice(300);
        Product updateProduct = productService.updateProduct(product);

        // then
        assertThat(updateProduct.getProductName()).isEqualTo("Update Product");
        assertThat(updateProduct.getPrice()).isEqualTo(300);
    }

    @Test
    @DisplayName("모든 상품을 조회할 수 있다.")
    void getAllProducts() {
        // given
        productService.createProduct("Test Product 1", Category.IT, 10000, "Test Description 1", "test-image-1.jpg");
        productService.createProduct("Test Product 2", Category.NOVEL, 20000, "Test Description 2", "test-image-2.jpg");

        // when
        List<Product> products = productService.getAllProducts();

        // then
        assertThat(products).hasSize(2);
    }

    @Test
    @DisplayName("ID로 상품을 조회할 수 있다.")
    void getProductById() {
        // given
        Product product = productService.createProduct("Test Product", Category.IT, 10000, "Test Description", "test-image.jpg");
        UUID productId = product.getProductId();

        // when
        Product findProduct = productService.getProductById(productId);

        // then
        assertThat(findProduct.getProductId()).isEqualTo(productId);
    }

    @Test
    @DisplayName("존재하지 않은 ID로 상품을 조회할 때, 에러 메세지를 던진다.")
    void getProductByNonExistentId() {
        // given
        UUID nonExistentId = UUID.randomUUID();

        // then
        assertThatThrownBy(() -> productService.getProductById(nonExistentId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("이름으로 상품을 조회할 수 있다.")
    void getProductByName() {
        // given
        String productName = "Test Product";
        Product product = productService.createProduct(productName, Category.IT, 10000, "Test Description", "test-image.jpg");

        // when
        Product findProduct = productService.getProductByName(productName);

        // then
        assertThat(findProduct.getProductName()).isEqualTo(productName);
    }

    @Test
    @DisplayName("존재하지 않은 이름으로 조회할 때, 에러 메세지를 던진다.")
    void getProductByNonExistentName() {
        // given
        String nonExistentName = "Non Existent Product";

        // then
        assertThatThrownBy(() -> productService.getProductByName(nonExistentName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("카테고리로 상품을 조회할 수 있다.")
    void getProductByCategory() {
        // given
        productService.createProduct("Test Product 1", Category.IT, 10000, "Test Description 1", "test-image-1.jpg");
        productService.createProduct("Test Product 2", Category.NOVEL, 20000, "Test Description 2", "test-image-2.jpg");

        // when
        List<Product> products = productService.getProductByCategory(Category.IT);

        // then
        assertThat(products).hasSize(1);
    }

    @Test
    @DisplayName("상품을 삭제할 수 있다.")
    void deleteProduct() {
        // given
        Product product = productService.createProduct("Test Product 1", Category.IT, 10000, "Test Description 1", "test-image-1.jpg");
        UUID productId = product.getProductId();

        // when
        productService.deleteProduct(productId);

        // then
        Optional<Product> findProduct = productRepository.findById(productId);
        assertThat(findProduct).isNotPresent();
    }
}

