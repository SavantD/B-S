package com.bns.shoppingcart.service;

import com.bns.shoppingcart.dto.CategoryRequestDTO;
import com.bns.shoppingcart.model.Category;
import com.bns.shoppingcart.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;

/**
 * Handles Product Category related business logic.
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Gets all the Catigories.
     * @return List<Category>
     */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Gets Catigory By Id.
     * @param categoryId
     * @return Category
     */
    public Category getCategoryById(int categoryId) {
        return categoryRepository.findById(categoryId).orElse(null);
    }

    /**
     * Create a new Categiry
     * @param requestDTO
     * @return ResponseEntity<?>
     */
    public ResponseEntity<?> createCategory(CategoryRequestDTO requestDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Category category = modelMapper.map(requestDTO, Category.class);
        Category persistedCategory = categoryRepository.save(category);

        return ResponseEntity.created(URI.create(String.format("/category/%s", persistedCategory.getCategoryId())))
                .body(persistedCategory);
    }
}
