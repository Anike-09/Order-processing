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
           
            File dir = new File(outputDirectory);
            if (!dir.exists()) {
                boolean created = dir.mkdirs();
                logger.info("Directory created: {} - Success: {}", dir.getAbsolutePath(), created);
            }
            
           
            File processedDir = new File("processed");
            if (!processedDir.exists()) {
                processedDir.mkdirs();
            }
            
            String jsonContent = objectMapper.writeValueAsString(order);
            logger.info("JSON Content: {}", jsonContent);
            
            String fileName = String.format("order-%s.json", order.getOrderId());
            String filePath = outputDirectory + File.separator + fileName;
            
            Files.write(Paths.get(filePath), jsonContent.getBytes());
            
            logger.info("✅ Order written to file: {}", filePath);
            logger.info("✅ File exists: {}", new File(filePath).exists());
            logger.info("✅ File size: {} bytes", new File(filePath).length());
            logger.info("✅ File location: {}", new File(filePath).getAbsolutePath());
            
        } catch (Exception e) {
            logger.error("❌ Failed to write order to file: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to write order to file", e);
        }
    }
}