package com.bns.shoppingcart.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO to hold the Product request attributes.
 */
public class ProductRequestDTO {

    @JsonProperty(value = "category_id")
    private int categoryId;
    @JsonProperty(value = "title")
    private String title;
    @JsonProperty(value = "name")
    private String productName;
    @JsonProperty(value = "price")
    private double price;
    @JsonProperty(value = "tax")
    private double tax;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }
}
