package com.orderprocessing.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.orderprocessing.dto.OrderRequest;
import com.orderprocessing.dto.OrderResponse;
import com.orderprocessing.entity.Order;
import com.orderprocessing.service.OrderService;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    
    @Autowired
    private OrderService orderService;
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request) {
        logger.info("Creating order for customer: {}", request.getCustomerId());
        
        OrderResponse response = orderService.createOrder(request);
        
        logger.info("Order created successfully: {}", response.getOrderId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    
    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('ADMIN') or @orderSecurityService.canAccessOrder(#orderId)")
    public ResponseEntity<Order> getOrderById(@PathVariable String orderId) {
        logger.info("Fetching order by ID: {}", orderId);
        
        Order order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }
    
    @GetMapping
    public ResponseEntity<List<Order>> getOrders(
            @RequestParam(value = "customerId", required = false) String customerId) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        
        logger.info("User {} (Admin: {}) fetching orders", currentUser, isAdmin);
        
        if (customerId != null && !customerId.trim().isEmpty()) {
            if (!isAdmin && !customerId.equals(currentUser)) {
                logger.warn("User {} attempted to access orders of {}", currentUser, customerId);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            
            logger.info("Fetching orders for customer: {}", customerId);
            List<Order> orders = orderService.getOrdersByCustomerId(customerId);
            return ResponseEntity.ok(orders);
            
        } else {
            if (isAdmin) {
                logger.info("Admin fetching all orders");
                List<Order> orders = orderService.getAllOrders();
                return ResponseEntity.ok(orders);
            } else {
                logger.info("User {} fetching own orders", currentUser);
                List<Order> orders = orderService.getOrdersByCustomerId(currentUser);
                return ResponseEntity.ok(orders);
            }
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Order Processing System is running");
    }
}