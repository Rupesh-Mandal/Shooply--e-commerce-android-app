package com.firoz.shooply.model;


public class StoreCategory {

    private Long id;

    private String storeCategoryId;
    private String category;
    private String banner;
    private String logo;

    public StoreCategory() {
    }

    public StoreCategory(Long id, String storeCategoryId, String category, String banner, String logo) {
        this.id = id;
        this.storeCategoryId = storeCategoryId;
        this.category = category;
        this.banner = banner;
        this.logo = logo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStoreCategoryId() {
        return storeCategoryId;
    }

    public void setStoreCategoryId(String storeCategoryId) {
        this.storeCategoryId = storeCategoryId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
