package com.bns.shoppingcart.repository;

import com.bns.shoppingcart.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p INNER JOIN p.category c WHERE c.categoryId = ?1")
    List<Product> findProductsByCategory(int categoryId);
}
