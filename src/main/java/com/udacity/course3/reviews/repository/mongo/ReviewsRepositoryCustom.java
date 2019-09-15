package com.udacity.course3.reviews.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewsRepositoryCustom extends MongoRepository<com.udacity.course3.reviews.entity.mongo.Review, String> {

    /**
     * finds all review for a product
     * @param id
     * @return
     */
    @Query("{'product.id': ?0}")
    List<com.udacity.course3.reviews.entity.mongo.Review> findAllByProduct(Integer id);


}
