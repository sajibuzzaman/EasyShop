package com.nabdroid.easyshop.model;

public class AmountHistory {
    private int amount, date;
    private String amountType;

    public AmountHistory() {
    }

    public AmountHistory(int amount, int date, String amountType) {
        this.amount = amount;
        this.date = date;
        this.amountType = amountType;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getAmountType() {
        return amountType;
    }

    public void setAmountType(String amountType) {
        this.amountType = amountType;
    }
}
