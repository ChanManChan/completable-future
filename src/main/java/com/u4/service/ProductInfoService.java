package com.u4.service;

import com.u4.domain.ProductInfo;
import com.u4.domain.ProductOption;

import java.util.List;

import static com.u4.util.CommonUtil.delay;

public class ProductInfoService {

    public ProductInfo retrieveProductInfo(String productId) {
        delay(1000);
        // number of items = number of CPU cores - 1 (for parallelism)
        List<ProductOption> productOptions = List.of(
                new ProductOption(1, "64GB", "Black", 699.99),
                new ProductOption(2, "128GB", "Green", 749.99),
                new ProductOption(3, "512GB", "Red", 849.99),
                new ProductOption(4, "256GB", "Blue", 339.99),
                new ProductOption(5, "1024GB", "Yellow", 999.99),
                new ProductOption(6, "2048GB", "Violet", 109.99),
                new ProductOption(7, "32GB", "Pink", 289.99)
        );
        return ProductInfo.builder().productId(productId)
                .productOptions(productOptions)
                .build();
    }
}
