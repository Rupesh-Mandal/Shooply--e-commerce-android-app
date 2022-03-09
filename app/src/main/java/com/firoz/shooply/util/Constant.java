package com.firoz.shooply.util;

public class Constant {
    public static final String  BaseUrl="http://139.59.46.233/api/v1";
    public static final String LogIn=BaseUrl+"/auth/sign_in";
    public static final String SignUp=BaseUrl+"/auth/sign_up";
    public static final String getStoreByStoreId=BaseUrl+"/store/getStoreByStoreId";
    public static final String updateStore=BaseUrl+"/store/updateStore";

    public static final String getOrderAsSeller=BaseUrl+"/order/getOrderAsSeller";
    public static final String getOrderAsUser=BaseUrl+"/order/getOrderAsUser";

    public static final String acceptOrder=BaseUrl+"/order/acceptOrder";
    public static final String cancelOrderBySeller=BaseUrl+"/order/cancelOrderBySeller";
    public static final String deliverdFaildOrder=BaseUrl+"/order/deliverdFaildOrder";
    public static final String onDeliveryStarted=BaseUrl+"/order/deliveryStarted";
    public static final String addProduct=BaseUrl+"/product/addProduct";
    public static final String updateProduct=BaseUrl+"/product/updateProduct";
    public static final String getAllProductSeller=BaseUrl+"/product/getAllProductSeller";
    public static final String findByProductId=BaseUrl+"/product/findByProductId";
    public static final String deletProduct=BaseUrl+"/product/deletProduct";
    public static final String loadSearch=BaseUrl+"/product/loadSearch";
    public static final String findByStorecategory=BaseUrl+"/product/findByStorecategory";
    public static final String getAllProductUser=BaseUrl+"/product/getAllProductUser";

    public static final String getAddressByUserId=BaseUrl+"/addressBook/getAddressByUserId";
    public static final String getDefaultAddressByUserId=BaseUrl+"/addressBook/getDefaultAddressByUserId";
    public static final String addAddress=BaseUrl+"/addressBook/addAddress";
    public static final String deleteByAddressId=BaseUrl+"/addressBook/deleteByAddressId";
    public static final String setDefaultAddress=BaseUrl+"/addressBook/setDefaultAddress";
    public static final String updateAddress=BaseUrl+"/addressBook/updateAddress";

    public static final String getAllCategory=BaseUrl+"/admin/getAllCategory";
    public static final String getSubCategory=BaseUrl+"/admin/getSubCategory";


    public static final String oderProduct=BaseUrl+"/order/oderProduct";
    public static final String addToCart=BaseUrl+"/order/addToCart";
    public static final String getAllCartList=BaseUrl+"/order/getAllCartList";
    public static final String updateCart=BaseUrl+"/order/updateCart";
    public static final String deleteCart=BaseUrl+"/order/deleteCart";
    public static final String getOrderHistory=BaseUrl+"/order/getOrderHistory";
    public static final String getStartedOrder=BaseUrl+"/order/getStartedOrder";
    public static final String getPendingOrder=BaseUrl+"/order/getPendingOrder";
    public static final String cancelOrderByUser=BaseUrl+"/order/cancelOrderByUser";



}
