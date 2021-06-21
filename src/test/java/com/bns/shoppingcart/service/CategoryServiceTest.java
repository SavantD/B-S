package com.bns.shoppingcart.service;

import com.bns.shoppingcart.model.Category;
import com.bns.shoppingcart.repository.CategoryRepository;
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
public class CategoryServiceTest {

    @InjectMocks
    CategoryService categoryService;

    @Mock
    CategoryRepository categoryRepository;

    @Test
    public void getAllCategoriesTest() {
        List<Category> list = new ArrayList<>();
        Category expected = new Category();
        expected.setCategoryName("123");
        expected.setDescription("desc 123");
        list.add(expected);
        when(categoryRepository.findAll()).thenReturn(list);
        List<Category> returnList = categoryService.getAllCategories();
        Assert.assertEquals(1, returnList.size());
        Assert.assertEquals("123", returnList.get(0).getCategoryName());
    }

    @Test
    public void getCategoryByIdTest() {
        Category expected = new Category();
        expected.setCategoryName("123");
        expected.setDescription("desc 123");
        when(categoryRepository.findById(1)).thenReturn(java.util.Optional.of(expected));
        Category result = categoryService.getCategoryById(1);
        Assert.assertEquals(expected, result);
    }
}
