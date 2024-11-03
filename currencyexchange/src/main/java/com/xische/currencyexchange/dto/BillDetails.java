package com.xische.currencyexchange.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BillDetails {
    private List<Item> items;
    private double totalAmount;
    private String userType;
    private int customerTenure;
    private String originalCurrency;
    private String targetCurrency;

}
