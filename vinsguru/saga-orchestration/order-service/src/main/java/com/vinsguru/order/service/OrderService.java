package com.vinsguru.order.service;

import com.vinsguru.dto.OrchestratorRequestDTO;
import com.vinsguru.dto.OrderRequestDTO;
import com.vinsguru.dto.OrderResponseDTO;
import com.vinsguru.enums.OrderStatus;
import com.vinsguru.order.entity.PurchaseOrder;
import com.vinsguru.order.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.Map;

@Service
public class OrderService {

    // product-price map
    private static final Map<Integer, Double> PRODUCTS_PRICES =  Map.of(
            1, 100d,
            2, 200d,
            3, 300d
    );

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private Sinks.Many<OrchestratorRequestDTO> sink;

    public Mono<PurchaseOrder> createOrder(OrderRequestDTO orderRequestDTO){
        return this.purchaseOrderRepository.save(this.dtoToEntity(orderRequestDTO))
                .doOnNext(e -> orderRequestDTO.setOrderId(e.getId()))
                .doOnNext(e -> this.emitEvent(orderRequestDTO));
    }

    /**
     *
     * @return Flux<OrderResponseDTO>
     */
    public Flux<OrderResponseDTO> getAll() {
        return this.purchaseOrderRepository.findAll()
                .map(this::entityToDto);
    }

    /**
     *
     * @param orderRequestDTO
     */
    private void emitEvent(OrderRequestDTO orderRequestDTO){
        this.sink.tryEmitNext(this.getOrchestratorRequestDTO(orderRequestDTO));
    }

    /**
     *
     * @param dto
     * @return PurchaseOrder
     */
    private PurchaseOrder dtoToEntity(final OrderRequestDTO dto){
        PurchaseOrder purchaseOrder = new PurchaseOrder();

        purchaseOrder.setId(dto.getOrderId());
        purchaseOrder.setProductId(dto.getProductId());
        purchaseOrder.setUserId(dto.getUserId());
        purchaseOrder.setStatus(OrderStatus.ORDER_CREATED);
        purchaseOrder.setPrice(PRODUCTS_PRICES.get(purchaseOrder.getProductId()));

        return purchaseOrder;
    }

    /**
     *
     * @param purchaseOrder
     * @return OrderResponseDTO
     */
    private OrderResponseDTO entityToDto(final PurchaseOrder purchaseOrder){
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setOrderId(purchaseOrder.getId());
        dto.setProductId(purchaseOrder.getProductId());
        dto.setUserId(purchaseOrder.getUserId());
        dto.setStatus(purchaseOrder.getStatus());
        dto.setAmount(purchaseOrder.getPrice());
        return dto;
    }

    /**
     *
     * @param orderRequestDTO
     * @return OrchestratorRequestDTO
     */
    public OrchestratorRequestDTO getOrchestratorRequestDTO(OrderRequestDTO orderRequestDTO){
        OrchestratorRequestDTO requestDTO = new OrchestratorRequestDTO();
        requestDTO.setUserId(orderRequestDTO.getUserId());
        requestDTO.setAmount(PRODUCTS_PRICES.get(orderRequestDTO.getProductId()));
        requestDTO.setOrderId(orderRequestDTO.getOrderId());
        requestDTO.setProductId(orderRequestDTO.getProductId());
        return requestDTO;
    }

}
