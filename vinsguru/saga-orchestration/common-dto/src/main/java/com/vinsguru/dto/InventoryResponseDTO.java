package com.vinsguru.dto;

import com.vinsguru.enums.InventoryStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class InventoryResponseDTO {

    private Integer userId;
    private Integer productId;
    private UUID orderId;
    private InventoryStatus status;

}
