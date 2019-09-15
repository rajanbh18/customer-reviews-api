package com.udacity.course3.reviews.repository.mongo;

import com.mongodb.MongoClient;
import com.udacity.course3.reviews.entity.Product;
import com.udacity.course3.reviews.entity.mongo.Review;
import com.udacity.course3.reviews.repository.mongo.ReviewsRepositoryCustom;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@DataMongoTest
@RunWith(SpringRunner.class)

public class ReviewRepositoryCustomTest {


    private MongodExecutable mongodExecutable;

    private MongoTemplate mongoTemplate;

    @Autowired
    private ReviewsRepositoryCustom reviewsRepositoryCustom;

    @Before
    public void setup() throws Exception {
        String ip = "localhost";
        int port = 27017;

        IMongodConfig mongodConfig = new MongodConfigBuilder().version(Version.Main.PRODUCTION)
                .net(new Net(ip, port, Network.localhostIsIPv6()))
                .build();

        MongodStarter starter = MongodStarter.getDefaultInstance();
        mongodExecutable = starter.prepare(mongodConfig);
        mongodExecutable.start();
        mongoTemplate = new MongoTemplate(new MongoClient(ip, port), "test");
    }

    @After
    public void clean() {
        mongodExecutable.stop();
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

        reviewsRepositoryCustom.save(review);
        reviewsRepositoryCustom.save(badReview);

        List<Review> responseReview = reviewsRepositoryCustom.findAllByProduct(product.getId());
        Assert.assertNotNull(responseReview);
        Assert.assertEquals(review.getReviewText(), responseReview.get(0).getReviewText());
        Assert.assertEquals(2, responseReview.size());
    }

    private Product getProduct() {
        Product product = new Product();
        product.setName("Mobile");
        product.setDescription("Apple mobile");
        return product;
    }

    private Review getReview() {
        com.udacity.course3.reviews.entity.mongo.Review review = new com.udacity.course3.reviews.entity.mongo.Review();
        review.setTitle("Product Review");
        review.setReviewText("product is good");
        return review;
    }
}
