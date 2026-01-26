package com.orderprocessing.orderrepository;

import org.springframework.stereotype.Repository;

import com.orderprocessing.entity.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class OrderRepository {

	private final Map<String, Order> orderStore = new ConcurrentHashMap<>();

	public Order save(Order order) {
		orderStore.put(order.getOrderId(), order);
		return order;
	}

	public Order findById(String orderId) {
		return orderStore.get(orderId);
	}

	public List<Order> findByCustomerId(String customerId) {
		return orderStore.values().stream().filter(order -> order.getCustomerId().equals(customerId))
				.collect(Collectors.toList());
	}

	public List<Order> findAll() {
		return new ArrayList<>(orderStore.values());
	}

	public boolean exists(String orderId) {
		return orderStore.containsKey(orderId);
	}

	public long count() {
		return orderStore.size();
	}
}