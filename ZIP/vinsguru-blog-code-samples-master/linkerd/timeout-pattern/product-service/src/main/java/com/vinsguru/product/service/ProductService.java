package com.vinsguru.product.service;

import com.vinsguru.dto.ProductDto;
import com.vinsguru.product.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Map;

@Service
public class ProductService {

    private Map<Integer, Product> map;

    @Autowired
    private RatingServiceClient ratingServiceClient;

    @PostConstruct
    private void init(){
        this.map = Map.of(
                1, Product.of(1, "Blood On The Dance Floor", 12.45),
                2, Product.of(2, "The Eminem Show", 12.12)
        );
    }

    public Mono<ProductDto> getProductDto(int productId){
           return this.ratingServiceClient
                        .getProductRatingDto(productId)
                        .map(productRatingDto -> {
                            Product product = this.map.get(productId);
                            return ProductDto.of(productId, product.getDescription(), product.getPrice(), productRatingDto);
                        });
    }

}
