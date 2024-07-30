package com.vinsguru.inventory.service;

import com.vinsguru.dto.InventoryRequestDTO;
import com.vinsguru.dto.InventoryResponseDTO;
import com.vinsguru.enums.InventoryStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class InventoryService {

    private Map<Integer, Integer> productInventoryMap;

    /**
     *  Map for keeping products in the store room or db.
     */
    @PostConstruct
    private void init(){
        this.productInventoryMap = new HashMap<>();
        this.productInventoryMap.put(1, 5);
        this.productInventoryMap.put(2, 5);
        this.productInventoryMap.put(3, 5);
    }

    /**
     * Get product quantity from dbase (here map).
     * If product is available, deduct it, if not return UNAVAILABLE status.
     *
     * @param requestDTO
     * @return InventoryResponseDTO
     */
    public InventoryResponseDTO deductInventory(final InventoryRequestDTO requestDTO){
        int quantity = this.productInventoryMap.getOrDefault(requestDTO.getProductId(), 0);
        log.info("quantity: {}", quantity);

        InventoryResponseDTO responseDTO = new InventoryResponseDTO();
        responseDTO.setOrderId(requestDTO.getOrderId());
        responseDTO.setUserId(requestDTO.getUserId());
        responseDTO.setProductId(requestDTO.getProductId());
        responseDTO.setStatus(InventoryStatus.UNAVAILABLE);

        if(quantity > 0){
            responseDTO.setStatus(InventoryStatus.AVAILABLE);
            this.productInventoryMap.put(requestDTO.getProductId(), quantity - 1);
            log.info("prod inventory map-dbase: {}", this.productInventoryMap.get(requestDTO.getProductId()).toString());
        }
        log.info("responseDTO: {}", responseDTO.toString());
        return responseDTO;
    }

    /**
     * If product exists, raise product quantity in map (or db) for 1.
     *
     * @param requestDTO
     */
    public void addInventory(final InventoryRequestDTO requestDTO){
        this.productInventoryMap
                .computeIfPresent(requestDTO.getProductId(), (k, v) -> v + 1);
    }

}

/*
default V getOrDefault(Object key, V defaultValue)

Parameters: This method accepts two parameters:

key: which is the key of the element whose value has to be obtained.
defaultValue: which is the default value that has to be returned, if no value is mapped with the specified key.
Return Value: This method returns value mapped with the specified key, otherwise default value is returned.
 */