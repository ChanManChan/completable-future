package com.u4.service;

import com.u4.domain.Inventory;
import com.u4.domain.ProductOption;

import static com.u4.util.CommonUtil.delay;

public class InventoryService {
    public Inventory retrieveInventory(ProductOption productOption) {
        delay(500);
        return Inventory.builder()
                .count(2).build();
    }
}
