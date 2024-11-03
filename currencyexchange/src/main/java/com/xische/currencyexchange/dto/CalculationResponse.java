package com.xische.currencyexchange.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CalculationResponse {
    private double netPayableAmount;
    private String currency;
}
