package com.bns.shoppingcart.service;

import com.bns.shoppingcart.model.Product;
import com.bns.shoppingcart.repository.ProductRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class ProductServiceTest {

    @InjectMocks
    ProductService productService;

    @Mock
    ProductRepository productRepository;

    @Test
    public void getAllProductsTest() {
        List<Product> list = new ArrayList<>();
        Product expected = new Product();
        expected.setTitle("123");
        expected.setProductName("pro 123");
        list.add(expected);
        when(productRepository.findAll()).thenReturn(list);
        List<Product> returnList = productService.getAllProducts();
        Assert.assertEquals(1, returnList.size());
        Assert.assertEquals("123", returnList.get(0).getTitle());
    }

    @Test
    public void getProductByIdTest() {
        Product expected = new Product();
        expected.setTitle("123");
        expected.setProductName("pro 123");
        when(productRepository.findById(2)).thenReturn(java.util.Optional.of(expected));
        Product result = productService.getProductById(2);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void getAllProductsByCategoryTest() {
        List<Product> list = new ArrayList<>();
        Product expected = new Product();
        expected.setTitle("123");
        expected.setProductName("pro 123");
        list.add(expected);
        when(productRepository.findProductsByCategory(2)).thenReturn(list);
        List<Product> result = productService.getAllProductsByCategory(2);
        Assert.assertEquals(list, result);
    }
}
