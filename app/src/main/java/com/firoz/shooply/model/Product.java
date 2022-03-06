package com.firoz.shooply.model;

import java.time.LocalDateTime;

public class Product {
    private Long id;
    private String productId;

    private String storeId;
    private String storeName;
    private String storeEmail;

    private String productName;
    private String productDescription;
    private String productRate;

    private String productImageLink;
    private String mrp;
    private String discount;
    private String storeCategoryId;
    private String storecategory;
    private String sub_category;

    private String createdTime;

    public Product() {
    }

    public Product(Long id, String productId, String storeId, String storeName, String storeEmail, String productName, String productDescription, String productRate, String productImageLink, String mrp, String discount, String storeCategoryId, String storecategory, String sub_category, String createdTime) {
        this.id = id;
        this.productId = productId;
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeEmail = storeEmail;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productRate = productRate;
        this.productImageLink = productImageLink;
        this.mrp = mrp;
        this.discount = discount;
        this.storeCategoryId = storeCategoryId;
        this.storecategory = storecategory;
        this.sub_category = sub_category;
        this.createdTime = createdTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreEmail() {
        return storeEmail;
    }

    public void setStoreEmail(String storeEmail) {
        this.storeEmail = storeEmail;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductRate() {
        return productRate;
    }

    public void setProductRate(String productRate) {
        this.productRate = productRate;
    }

    public String getProductImageLink() {
        return productImageLink;
    }

    public void setProductImageLink(String productImageLink) {
        this.productImageLink = productImageLink;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getStoreCategoryId() {
        return storeCategoryId;
    }

    public void setStoreCategoryId(String storeCategoryId) {
        this.storeCategoryId = storeCategoryId;
    }

    public String getStorecategory() {
        return storecategory;
    }

    public void setStorecategory(String storecategory) {
        this.storecategory = storecategory;
    }

    public String getSub_category() {
        return sub_category;
    }

    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }
}
