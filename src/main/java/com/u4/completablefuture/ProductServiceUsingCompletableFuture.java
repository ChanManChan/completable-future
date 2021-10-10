package com.u4.completablefuture;

import com.u4.domain.*;
import com.u4.service.InventoryService;
import com.u4.service.ProductInfoService;
import com.u4.service.ReviewService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.u4.util.CommonUtil.stopWatch;
import static com.u4.util.LoggerUtil.log;

public class ProductServiceUsingCompletableFuture {
    private final ProductInfoService productInfoService;
    private final ReviewService reviewService;
    private InventoryService inventoryService;

    public ProductServiceUsingCompletableFuture(ProductInfoService productInfoService, ReviewService reviewService) {
        this.productInfoService = productInfoService;
        this.reviewService = reviewService;
    }

    public ProductServiceUsingCompletableFuture(ProductInfoService productInfoService, ReviewService reviewService, InventoryService inventoryService) {
        this.productInfoService = productInfoService;
        this.reviewService = reviewService;
        this.inventoryService = inventoryService;
    }

    public Product retrieveProductDetails(String productId) {
        stopWatch.start();

        CompletableFuture<ProductInfo> productInfoCompletableFuture = CompletableFuture.supplyAsync(() -> productInfoService.retrieveProductInfo(productId));
        CompletableFuture<Review> reviewCompletableFuture = CompletableFuture.supplyAsync(() -> reviewService.retrieveReviews(productId));

        Product product = productInfoCompletableFuture
                .thenCombine(reviewCompletableFuture, (productInfo, review) -> new Product(productId, productInfo, review))
                .join(); // block the thread

        stopWatch.stop();
        log("Total Time Taken : " + stopWatch.getTime());
        return product;
    }

    // it's the responsibility of the client to perform any blocking operation in their end to retrieve the product information
    public CompletableFuture<Product> retrieveProductDetailsApproach2(String productId) {
        stopWatch.start();

        CompletableFuture<ProductInfo> productInfoCompletableFuture = CompletableFuture.supplyAsync(() -> productInfoService.retrieveProductInfo(productId));
        CompletableFuture<Review> reviewCompletableFuture = CompletableFuture.supplyAsync(() -> reviewService.retrieveReviews(productId));

        CompletableFuture<Product> productCompletableFuture = productInfoCompletableFuture
                .thenCombine(reviewCompletableFuture, (productInfo, review) -> new Product(productId, productInfo, review));

        stopWatch.stop();
        log("Total Time Taken : " + stopWatch.getTime());
        return productCompletableFuture;
    }

    public Product retrieveProductDetailsWithInventory(String productId) {
        stopWatch.start();

        CompletableFuture<ProductInfo> productInfoCompletableFuture = CompletableFuture
                .supplyAsync(() -> productInfoService.retrieveProductInfo(productId))
                .thenApply(productInfo -> {
                    productInfo.setProductOptions(updateInventory(productInfo));
                    return productInfo;
                });
        CompletableFuture<Review> reviewCompletableFuture = CompletableFuture.supplyAsync(() -> reviewService.retrieveReviews(productId));

        Product product = productInfoCompletableFuture
                .thenCombine(reviewCompletableFuture, (productInfo, review) -> new Product(productId, productInfo, review))
                .join(); // block the thread

        stopWatch.stop();
        log("Total Time Taken : " + stopWatch.getTime());
        return product;
    }

    private List<ProductOption> updateInventory(ProductInfo productInfo) {
        return productInfo.getProductOptions()
                .stream()// or use parallelStream() to use all the CPU cores efficiently
                .map(productOption -> {
                    Inventory inventory = inventoryService.retrieveInventory(productOption);
                    productOption.setInventory(inventory);
                    return productOption;
                })
                .collect(Collectors.toList());
    }

    public Product retrieveProductDetailsWithInventoryApproach2(String productId) {
        stopWatch.start();

        CompletableFuture<ProductInfo> productInfoCompletableFuture = CompletableFuture
                .supplyAsync(() -> productInfoService.retrieveProductInfo(productId))
                .thenApply(productInfo -> {
                    productInfo.setProductOptions(updateInventoryApproach2(productInfo));
                    return productInfo;
                });
        CompletableFuture<Review> reviewCompletableFuture = CompletableFuture.supplyAsync(() -> reviewService.retrieveReviews(productId));

        Product product = productInfoCompletableFuture
                .thenCombine(reviewCompletableFuture, (productInfo, review) -> new Product(productId, productInfo, review))
                .join(); // block the thread

        stopWatch.stop();
        log("Total Time Taken : " + stopWatch.getTime());
        return product;
    }

    private List<ProductOption> updateInventoryApproach2(ProductInfo productInfo) {
        List<CompletableFuture<ProductOption>> completableFutureList = productInfo.getProductOptions()
                .stream()
                .map(productOption ->
                        CompletableFuture.supplyAsync(() -> inventoryService.retrieveInventory(productOption))
                                .thenApply(inventory -> {
                                    productOption.setInventory(inventory);
                                    return productOption;
                                })
                )
                .collect(Collectors.toList());

        return completableFutureList
                .stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        ProductInfoService productInfoService = new ProductInfoService();
        ReviewService reviewService = new ReviewService();
        ProductServiceUsingCompletableFuture productService = new ProductServiceUsingCompletableFuture(productInfoService, reviewService);
        String productId = "ABC123";
        Product product = productService.retrieveProductDetails(productId);
        log("Product is " + product);
    }
}
