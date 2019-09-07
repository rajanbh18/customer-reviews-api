package com.udacity.course3.reviews.repository;

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
public class ReviewRepositoryTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ReviewRepository reviewRepository;


    @Test
    public void injectedComponentsAreNotNull(){
        Assert.assertNotNull(dataSource);
        Assert.assertNotNull(jdbcTemplate);
        Assert.assertNotNull(entityManager);
        Assert.assertNotNull(testEntityManager);
        Assert.assertNotNull(reviewRepository);
    }

    @Test
    public void testFindAllReviewByProductId(){
        Product product = getProduct();
        Review review = getReview();
        review.setProduct(product);

        Review badReview = new Review();
        badReview.setProduct(product);
        badReview.setTitle("review 1");
        badReview.setReviewText("product is not good");

        entityManager.persist(product);
        entityManager.persist(review);

        reviewRepository.save(badReview);
        List<Review> responseReview = reviewRepository.findAllReviewByProduct(product);
        Assert.assertNotNull(responseReview);
        Assert.assertEquals(review.getReviewText(), responseReview.get(0).getReviewText());
        Assert.assertEquals(badReview.getReviewText(), responseReview.get(1).getReviewText());

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
