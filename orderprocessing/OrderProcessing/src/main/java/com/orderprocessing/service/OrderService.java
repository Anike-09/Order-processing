package com.orderprocessing.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.orderprocessing.dto.OrderRequest;
import com.orderprocessing.dto.OrderResponse;
import com.orderprocessing.ecxception.OrderNotFoundException;
import com.orderprocessing.entity.Order;
import com.orderprocessing.orderrepository.OrderRepository;

@Service
public class OrderService {

	private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private FileService fileService;

	public OrderResponse createOrder(OrderRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		logger.info("Creating order for customer: {} by user: {}", request.getCustomerId(), auth.getName());

		Order order = new Order(request.getCustomerId(), request.getProduct(), request.getAmount());

		orderRepository.save(order);
		logger.info("Order created successfully. OrderId: {}", order.getOrderId());

		try {
			fileService.writeOrderToFile(order);
			logger.info("Order JSON file created: order-{}.json", order.getOrderId());
		} catch (Exception e) {
			logger.error("Failed to write order to file, but order is saved: {}", e.getMessage());
		}

		return new OrderResponse(order.getOrderId(), order.getStatus());
	}

	public Order getOrderById(String orderId) {
		logger.info("Fetching order by ID:", orderId);
		Order order = orderRepository.findById(orderId);

		if (order == null) {
			logger.warn("Order not found: {}", orderId);
			throw new OrderNotFoundException("Order not found with id: " + orderId);
		}

		return order;
	}

	public List<Order> getOrdersByCustomerId(String customerId) {
		logger.info("Fetching orders for customer", customerId);
		return orderRepository.findByCustomerId(customerId);
	}

	public List<Order> getAllOrders() {
		logger.info("Fetching all orders");
		return orderRepository.findAll();
		
	}
	// just checking
}