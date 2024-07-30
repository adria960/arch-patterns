CORRECT COMMENTS:

1. PRODUCT = 1
GET localhost:8081/user-service/1/1/rate/2
RESPONSE
{
    "productId": 1,
    "avgRating": 3.75
}

Comments - user-service:
--------------------------
UserController rate - Get
UserService - rateProduct
UserService - rateProduct ratingRequest: RatingRequest(userId=1, productId=1, rating=TWO, comment=null)
Before call RatingService from rating-client dependency!
validate => RatingRequest: RatingRequest(userId=1, productId=1, rating=TWO, comment=null)
client/RatingService - before webClient POST

Comments - rating-service:
--------------------------
in RatingController.rate method
*service/RatingService - rate method
*RatingRequest: RatingRequest(userId=1, productId=1, rating=TWO, comment=null)
*ProductRating: [RatingService.ProductRating(userId=1, rating=FOUR), RatingService.ProductRating(userId=2, rating=FOUR), RatingService.ProductRating(userId=3, rating=FIVE), RatingService.ProductRating(userId=1, rating=TWO)]
*service/RatingService - ratingResponse:RatingResponse(productId=1, avgRating=3.75)

(4+4+5+2)/4=3.75


2. PRODUCT = 2
GET localhost:8081/user-service/1/1/rate/2
RESPONSE
{
    "productId": 2,
    "avgRating": 3.0
}

Comments - user-service:
--------------------------
UserService - rateProduct
UserService - rateProduct ratingRequest: RatingRequest(userId=1, productId=2, rating=TWO, comment=null)
Before call RatingService from rating-client dependency!
validate => RatingRequest: RatingRequest(userId=1, productId=2, rating=TWO, comment=null)
client/RatingService - before webClient POST

Comments in rating-service:
-------------------------------
in RatingController.rate method
*service/RatingService - rate method
*RatingRequest: RatingRequest(userId=1, productId=2, rating=TWO, comment=null)
*ProductRating: [RatingService.ProductRating(userId=1, rating=THREE), RatingService.ProductRating(userId=2, rating=TWO), RatingService.ProductRating(userId=3, rating=FIVE), RatingService.ProductRating(userId=1, rating=TWO)]
*service/RatingService - ratingResponse:RatingResponse(productId=2, avgRating=3.0)

(3+2+5+2)/4=3
---------------------------------------------------------------------------------------
OLDER tests:
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

2.
Product Id = 2

GET
localhost:8081/user-service/1/2/rate/1
RESPONSE
{
    "productId": 2,
    "avgRating": 2.75
}

in RatingController.rate method
service/RatingService - rate method
RatingRequest: RatingRequest(userId=1, productId=2, rating=ONE, comment=null)
ProductRating: [RatingService.ProductRating(userId=1, rating=THREE), RatingService.ProductRating(userId=2, rating=TWO), RatingService.ProductRating(userId=3, rating=FIVE)]
service/RatingService - ratingResponse:RatingResponse(productId=2, avgRating=2.75)


3. TEST
user-service
GET - localhost:8081/user-service/1/2/rate/1
RESPONSE
{
    "productId": 2,
    "avgRating": 2.75
}

--->
UserController rate - Get
UserService - rateProduct
UserService - rateProduct ratingRequest: RatingRequest(userId=1, productId=2, rating=ONE, comment=null)
Before call RatingService from rating-client dependency!
--> client/RatingService
validate => RatingRequest: RatingRequest(userId=1, productId=2, rating=ONE, comment=null)
client/RatingService - before webClient POST --->

--->
in RatingController.rate method
*service/RatingService - rate method
*RatingRequest: RatingRequest(userId=1, productId=2, rating=ONE, comment=null)
*ProductRating: [RatingService.ProductRating(userId=1, rating=THREE), RatingService.ProductRating(userId=2, rating=TWO), RatingService.ProductRating(userId=3, rating=FIVE)]
*service/RatingService - ratingResponse:RatingResponse(productId=2, avgRating=2.75)
My comment: (1+3+2+5)/4 = 2.75