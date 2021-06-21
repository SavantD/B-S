package com.bns.shoppingcart.repository;

import com.bns.shoppingcart.model.CartItem;
import com.bns.shoppingcart.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    @Query("SELECT ci FROM CartItem ci INNER JOIN ci.cart cart WHERE cart.cartId = ?1")
    List<ShoppingCart> findCartItemsByCartId(int cartId);
}
