package org.prgms.starbooks.controller.view;

import org.prgms.starbooks.controller.dto.product.CreateProductRequest;
import org.prgms.starbooks.controller.dto.product.UpdateProductRequest;
import org.prgms.starbooks.model.product.Product;
import org.prgms.starbooks.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/products")
@Controller
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String getAllProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "product/product-list";
    }

    @GetMapping("/save")
    public String createProduct() {
        return "product/product-new";
    }

    @PostMapping("/save")
    public String createProduct(CreateProductRequest request) {
        productService.createProduct(
                request.productName(),
                request.category(),
                request.price(),
                request.description(),
                request.imageUrl()
        );
        return "redirect:/products";
    }

    @GetMapping("/{id}")
    public String getProduct(@PathVariable("id") UUID productId, Model model) {
        Product product = productService.getProductById(productId);
        model.addAttribute("product", product);
        return "product/product-detail";
    }

    @GetMapping("/{id}/edit")
    public String updateProduct(@PathVariable("id") UUID productId, Model model) {
        model.addAttribute("product", productService.getProductById(productId));
        return "product/product-edit";
    }

    @PostMapping("/{id}/edit")
    public String updateProduct(@PathVariable("id") UUID productId, @ModelAttribute UpdateProductRequest request) {
        productService.updateProduct(request.toProduct());
        return "redirect:/products/" + productId;
    }

    @GetMapping("/{id}/delete")
    public String deleteProduct(@PathVariable("id") UUID productId) {
        productService.deleteProduct(productId);
        return "redirect:/products";
    }
}
