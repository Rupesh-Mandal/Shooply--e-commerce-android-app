package com.firoz.shooply.model;

public class AddressBookModel {
    private Long id;

    private String userId;
    private String addressId;
    private String productDeliverAddress;
    private String userPhoneNumber;

    public AddressBookModel() {
    }

    public AddressBookModel(Long id, String userId, String addressId, String productDeliverAddress, String userPhoneNumber) {
        this.id = id;
        this.userId = userId;
        this.addressId = addressId;
        this.productDeliverAddress = productDeliverAddress;
        this.userPhoneNumber = userPhoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getProductDeliverAddress() {
        return productDeliverAddress;
    }

    public void setProductDeliverAddress(String productDeliverAddress) {
        this.productDeliverAddress = productDeliverAddress;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }
}
