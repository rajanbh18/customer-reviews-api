package com.udacity.course3.reviews.repository;

import com.udacity.course3.reviews.entity.Product;
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
public class ProductRepositoryTest {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testFindProductById(){
        Product product = getProduct();
        entityManager.persist(product);

        Optional<Product> responseProduct = productRepository.findById(product.getId());
        Assert.assertNotNull(responseProduct.get());
        Assert.assertEquals(product.getName(), responseProduct.get().getName());
    }

    @Test
    public void testFindAllProduct(){
        Product product = getProduct();
        entityManager.persist(product);

        List<Product> responseProduct = productRepository.findAll();
        Assert.assertNotNull(responseProduct);
        Assert.assertEquals(product.getName(), responseProduct.get(0).getName());
    }

    private Product getProduct() {
        Product product = new Product();
        product.setName("Mobile");
        product.setDescription("Apple mobile");
        return product;
    }
}
