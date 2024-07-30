package com.vinsguru.cqrspattern.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderSummaryDto {

    private String state;
    private double totalSale;

}