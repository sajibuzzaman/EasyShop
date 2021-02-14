package com.nabdroid.easyshop.model;

public class Customer {
    private String customerName, number;
    private int due;
    private boolean visibilityCheck = true;

    public boolean isVisibilityCheck() {
        return visibilityCheck;
    }

    public void setVisibilityCheck(boolean visibilityCheck) {
        this.visibilityCheck = visibilityCheck;
    }

    public Customer() {
    }

    public Customer(String customerName, String address, int due) {
        this.customerName = customerName;
        this.number = address;
        this.due = due;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getDue() {
        return due;
    }

    public void setDue(int due) {
        this.due = due;
    }
}
