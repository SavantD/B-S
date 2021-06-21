package com.bns.shoppingcart.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO to hold the attributes of a single item of the Cart.
 */
public class CartItemDTO {
    @JsonProperty("product_id")
    private int productId;
    @JsonProperty("quantity")
    private int quantity;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
