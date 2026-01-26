package com.orderprocessing.config;



import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.context.annotation.Configuration;

import com.orderprocessing.entity.Order;

@Configuration
public class CamelConfig extends RouteBuilder {
    
    @Override
    public void configure() throws Exception {
        
        // ========== Route 1: File to ActiveMQ ==========
        from("file:input/orders?noop=true&delete=true&delay=5000")
            .routeId("file-to-activemq-route")
            .log("Processing file: ${header.CamelFileName}")
            
            .doTry()
                .unmarshal().json(JsonLibrary.Jackson, Order.class)
                
                .process(exchange -> {
                    Order order = exchange.getIn().getBody(Order.class);
                    System.out.println("Processing file: " + exchange.getIn().getHeader("CamelFileName") + 
                                     " | OrderId: " + order.getOrderId());
                    
                    // Validate
                    validateOrder(order);
                })
                
                .marshal().json()
                .to("activemq:queue:ORDER.CREATED.QUEUE")
                .log("Successfully sent Order to ActiveMQ")
                
            .doCatch(Exception.class)
                .log("Error processing file ${header.CamelFileName}: ${exception.message}")
                .to("file:error/orders?fileName=${header.CamelFileName}")
            .end();
        
        // ========== Route 2: ActiveMQ Consumer ==========
        from("activemq:queue:ORDER.CREATED.QUEUE")
            .routeId("activemq-consumer-route")
            .log("Received message from ORDER.CREATED.QUEUE")
            
            .doTry()
                .unmarshal().json(JsonLibrary.Jackson, Order.class)
                
                .process(exchange -> {
                    Order order = exchange.getIn().getBody(Order.class);
                    
                    // Log as specified in requirements
                    System.out.println("Order processed | OrderId=" + order.getOrderId() + 
                                     " | CustomerId=" + order.getCustomerId() + 
                                     " | Amount=" + order.getAmount());
                    
                    System.out.println("Successfully processed order: " + order.getOrderId());
                })
                
            .doCatch(Exception.class)
                .log("Error processing message from ActiveMQ: ${exception.message}")
            .end();
    }
    
    private void validateOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order is null");
        }
        
        if (order.getOrderId() == null || order.getOrderId().trim().isEmpty()) {
            throw new IllegalArgumentException("Order ID is null or empty");
        }
        
        if (order.getCustomerId() == null || order.getCustomerId().trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID is null or empty");
        }
        
        if (order.getAmount() == null || order.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        
        System.out.println("Validation passed for Order: " + order.getOrderId());
    }
}