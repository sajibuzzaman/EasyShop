package com.nabdroid.easyshop.model;

public class Supplier {
    private String name, number, products;
    private int due;

    public boolean isVisibilityStatus() {
        return visibilityStatus;
    }

    public void setVisibilityStatus(boolean visibilityStatus) {
        this.visibilityStatus = visibilityStatus;
    }

    private boolean visibilityStatus = true;

    public Supplier() {
    }

    public Supplier(String name, String address, String products, int due) {
        this.name = name;
        this.number = address;
        this.products = products;
        this.due = due;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public int getDue() {
        return due;
    }

    public void setDue(int due) {
        this.due = due;
    }
}
