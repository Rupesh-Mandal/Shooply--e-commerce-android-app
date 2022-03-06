package com.firoz.shooply.model;

public class OrderModel {
    private Long id;
    private String orderId;
    private String productId;
    private String storeId;
    private String storeName;
    private String storeEmail;
    private String productName;
    private String productDescription;
    private String productQuantity;
    private String productRate;
    private String productTotalRate;
    private String productDeliverAddress;

    private String productImageLink;
    private String productCategory;
    private String userPhoneNumber;
    private String userName;
    private String userId;
    private String status;
    private String statusMessage;
    private String createdTime;

    public OrderModel() {
    }

    public OrderModel(Long id, String orderId, String productId, String storeId, String storeName, String storeEmail, String productName, String productDescription, String productQuantity, String productRate, String productTotalRate, String productImageLink, String productCategory, String userPhoneNumber, String userName, String userId, String status, String statusMessage, String createdTime) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeEmail = storeEmail;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productQuantity = productQuantity;
        this.productRate = productRate;
        this.productTotalRate = productTotalRate;
        this.productImageLink = productImageLink;
        this.productCategory = productCategory;
        this.userPhoneNumber = userPhoneNumber;
        this.userName = userName;
        this.userId = userId;
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

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
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

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
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

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }
}
