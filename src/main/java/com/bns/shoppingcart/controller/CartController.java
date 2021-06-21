package com.bns.shoppingcart.controller;

import com.bns.shoppingcart.dto.CartRequestDTO;
import com.bns.shoppingcart.exception.InvalidCustomerException;
import com.bns.shoppingcart.exception.InvalidProductException;
import com.bns.shoppingcart.model.CartItem;
import com.bns.shoppingcart.model.ShoppingCart;
import com.bns.shoppingcart.service.ShoppingCartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Cart related resource controller
 */
@RestController
@RequestMapping(value = "/cart")
public class CartController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);
    @Autowired
    private ShoppingCartService cartService;

    /**
     * API Endpoint that adds items to the cart. Also creates a new cart.
     *
     * @param request
     * @return
     * @throws InvalidCustomerException
     * @throws InvalidProductException
     */
    @RequestMapping(value = "/items/add", method = RequestMethod.POST)
    public ShoppingCart addToCart(@RequestBody CartRequestDTO request) throws InvalidCustomerException, InvalidProductException {
        List<CartItem> cartItems;
        ShoppingCart cart = cartService.createCart(request);
        logger.info("Created Shopping-Cart for Customer ID:" + request.getCustomerId());
        cartItems = cartService.addItemsToCart(request.getCartItems(), cart);
        logger.info("Saved the Cart Items for Cart ID:" + cart.getCartId());
        ShoppingCart response = cartService.calculateAndUpdateShoppingCart(cart, cartItems);
        logger.info("Shopping Cart Updated" + cart.getCartId());

        return response;
    }
}
