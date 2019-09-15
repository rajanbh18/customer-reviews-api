package com.udacity.course3.reviews.entity.mongo;

import com.udacity.course3.reviews.entity.Product;
import org.springframework.data.mongodb.core.mapping.Document;


import javax.validation.constraints.NotBlank;
import java.util.List;

@Document("review")
public class Review {

    private String id;

    @NotBlank(message = "Title can not be null or blank.")
    private String title;

    private String reviewText;

    private boolean recommended;

    private Product product;

    List<ReviewComment> commentList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public boolean isRecommended() {
        return recommended;
    }

    public void setRecommended(boolean recommended) {
        this.recommended = recommended;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<ReviewComment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<ReviewComment> commentList) {
        this.commentList = commentList;
    }
}
