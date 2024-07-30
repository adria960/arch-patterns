package com.vinsguru.cqrspattern.controller.command;

import com.vinsguru.cqrspattern.dto.OrderCommandDto;
import com.vinsguru.cqrspattern.service.OrderCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;

/**
 *  We can even control if the app should work in READ mode or WRITE mode based on a property value.
 *  Done in ConditionalOnProperty with
 *  app.write.enabled", havingValue = "true"
 */

@Slf4j
@RestController
@RequestMapping("po")
@ConditionalOnProperty(name = "app.write.enabled", havingValue = "true")
public class OrderCommandController {

    @Autowired
    private OrderCommandService orderCommandService;

    @PostMapping("/sale")
    public void placeOrder(@RequestBody OrderCommandDto dto) {
        orderCommandService.createOrder(dto.getUserIndex(), dto.getProductIndex());
    }

    @PutMapping("/cancel-order/{orderId}")
    public void cancelOrder(@PathVariable long orderId) {
        log.info("orderId: {}", orderId);
        orderCommandService.cancelOrder(orderId);
    }
}

/*
    So based on a property, we change if the app is going to behave like a read-only node or write-only node.
    It gives us the ability to run multiple instances of an app with different modes.
    I can have 1 instance of my app which does the writing while
    I can have multiple instances of my app just for serving the read requests.
    They can be scaled in-out independently.
    We can place them behind a load balancer / proxy like nginx – so that READ / WRITE requests could be forwarded to
    appropriate instances using path based routing or some other mechanism.
 */