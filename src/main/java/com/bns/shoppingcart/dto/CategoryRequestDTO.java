package com.bns.shoppingcart.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO to hold the Category request attributes.
 */
public class CategoryRequestDTO {
    @JsonProperty("name")
    private String categoryName;
    @JsonProperty("description")
    private String description;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
