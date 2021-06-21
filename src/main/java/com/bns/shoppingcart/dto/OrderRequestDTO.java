package com.bns.shoppingcart.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * DTO to hold the Order request attributes.
 */
public class OrderRequestDTO {
    @JsonProperty("customer_id")
    private int customerId;
    @JsonProperty("items")
    private List<OrderItemDTO> orderItems;
    @JsonProperty("coupon_code")
    private String couponCode;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public List<OrderItemDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }
}
