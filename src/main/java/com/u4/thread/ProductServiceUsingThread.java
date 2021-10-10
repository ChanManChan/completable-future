package com.u4.thread;

import com.u4.domain.Product;
import com.u4.domain.ProductInfo;
import com.u4.domain.Review;
import com.u4.service.ProductInfoService;
import com.u4.service.ReviewService;

import static com.u4.util.CommonUtil.stopWatch;
import static com.u4.util.LoggerUtil.log;

public class ProductServiceUsingThread {
    private final ProductInfoService productInfoService;
    private final ReviewService reviewService;

    public ProductServiceUsingThread(ProductInfoService productInfoService, ReviewService reviewService) {
        this.productInfoService = productInfoService;
        this.reviewService = reviewService;
    }

    public Product retrieveProductDetails(String productId) throws InterruptedException {
        stopWatch.start();

        ProductInfoRunnable productInfoRunnable = new ProductInfoRunnable(productId);
        Thread productInfoThread = new Thread(productInfoRunnable);

        ReviewRunnable reviewRunnable = new ReviewRunnable(productId);
        Thread reviewThread = new Thread(reviewRunnable);

        productInfoThread.start();
        reviewThread.start();

        productInfoThread.join();
        reviewThread.join();

        ProductInfo productInfo = productInfoRunnable.getProductInfo();
        Review review = reviewRunnable.getReview();

        stopWatch.stop();
        log("Total Time Taken : " + stopWatch.getTime());
        return new Product(productId, productInfo, review);
    }

    public static void main(String[] args) throws InterruptedException {
        ProductInfoService productInfoService = new ProductInfoService();
        ReviewService reviewService = new ReviewService();
        ProductServiceUsingThread productService = new ProductServiceUsingThread(productInfoService, reviewService);
        String productId = "ABC123";
        Product product = productService.retrieveProductDetails(productId);
        log("Product is " + product);
    }

    private class ProductInfoRunnable implements Runnable {
        private ProductInfo productInfo;
        private final String productId;

        public ProductInfoRunnable(String productId) {
            this.productId = productId;
        }

        public ProductInfo getProductInfo() {
            return productInfo;
        }

        @Override
        public void run() {
            productInfo = productInfoService.retrieveProductInfo(productId);
        }
    }

    private class ReviewRunnable implements Runnable {
        private final String productId;
        private Review review;

        public ReviewRunnable(String productId) {
            this.productId = productId;
        }

        public Review getReview() {
            return review;
        }

        @Override
        public void run() {
            review = reviewService.retrieveReviews(productId);
        }
    }
}
