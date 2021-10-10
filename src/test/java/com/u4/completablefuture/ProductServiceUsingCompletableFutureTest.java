package com.u4.completablefuture;

import com.u4.domain.Product;
import com.u4.service.InventoryService;
import com.u4.service.ProductInfoService;
import com.u4.service.ReviewService;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceUsingCompletableFutureTest {
    private final ProductInfoService productInfoService = new ProductInfoService();
    private final ReviewService reviewService = new ReviewService();
    private final InventoryService inventoryService = new InventoryService();
    private final ProductServiceUsingCompletableFuture productServiceUsingCompletableFuture = new ProductServiceUsingCompletableFuture(productInfoService, reviewService, inventoryService);

    @Test
    void retrieveProductDetails() {
        String productId = "ABC123";
        Product product = productServiceUsingCompletableFuture.retrieveProductDetails(productId);
        assertNotNull(product);
        assertTrue(product.getProductInfo().getProductOptions().size() > 0);
        assertNotNull(product.getReview());
    }

    @Test
    void retrieveProductDetailsApproach2() {
        String productId = "ABC123";
        CompletableFuture<Product> productCompletableFuture = productServiceUsingCompletableFuture.retrieveProductDetailsApproach2(productId);
        Product product = productCompletableFuture.join();
        assertNotNull(product);
        assertTrue(product.getProductInfo().getProductOptions().size() > 0);
        assertNotNull(product.getReview());
    }

    @Test
    void retrieveProductDetailsWithInventory() {
        String productId = "ABC123";
        Product product = productServiceUsingCompletableFuture.retrieveProductDetailsWithInventory(productId);
        assertNotNull(product);
        assertTrue(product.getProductInfo().getProductOptions().size() > 0);
        product.getProductInfo().getProductOptions().forEach(productOption -> assertNotNull(productOption.getInventory()));
        assertNotNull(product.getReview());
    }

    @Test
    void retrieveProductDetailsWithInventoryApproach2() {
        String productId = "ABC123";
        Product product = productServiceUsingCompletableFuture.retrieveProductDetailsWithInventoryApproach2(productId);
        assertNotNull(product);
        assertTrue(product.getProductInfo().getProductOptions().size() > 0);
        product.getProductInfo().getProductOptions().forEach(productOption -> assertNotNull(productOption.getInventory()));
        assertNotNull(product.getReview());
    }
}