package com.orderprocessing.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class OrderRequest {
    
    @NotBlank(message = "customerId is mandatory")
    private String customerId;
    
    @NotBlank(message = "product is mandatory")
    private String product;
    
    @Min(value = 1, message = "amount must be greater than 0")
    private Double amount;
    
    public String getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    public String getProduct() {
        return product;
    }
    
    public void setProduct(String product) {
        this.product = product;
    }
    
    public Double getAmount() {
        return amount;
    }
    
    public void setAmount(Double amount) {
        this.amount = amount;
    }
}