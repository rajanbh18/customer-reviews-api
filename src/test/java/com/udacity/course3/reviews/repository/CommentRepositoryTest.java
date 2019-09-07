package com.udacity.course3.reviews.repository;

import com.udacity.course3.reviews.entity.Comment;
import com.udacity.course3.reviews.entity.Product;
import com.udacity.course3.reviews.entity.Review;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CommentRepositoryTest {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    public void injectedComponentsAreNotNull(){
        Assert.assertNotNull(dataSource);
        Assert.assertNotNull(jdbcTemplate);
        Assert.assertNotNull(entityManager);
        Assert.assertNotNull(testEntityManager);
        Assert.assertNotNull(commentRepository);
    }

    @Test
    public void testFindByReviewId(){
        Product product = getProduct();
        Review review = getReview();
        review.setProduct(product);
        Comment comment = getComment();
        comment.setReview(review);

        entityManager.persist(product);
        entityManager.persist(review);
        entityManager.persist(comment);

        Optional<Comment> actualComment = commentRepository.findById(review.getId());
        Assert.assertNotNull(actualComment.get());
        Assert.assertEquals(comment.getCommentText(), actualComment.get().getCommentText());
    }

    @Test
    public void testFindAllByReview(){
        Product product = getProduct();
        Review review = getReview();
        review.setProduct(product);
        Comment comment = getComment();
        comment.setReview(review);

        entityManager.persist(product);
        entityManager.persist(review);
        entityManager.persist(comment);

        List<Comment> actualComment = commentRepository.findAllByReview(review);
        Assert.assertNotNull(actualComment);
        Assert.assertEquals(comment.getCommentText(), actualComment.get(0).getCommentText());
    }

    private Comment getComment() {
        Comment comment = new Comment();
        comment.setCommentText("this is new comment");
        comment.setTitle("new comment");
        return comment;
    }

    private Product getProduct() {
        Product product = new Product();
        product.setName("Mobile");
        product.setDescription("Apple mobile");
        return product;
    }

    private Review getReview() {
        Review review = new Review();
        review.setTitle("Product Review");
        review.setReviewText("product is good");
        return review;
    }

}
