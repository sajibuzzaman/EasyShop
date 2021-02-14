package com.nabdroid.easyshop.model;

public class Shop {
    private String shopName, email, password;

    public Shop(String shopName, String email, String password) {
        this.shopName = shopName;
        this.email = email;
        this.password = password;
    }

    public Shop() {
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
