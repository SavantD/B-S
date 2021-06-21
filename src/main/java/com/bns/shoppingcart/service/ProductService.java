package com.bns.shoppingcart.service;

import com.bns.shoppingcart.dto.ProductRequestDTO;
import com.bns.shoppingcart.exception.InvalidCategoryException;
import com.bns.shoppingcart.model.Category;
import com.bns.shoppingcart.model.Product;
import com.bns.shoppingcart.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;

/**
 * Handles Product related business logic.
 */
@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;

    /**
     * Gets all products.
     * @return List<Product>
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Gets all products based on the categoryId
     * @param categoryId
     * @return List<Product>
     */
    public List<Product> getAllProductsByCategory(int categoryId) {
        return productRepository.findProductsByCategory(categoryId);
    }

    /**
     * Get product by ID
     * @param productId
     * @return Product
     */
    public Product getProductById(int productId) {
        return productRepository.findById(productId).orElse(null);
    }

    /**
     * Create a new product
     * @param requestDTO
     * @return ResponseEntity<?>
     * @throws InvalidCategoryException
     */
    public ResponseEntity<?> createProduct(ProductRequestDTO requestDTO) throws InvalidCategoryException {
        ModelMapper modelMapper = new ModelMapper();
        Product product = modelMapper.map(requestDTO, Product.class);

        Category category = categoryService.getCategoryById(requestDTO.getCategoryId());
        Product persistedProduct;
        if (category != null) {
            product.setCategory(category);
            persistedProduct = productRepository.save(product);
        } else {
            InvalidCategoryException icEx = new InvalidCategoryException("Category doesnt exist.");
            logger.error("Exception when creating product createProduct() ", icEx.getCause());
            throw icEx;
        }


        return ResponseEntity.created(URI.create(String.format("/product/%s", persistedProduct.getProductId())))
                .body(persistedProduct);
    }

}
