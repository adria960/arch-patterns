package com.javatechie.service;

import com.javatechie.dto.ProductEvent;
import com.javatechie.entity.Product;
import com.javatechie.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductQueryService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @KafkaListener(topics = "product-event-topic", groupId = "product-event-group")
    public void processProductEvents(ProductEvent productEvent) {
        Product product = productEvent.getProduct();

        if (productEvent.getEventType().equals("CreateProduct")) {
            productRepository.save(product);
        }

        if (productEvent.getEventType().equals("UpdateProduct")) {
            Product existingproduct = productRepository.findById(product.getId()).get();
            existingproduct.setName(product.getName());
            existingproduct.setDescription(product.getDescription());
            existingproduct.setPrice(product.getPrice());
            productRepository.save(existingproduct);
        }
    }

}
