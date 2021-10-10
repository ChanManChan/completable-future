package com.u4.completablefuture;

import com.u4.domain.Product;
import com.u4.domain.ProductOption;
import com.u4.service.InventoryService;
import com.u4.service.ProductInfoService;
import com.u4.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceUsingCompletableFutureExceptionHandlingTest {

    @Mock
    private ProductInfoService productInfoServiceMock;

    @Mock
    private ReviewService reviewServiceMock;

    @Mock
    private InventoryService inventoryServiceMock;

    @InjectMocks
    private ProductServiceUsingCompletableFuture productServiceUsingCompletableFuture;

    @Test
    void retrieveProductDetailsWithInventoryApproach2Test() {
        String productId = "ABC123";
        when(productInfoServiceMock.retrieveProductInfo(anyString())).thenCallRealMethod();
        when(reviewServiceMock.retrieveReviews(anyString())).thenThrow(new RuntimeException("Exception occurred"));
        when(inventoryServiceMock.retrieveInventory(any(ProductOption.class))).thenCallRealMethod();

        Product product = productServiceUsingCompletableFuture.retrieveProductDetailsWithInventoryApproach2(productId);

        assertNotNull(product);
        assertTrue(product.getProductInfo().getProductOptions().size() > 0);
        product.getProductInfo().getProductOptions().forEach(productOption -> assertNotNull(productOption.getInventory()));
        assertNotNull(product.getReview());
        assertEquals(0, product.getReview().getNoOfReviews());
        assertEquals(0.0, product.getReview().getOverallRating());
    }

    @Test
    void retrieveProductDetailsWithInventoryProductInfoServiceError() {
        String productId = "ABC123";
        when(productInfoServiceMock.retrieveProductInfo(anyString())).thenThrow(new RuntimeException("Exception occurred"));
        when(reviewServiceMock.retrieveReviews(anyString())).thenCallRealMethod();

        assertThrows(RuntimeException.class, () -> productServiceUsingCompletableFuture.retrieveProductDetailsWithInventoryApproach2(productId));
    }
}