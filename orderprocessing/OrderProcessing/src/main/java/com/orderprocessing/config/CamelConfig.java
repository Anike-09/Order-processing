package com.orderprocessing.config;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

import com.orderprocessing.entity.Order;

@Component
public class CamelConfig extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        System.out.println("🔄 Camel Route Initializing...");
        
        // File → ActiveMQ Route - Changed delete=false and added move
        from("file:input/orders?noop=false&delay=1000&delete=false&move=./processed")
            .routeId("file-to-activemq")
            .log("🚀 File received: ${header.CamelFileName}")
            .log("📄 File content: ${body}")
            .log("📁 File size: ${header.CamelFileLength} bytes")
            .unmarshal().json(JsonLibrary.Jackson, Order.class)
            .process(exchange -> {
                Order order = exchange.getIn().getBody(Order.class);
                System.out.println("✅ Parsed Order: " + order.getOrderId() + 
                                  " | Customer: " + order.getCustomerId() +
                                  " | Created At: " + order.getCreatedAt());
                validateOrder(order);
            })
            .marshal().json()
            .log("📤 Sending to ActiveMQ queue: ORDER.CREATED.QUEUE")
            .log("📤 Message body: ${body}")
            .to("activemq:queue:ORDER.CREATED.QUEUE")
            .log("✅ Message successfully sent to ActiveMQ")
            .onException(Exception.class)
                .log("❌ Error in file-to-activemq route: ${exception.message}")
                .handled(true);

        // ActiveMQ Consumer Route
        from("activemq:queue:ORDER.CREATED.QUEUE")
            .routeId("activemq-consumer")
            .log("📥 Received message from ActiveMQ: ${body}")
            .unmarshal().json(JsonLibrary.Jackson, Order.class)
            .process(exchange -> {
                Order order = exchange.getIn().getBody(Order.class);
                System.out.println("🎯 ACTIVE MQ RECEIVED:");
                System.out.println("🎯 Order ID: " + order.getOrderId());
                System.out.println("🎯 Customer: " + order.getCustomerId());
                System.out.println("🎯 Product: " + order.getProduct());
                System.out.println("🎯 Amount: " + order.getAmount());
                System.out.println("🎯 Created At: " + order.getCreatedAt());
            })
            .log("✅ Order processing completed");

        System.out.println("✅ Camel Routes configured successfully");
    }

    private void validateOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order is null");
        }
        if (order.getAmount() <= 0) {
            throw new IllegalArgumentException("Invalid Order Amount: " + order.getAmount());
        }
        if (order.getCustomerId() == null || order.getCustomerId().trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID is required");
        }
    }
}