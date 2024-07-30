package com.vinsguru.orderservice.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class PurchaseOrder {

    @Id
    private String id;
    private User user;
    private Product product;
    private double price;

}
