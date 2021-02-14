package com.nabdroid.easyshop.model;

public class Product {
    private String productName, supplier, imageURL;
    private int quantity, unitPrice;
    private boolean visibilityStatus = true;

    public boolean isVisibilityStatus() {
        return visibilityStatus;
    }

    public void setVisibilityStatus(boolean visibilityStatus) {
        this.visibilityStatus = visibilityStatus;
    }

    public Product(String productName, String supplier, String imageURL, int quantity, int unitPrice, boolean visibilityStatus) {
        this.productName = productName;
        this.supplier = supplier;
        this.imageURL = imageURL;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.visibilityStatus = visibilityStatus;
    }

    public Product(String productName, String supplier, String imageURL, int quantity, int unitPrice) {
        this.productName = productName;
        this.supplier = supplier;
        this.imageURL = imageURL;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public Product() {
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }
}
