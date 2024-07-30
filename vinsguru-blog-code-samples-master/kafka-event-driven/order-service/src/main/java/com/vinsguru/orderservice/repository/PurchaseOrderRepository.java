package com.vinsguru.orderservice.repository;

import com.vinsguru.orderservice.entity.PurchaseOrder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderRepository extends MongoRepository<PurchaseOrder, String> {

    @Query("{ 'user.id': ?0 }")
    List<PurchaseOrder> findByUserId(long userId); // ?0 is userId value from this method

}

/*
@Query("{ 'name' : ?0 }")
List<User> findUsersByName(String name);

//This method should return users by name.
//The placeholder ?0 references the first parameter of the method.

List<User> users = userRepository.findUsersByName("Eric");

 */

// findIterable = collection.find(eq("status", "D"));
// SELECT * FROM inventory WHERE status = "D"

/*
Query query = new Query();
query.addCriteria(Criteria.where("name").is("Eric"));
List<User> users = mongoTemplate.find(query, User.class);
*/

