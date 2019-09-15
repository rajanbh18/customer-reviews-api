package com.udacity.course3.reviews.controller;

import com.udacity.course3.reviews.entity.Comment;
import com.udacity.course3.reviews.entity.Review;
import com.udacity.course3.reviews.entity.mongo.ReviewComment;
import com.udacity.course3.reviews.exception.CommentsNotFoundException;
import com.udacity.course3.reviews.exception.ReviewNotFoundException;
import com.udacity.course3.reviews.repository.CommentRepository;
import com.udacity.course3.reviews.repository.ReviewRepository;
import com.udacity.course3.reviews.repository.mongo.ReviewsRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Spring REST controller for working with comment entity.
 */
@RestController
@RequestMapping("/comments")
public class CommentsController {

    private final ReviewRepository reviewRepository;
    private final CommentRepository commentRepository;
    private ReviewsRepositoryCustom reviewsRepositoryCustom;

    @Autowired
    public CommentsController(ReviewRepository reviewRepository, CommentRepository commentRepository, ReviewsRepositoryCustom reviewsRepositoryCustom) {
        this.reviewRepository = reviewRepository;
        this.commentRepository = commentRepository;
        this.reviewsRepositoryCustom = reviewsRepositoryCustom;
    }

    /**
     * Creates a comment for a review.
     *
     * @param reviewId The id of the review.
     */
    @RequestMapping(value = "/reviews/{reviewId}", method = RequestMethod.POST)
    public ResponseEntity<?> createCommentForReview(@PathVariable("reviewId") Integer reviewId, @RequestBody @Valid Comment comment) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        if (optionalReview.isPresent()){
            comment.setReview(optionalReview.get());
            Comment savedComment = commentRepository.save(comment);

            Optional<com.udacity.course3.reviews.entity.mongo.Review> review = reviewsRepositoryCustom.findById(reviewId.toString());
            if (review.isPresent()){
                review.get().getCommentList().add(getComment(savedComment));
                reviewsRepositoryCustom.save(review.get());
            }
            return ResponseEntity.ok(savedComment);
        }else {
            throw new ReviewNotFoundException();
        }
    }

    private ReviewComment getComment(Comment comment){
        ReviewComment reviewComment = new ReviewComment();
        reviewComment.setId(comment.getId().toString());
        reviewComment.setTitle(comment.getTitle());
        reviewComment.setCommentText(comment.getCommentText());
        return reviewComment;
    }
    /**
     * List comments for a review.
     *
     * @param reviewId The id of the review.
     */
    @RequestMapping(value = "/reviews/{reviewId}", method = RequestMethod.GET)
    public List<?> listCommentsForReview(@PathVariable("reviewId") Integer reviewId) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        if (!optionalReview.isPresent()){
            throw new ReviewNotFoundException();
        }
        List<Comment> commentList = commentRepository.findAllByReview(new Review(reviewId));
        if (commentList.isEmpty()){
            throw new CommentsNotFoundException();
        }
        return commentList;
    }
}