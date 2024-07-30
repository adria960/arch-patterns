package com.vinsguru.cqrspattern.service.impl;

import com.vinsguru.cqrspattern.entity.Product;
import com.vinsguru.cqrspattern.entity.PurchaseOrder;
import com.vinsguru.cqrspattern.entity.User;
import com.vinsguru.cqrspattern.repository.ProductRepository;
import com.vinsguru.cqrspattern.repository.PurchaseOrderRepository;
import com.vinsguru.cqrspattern.repository.UserRepository;
import com.vinsguru.cqrspattern.service.OrderCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class OrderCommandServiceImpl implements OrderCommandService {

    private static final long ORDER_CANCELLATION_WINDOW = 30;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    private List<User> users;
    private List<Product> products;

    @PostConstruct
    private void init() {
        this.users = this.userRepository.findAll();
        this.products = this.productRepository.findAll();
    }

    @Override
    public void createOrder(int userIndex, int productIndex) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setProductId(products.get(productIndex).getId()); // get Product from Product List by index
        purchaseOrder.setUserId(users.get(userIndex).getId()); // get User from User List by index
        purchaseOrder.setOrderDate(new Date());
        purchaseOrderRepository.save(purchaseOrder);
    }

    // Order can be canceled if purchase date <= 30
    @Override
    public void cancelOrder(long orderId) {
        purchaseOrderRepository.findById(orderId)
                .ifPresent(purchaseOrder -> {
                    LocalDateTime orderDate = LocalDateTime.ofInstant(purchaseOrder.getOrderDate().toInstant(), ZoneId.systemDefault());
                    if (Duration.between(orderDate, LocalDateTime.now()).toDays() <= ORDER_CANCELLATION_WINDOW) {
                        this.purchaseOrderRepository.deleteById(orderId);
                        log.info("orderId: {}", orderId);
                        //additional logic to issue refund
                    }
                });
    }
}
