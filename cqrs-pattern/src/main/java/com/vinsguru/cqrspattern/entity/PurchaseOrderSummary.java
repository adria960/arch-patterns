package com.vinsguru.cqrspattern.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class PurchaseOrderSummary {

    @Id
    private String state;
    private Double totalSale;

}
