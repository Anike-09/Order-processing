package com.orderprocessing.entity;



import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

public class Order {
    
    @JsonProperty("orderId")
    private String orderId;
    
    @JsonProperty("customerId")
    private String customerId;
    
    @JsonProperty("product")
    private String product;
    
    @JsonProperty("amount")
    private Double amount;
    
    @JsonProperty("createdAt")
 //   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createdAt;
    
    @JsonProperty("status")
    private String status;
    
    public Order() {
        this.orderId = UUID.randomUUID().toString();
      //  this.createdAt = LocalDateTime.now();
        this.createdAt = java.time.LocalDateTime.now().toString();
        this.status = "CREATED";
    }
    
    public Order(String customerId, String product, Double amount) {
        this();
        this.customerId = customerId;
        this.product = product;
        this.amount = amount;
    }
    
    // Getters and Setters
    public String getOrderId() { 
        return orderId; 
    }
    
    public void setOrderId(String orderId) { 
        this.orderId = orderId; 
    }
    
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
    
    public String getCreatedAt() { 
        return createdAt; 
    }
    
    public void setCreatedAt(String createdAt) { 
        this.createdAt = createdAt; 
    }
    
    public String getStatus() { 
        return status; 
    }
    
    public void setStatus(String status) { 
        this.status = status; 
    }
    
    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", product='" + product + '\'' +
                ", amount=" + amount +
                ", createdAt=" + createdAt +
                ", status='" + status + '\'' +
                '}';
    }
}