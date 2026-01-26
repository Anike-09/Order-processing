package com.orderprocessing.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.orderprocessing.entity.Order;
import com.orderprocessing.orderrepository.OrderRepository;

@Service
public class OrderSecurityService {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderSecurityService.class);
    
    @Autowired
    private OrderRepository orderRepository;
    
    public boolean canAccessOrder(String orderId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication.getName();
        
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        
        if (isAdmin) {
            logger.info("Admin user {} accessing order {}", currentUser, orderId);
            return true;
        }
        
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            logger.warn("Order {} not found", orderId);
            return false;
        }
        
        boolean canAccess = order.getCustomerId().equals(currentUser);
        if (canAccess) {
            logger.info("User {} authorized to access own order {}", currentUser, orderId);
        } else {
            logger.warn("User {} attempted to access order {} owned by {}", 
                    currentUser, orderId, order.getCustomerId());
        }
        
        return canAccess;
    }
}