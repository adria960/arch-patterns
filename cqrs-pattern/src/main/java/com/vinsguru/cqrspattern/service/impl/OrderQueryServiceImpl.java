package com.vinsguru.cqrspattern.service.impl;

import com.vinsguru.cqrspattern.dto.PurchaseOrderSummaryDto;
import com.vinsguru.cqrspattern.entity.PurchaseOrderSummary;
import com.vinsguru.cqrspattern.repository.PurchaseOrderSummaryRepository;
import com.vinsguru.cqrspattern.service.OrderQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderQueryServiceImpl implements OrderQueryService {

    @Autowired
    private PurchaseOrderSummaryRepository purchaseOrderSummaryRepository;

    @Override
    public List<PurchaseOrderSummaryDto> getSaleSummaryGroupByState() {
        log.info("METHOD - getSaleSummaryGroupByState");
        //get all PurchaseOrderSummary, map to Dto, ??? how it is grouped by state????
        return purchaseOrderSummaryRepository.findAll()
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PurchaseOrderSummaryDto getSaleSummaryByState(String state) {
        log.info("state: {}", state); // country
        // find all PurchaseOrderSummary with the same state (country), and map them to Dto
        return purchaseOrderSummaryRepository.findByState(state)
                .map(this::entityToDto)// if value is present
                .orElseGet(() -> new PurchaseOrderSummaryDto(state, 0)); // else returns this
    }

    @Override
    public double getTotalSale() {
        // find all PurchaseOrderSummary, filter only totalSale and make sum of total sale.
        return purchaseOrderSummaryRepository.findAll()
                .stream()
                .mapToDouble(PurchaseOrderSummary::getTotalSale)
                .sum();
    }

    /**
     *
     * @param purchaseOrderSummary
     * @return PurchaseOrderSummaryDto
     */
    private PurchaseOrderSummaryDto entityToDto(PurchaseOrderSummary purchaseOrderSummary) {
        PurchaseOrderSummaryDto dto = new PurchaseOrderSummaryDto();
        dto.setState(purchaseOrderSummary.getState());
        dto.setTotalSale(purchaseOrderSummary.getTotalSale());
        log.info("dt:  {}", dto.toString());
        return dto;
    }
}
