package com.vinsguru.service;

import com.vinsguru.rating.client.dto.Rating;
import com.vinsguru.rating.client.dto.RatingRequest;
import com.vinsguru.rating.client.dto.RatingResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RatingService {

    private Map<Integer, List<ProductRating>> mapProdRating;

    @PostConstruct
    public void init(){
        this.mapProdRating = Map.of(
                1, new ArrayList<>(),
                2, new ArrayList<>()
        );
        this.mapProdRating.get(1).add(new ProductRating(1, Rating.FOUR));
        this.mapProdRating.get(1).add(new ProductRating(2, Rating.FOUR));
        this.mapProdRating.get(1).add(new ProductRating(3, Rating.FIVE));

        this.mapProdRating.get(2).add(new ProductRating(1, Rating.THREE));
        this.mapProdRating.get(2).add(new ProductRating(2, Rating.TWO));
        this.mapProdRating.get(2).add(new ProductRating(3, Rating.FIVE));

    }

    @Data
    @ToString
    @AllArgsConstructor
    private static class ProductRating {
        private int userId;
        private Rating rating;
    }

    /**
     *
     * @param ratingRequest
     * @return RatingResponse
     */
    public RatingResponse rate(RatingRequest ratingRequest){

        List<ProductRating> productRatings = this.mapProdRating.get(ratingRequest.getProductId());

        System.out.println("*service/RatingService - rate method");
        System.out.println("*RatingRequest: " + ratingRequest.toString());

        productRatings.add(new ProductRating(ratingRequest.getUserId(), ratingRequest.getRating()));
        System.out.println("*ProductRating: " + productRatings.toString());

        RatingResponse ratingResponse = new RatingResponse();
        ratingResponse.setProductId(ratingRequest.getProductId());

        double avgRating = productRatings
                .stream()
                .map(ProductRating::getRating)
                .mapToInt(Rating::ordinal)
                .map(rating -> rating + 1)
                .average()
                .orElse(0);
        ratingResponse.setAvgRating(avgRating);
        System.out.println("*service/RatingService - ratingResponse:" + ratingResponse.toString());
        return ratingResponse;
    }

}
