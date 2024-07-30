package com.vinsguru.userservice.controller;

import com.vinsguru.rating.client.dto.RatingResponse;
import com.vinsguru.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user-service")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}/{productId}/rate/{rating}")
    public Mono<RatingResponse> rate(@PathVariable int userId,
                                     @PathVariable int productId,
                                     @PathVariable int rating){
        System.out.println("UserController rate - Get ");
        return this.userService.rateProduct(userId, productId, rating);
    }

     /*
     1. GET
     localhost:8081/user-service/1/1/rate/3
    RESPONSE
    {
    "productId": 1,
    "avgRating": 4.0
    }

    Goes to client/rating-service (via WebClient)
     @Override
        public Mono<RatingResponse> rate(RatingRequest ratingRequest) {
            this.validate(ratingRequest);
            return webClient
                    .post()
                    .uri("/rate")
                    .body(BodyInserters.fromValue(ratingRequest))
                    .retrieve()
                    .bodyToMono(RatingResponse.class);
        }
        Creates POST with BODY - {1, 1, 3}
    [RatingService.ProductRating(userId=1, rating=FOUR), RatingService.ProductRating(userId=2, rating=FOUR), RatingService.ProductRating(userId=3, rating=FIVE)]


2. GET localhost:8081/user-service/1/1/rate/1
RESPONSE
{
    "productId": 1,
    "avgRating": 3.5
}

RAtingServiceApplication log:

in RatingController.rate method
service/RatingService - rate method
RatingRequest: RatingRequest(userId=1, productId=1, rating=ONE, comment=null)
ProductRating: [RatingService.ProductRating(userId=1, rating=FOUR), RatingService.ProductRating(userId=2, rating=FOUR), RatingService.ProductRating(userId=3, rating=FIVE)]
service/RatingService - ratingResponse:RatingResponse(productId=1, avgRating=3.5)

        return Mono.fromSupplier(() -> this.ratingService.rate(ratingRequest)); ==>

 */
}
