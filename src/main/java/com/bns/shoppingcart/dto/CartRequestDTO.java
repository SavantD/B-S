package com.bns.shoppingcart.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * DTO to hold the Cart request attributes.
 */
public class CartRequestDTO {
    @JsonProperty("customer_id")
    private int customerId;
    @JsonProperty("items")
    private List<CartItemDTO> cartItems;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public List<CartItemDTO> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemDTO> cartItems) {
        this.cartItems = cartItems;
    }
}
