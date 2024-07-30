package com.vinsguru.payment.service;

import com.vinsguru.dto.PaymentRequestDTO;
import com.vinsguru.dto.PaymentResponseDTO;
import com.vinsguru.enums.PaymentStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class PaymentService {

    private Map<Integer, Double> userBalanceMap;

    /**
     * Map, here, or db, keeps users balances.
     */
    @PostConstruct
    private void init() {
        this.userBalanceMap = new HashMap<>();
        this.userBalanceMap.put(1, 1000d);
        this.userBalanceMap.put(2, 1000d);
        this.userBalanceMap.put(3, 1000d);
    }

    /**
     * DEBIT
     * Check user balance, if ok, deduct it, else, set status PAYMENT_REJECTED.
     *
     * @param requestDTO
     * @return PaymentResponseDTO
     */
    public PaymentResponseDTO debit(final PaymentRequestDTO requestDTO) {
        double balance = this.userBalanceMap.getOrDefault(requestDTO.getUserId(), 0d);
        log.info("balance: {}", balance);

        PaymentResponseDTO responseDTO = new PaymentResponseDTO();
        responseDTO.setAmount(requestDTO.getAmount());
        responseDTO.setUserId(requestDTO.getUserId());
        responseDTO.setOrderId(requestDTO.getOrderId());
        responseDTO.setStatus(PaymentStatus.PAYMENT_REJECTED);

        if (balance >= requestDTO.getAmount()) {
            responseDTO.setStatus(PaymentStatus.PAYMENT_APPROVED);
            this.userBalanceMap.put(requestDTO.getUserId(), balance - requestDTO.getAmount());
            log.info("user balance map-dbase: {}", this.userBalanceMap.get(requestDTO.getUserId()).toString());
        }
        log.info("responseDTO: {}", responseDTO.toString());
        return responseDTO;
    }

    /**
     * CREDIT
     *
     * @param requestDTO
     */
    public void credit(final PaymentRequestDTO requestDTO) {
        this.userBalanceMap.computeIfPresent(requestDTO.getUserId(), (k, v) -> v + requestDTO.getAmount());
        log.info("user balance map-dbase: {}", this.userBalanceMap.get(requestDTO.getUserId()).toString());
    }

}

/*
    default V getOrDefault(Object key, V defaultValue)

    Parameters: This method accepts two parameters:

    key: which is the key of the element whose value has to be obtained.
    defaultValue: which is the default value that has to be returned, if no value is mapped with the specified key.
    Return Value: This method returns value mapped with the specified key, otherwise default value is returned.
 */