package com.xische.currencyexchange.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Item {
    private String name;
    private String category;
    private double amount;
    public boolean isGrocery() {
        return "Grocery".equalsIgnoreCase(category);
    }

}
