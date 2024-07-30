package com.vinsguru.controller;

import com.vinsguru.rating.client.dto.Rating;
import com.vinsguru.rating.client.dto.RatingRequest;
import com.vinsguru.rating.client.dto.RatingResponse;
import com.vinsguru.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/rating-service")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @PostMapping("/rate")
    public Mono<RatingResponse> rate(@RequestBody RatingRequest ratingRequest){
        System.out.println("in RatingController.rate method");
        return Mono.fromSupplier(() -> this.ratingService.rate(ratingRequest));
    }

}
//RatingRequest
//    private Integer userId;
//    private Integer productId;
//    private Rating rating;
//    private String comment;

//    "userId":  11,
//    "productId": 111,
//    "rating": "ONE",
//    "comment: ""