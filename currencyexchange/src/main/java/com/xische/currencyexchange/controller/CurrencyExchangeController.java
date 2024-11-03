package com.xische.currencyexchange.controller;

import com.xische.currencyexchange.dto.BillDetails;
import com.xische.currencyexchange.dto.CalculationResponse;
import com.xische.currencyexchange.dto.Item;
import com.xische.currencyexchange.service.CurrencyService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class CurrencyExchangeController {

    private final CurrencyService currencyService;

    public CurrencyExchangeController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @PostMapping("/calculate")
    public CalculationResponse calculate(@RequestBody BillDetails request) {

        double totalAmount = request.getTotalAmount();
        String userType = request.getUserType();
        int customerTenure = request.getCustomerTenure();
        boolean hasGrocery = request.getItems().stream().anyMatch(Item::isGrocery);

        // Use CurrencyService to get conversion rate
        double conversionRate = currencyService.getConversionRate(request.getOriginalCurrency(), request.getTargetCurrency());
        double discountAmount = applyDiscounts(totalAmount, userType, customerTenure, hasGrocery);
        double amountAfterDiscount = totalAmount - discountAmount;
        double netPayableAmount = amountAfterDiscount * conversionRate;

        return new CalculationResponse(netPayableAmount, request.getTargetCurrency());
    }

    private double applyDiscounts(double totalAmount, String userType, int customerTenure, boolean hasGrocery) {
        double discountAmount = 0.0;

        // Apply percentage-based discounts if applicable
        if (!hasGrocery) {
            if ("employee".equalsIgnoreCase(userType)) {
                discountAmount = totalAmount * 0.30; // 30% discount
            } else if ("affiliate".equalsIgnoreCase(userType)) {
                discountAmount = totalAmount * 0.10; // 10% discount
            } else if (customerTenure > 2) {
                discountAmount = totalAmount * 0.05; // 5% discount
            }
        }

        // Apply $5 discount for every $100 on the bill
        discountAmount += Math.floor(totalAmount / 100) * 5;

        return discountAmount;
    }

}
