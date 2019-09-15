package com.udacity.course3.reviews.controller;

import com.udacity.course3.reviews.entity.Product;
import com.udacity.course3.reviews.entity.Review;
import com.udacity.course3.reviews.exception.ProductNotFoundException;
import com.udacity.course3.reviews.exception.ReviewNotFoundException;
import com.udacity.course3.reviews.repository.ProductRepository;
import com.udacity.course3.reviews.repository.ReviewRepository;
import com.udacity.course3.reviews.repository.mongo.ReviewsRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Spring REST controller for working with review entity.
 */
@RestController
public class ReviewsController {

    private ProductRepository productRepository;
    private ReviewRepository reviewRepository;
    private ReviewsRepositoryCustom reviewsRepositoryCustom;

    @Autowired
    public ReviewsController(ProductRepository productRepository, ReviewRepository reviewRepository, ReviewsRepositoryCustom reviewsRepositoryCustom) {
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
        this.reviewsRepositoryCustom = reviewsRepositoryCustom;
    }

    /**
     * Creates a review for a product.
     *
     * @param productId The id of the product.
     * @return The created review or 404 if product id is not found.
     */
    @RequestMapping(value = "/reviews/products/{productId}", method = RequestMethod.POST)
    public ResponseEntity<?> createReviewForProduct(@PathVariable("productId") Integer productId, @RequestBody @Valid Review review) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()){
            review.setProduct(optionalProduct.get());
            reviewsRepositoryCustom.save(getReview(review, optionalProduct));
            return ResponseEntity.ok(reviewRepository.save(review));
        } else {
            throw new ProductNotFoundException();
        }
    }

    private com.udacity.course3.reviews.entity.mongo.Review getReview(Review review, Optional<Product> optionalProduct) {

        com.udacity.course3.reviews.entity.mongo.Review newReview = new com.udacity.course3.reviews.entity.mongo.Review();
        newReview.setProduct(optionalProduct.get());
        newReview.setTitle(review.getTitle());
        newReview.setReviewText(review.getReviewText());
        newReview.setRecommended(review.isRecommended());
        return newReview;
    }

    /**
     * Lists reviews by product.
     *
     * @param productId The id of the product.
     * @return The list of reviews.
     */
    @RequestMapping(value = "/reviews/products/{productId}", method = RequestMethod.GET)
    public ResponseEntity<List<?>> listReviewsForProduct(@PathVariable("productId") Integer productId) {
        Optional<Product> product = productRepository.findById(productId);
        List<com.udacity.course3.reviews.entity.mongo.Review> reviewList = new ArrayList<>();
        if (product.isPresent()){
            reviewList = reviewsRepositoryCustom.findAllByProduct(productId);
        } else {
            throw new ProductNotFoundException();
        }

        if (reviewList.isEmpty()){
            throw new ReviewNotFoundException();
        }
        return ResponseEntity.ok(reviewList);
    }
}