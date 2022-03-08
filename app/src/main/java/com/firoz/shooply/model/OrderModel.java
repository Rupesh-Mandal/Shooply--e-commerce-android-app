package com.firoz.shooply.model;

import java.time.LocalDateTime;

public class OrderModel {
    private Long id;
    private String orderId;
    private String productId;
    private String userId;
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
    private String quantity;

    private String productDeliverAddress;
    private String userPhoneNumber;
    private String userName;
    private String productTotalRate;

    private String status;
    private String statusMessage;
    private String createdTime;

    public OrderModel() {
    }

    public OrderModel(Long id, String orderId, String productId, String userId, String storeId, String storeName, String storeEmail, String productName, String productDescription, String productRate, String productImageLink, String mrp, String discount, String storeCategoryId, String storecategory, String sub_category, String quantity, String productDeliverAddress, String userPhoneNumber, String userName, String productTotalRate, String status, String statusMessage, String createdTime) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.userId = userId;
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
        this.quantity = quantity;
        this.productDeliverAddress = productDeliverAddress;
        this.userPhoneNumber = userPhoneNumber;
        this.userName = userName;
        this.productTotalRate = productTotalRate;
        this.status = status;
        this.statusMessage = statusMessage;
        this.createdTime = createdTime;
    }

    public String getProductDeliverAddress() {
        return productDeliverAddress;
    }

    public void setProductDeliverAddress(String productDeliverAddress) {
        this.productDeliverAddress = productDeliverAddress;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public String getProductTotalRate() {
        return productTotalRate;
    }

    public void setProductTotalRate(String productTotalRate) {
        this.productTotalRate = productTotalRate;
    }

    public String getProductImageLink() {
        return productImageLink;
    }

    public void setProductImageLink(String productImageLink) {
        this.productImageLink = productImageLink;
    }




    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }
}
