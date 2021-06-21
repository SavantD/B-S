package com.bns.shoppingcart.controller;

import com.bns.shoppingcart.dto.CategoryRequestDTO;
import com.bns.shoppingcart.model.Category;
import com.bns.shoppingcart.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Product Category related resource controller
 */
@RestController
@RequestMapping(value = "/category")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
    @Autowired
    private CategoryService categoryService;

    /**
     * Gets all the Categories
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/fetch-all", method = RequestMethod.GET)
    public List<Category> fetchAllCategories() throws Exception {
        List<Category> response;
        try {
            response = categoryService.getAllCategories();
            logger.info("Retreived categories count: " + response.size());
        } catch (Exception ex) {
            logger.error("Exception when fetching all categories (category/fetch-all)", ex.getMessage());
            throw new Exception("Error fetchAllCategories() method", ex);
        }

        return response;
    }

    /**
     * Gets all the Categories based on the categoryId
     *
     * @param categoryId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/fetch", method = RequestMethod.GET)
    public Category fetchCategoryById(@RequestParam(name = "category_id", required = true) int categoryId) throws Exception {
        Category response;
        try {
            response = categoryService.getCategoryById(categoryId);
            logger.info("Retreived category for ID: " + categoryId);
        } catch (Exception ex) {
            logger.error("Exception when fetching category by id (category/fetch)", ex.getMessage());
            throw new Exception("Error fetchCategoryById() method", ex);
        }

        return response;
    }

    /**
     * Creates a new category
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequestDTO request) throws Exception {
        ResponseEntity<?> response;
        try {
            response = categoryService.createCategory(request);
            logger.info("Created Category with name " + request.getCategoryName());
        } catch (Exception ex) {
            logger.error("Exception when creating category", ex.getMessage());
            throw new Exception("Error createCategory() method", ex);
        }

        return response;
    }
}
