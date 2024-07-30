package com.javatechie.service;

import com.javatechie.dto.ProductEvent;
import com.javatechie.entity.Product;
import com.javatechie.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProductCommandService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public Product createProduct(ProductEvent productEvent) {
        Product productDO = productRepository.save(productEvent.getProduct());
        // Produce event - on Product creation
        ProductEvent event = new ProductEvent("CreateProduct", productDO);
        kafkaTemplate.send("product-event-topic", event);
        return productDO;
    }

    public Product updateProduct(long id, ProductEvent productEvent) {
        Product existingProduct = productRepository.findById(id).get();

        Product newProduct = productEvent.getProduct();

        existingProduct.setName(newProduct.getName());
        existingProduct.setDescription(newProduct.getDescription());
        existingProduct.setPrice(newProduct.getPrice());
        Product productDO = productRepository.save(existingProduct);

        // Kafka event creation - on product update
        ProductEvent eventProduct = new ProductEvent("UpdateProduct", productDO);
        kafkaTemplate.send("product-event-topic", eventProduct);

        //return successfully updated product
        return productDO;
    }
}
