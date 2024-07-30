package com.vinsguru.cqrspattern.dto;

import lombok.Data;

@Data
public class OrderCommandDto {

    private int userIndex;
    private int productIndex;
}
