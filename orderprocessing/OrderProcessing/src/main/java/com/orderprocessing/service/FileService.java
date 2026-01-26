package com.orderprocessing.service;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.orderprocessing.entity.Order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FileService {
    
    private static final Logger logger = LoggerFactory.getLogger(FileService.class);
    
    @Value("${file.output.directory:input/orders}")
    private String outputDirectory;
    
    private final ObjectMapper objectMapper;
    
    public FileService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }
    
    public void writeOrderToFile(Order order) {
        try {
            Files.createDirectories(Paths.get(outputDirectory));
            
            String jsonContent = objectMapper.writeValueAsString(order);
            String fileName = String.format("order-%s.json", order.getOrderId());
            String filePath = outputDirectory + File.separator + fileName;
            
            Files.write(Paths.get(filePath), jsonContent.getBytes());
            
            logger.info("Order written to file: {}", filePath);
        } catch (Exception e) {
            logger.error("Failed to write order to file: {}", e.getMessage());
            throw new RuntimeException("Failed to write order to file", e);
        }
    }
}