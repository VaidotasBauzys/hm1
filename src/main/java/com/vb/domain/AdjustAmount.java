package com.vb.domain;

public class AdjustAmount {
    private String userId;
    private int amount;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "AdjustAmount{" +
                "userId='" + userId + '\'' +
                ", amount=" + amount +
                '}';
    }
}
