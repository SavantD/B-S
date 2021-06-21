package com.bns.shoppingcart.controller;

import com.bns.shoppingcart.dto.ProductRequestDTO;
import com.bns.shoppingcart.exception.InvalidCategoryException;
import com.bns.shoppingcart.model.Product;
import com.bns.shoppingcart.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Product related resource controller
 */
@RestController
@RequestMapping(value = "/product")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    private ProductService productService;

    /**
     * Gets all the Products
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/fetch-all", method = RequestMethod.GET)
    public List<Product> fetchAllProducts() throws Exception {
        List<Product> response;
        try {
            response = productService.getAllProducts();
            logger.info("Retreived products count: " + response.size());
        } catch (Exception ex) {
            logger.error("Exception when fetching all products (product/fetch-all)", ex.getMessage());
            throw new Exception("Error fetchAllProducts() method", ex);
        }

        return response;
    }

    /**
     * Gets all the Products based on the categoryId
     *
     * @param categoryId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/fetch", method = RequestMethod.GET)
    public List<Product> fetchProductsByCategoryId(@RequestParam(name = "category_id", required = true) Integer categoryId)
            throws Exception {
        List<Product> response;
        try {
            response = productService.getAllProductsByCategory(categoryId);
            logger.info("Retreived products for category count: " + response.size());
        } catch (Exception ex) {
            logger.error("Exception when fetching products by category (product/fetch)", ex.getMessage());
            throw new Exception("Error fetchProductsByCategoryId() method", ex);
        }

        return response;
    }

    /**
     * Creates a new product
     *
     * @param request
     * @return
     * @throws InvalidCategoryException
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<?> createProduct(@RequestBody ProductRequestDTO request) throws InvalidCategoryException {
        ResponseEntity<?> response;
        response = productService.createProduct(request);
        logger.info("Created Product with name " + request.getProductName());

        return response;
    }
}
