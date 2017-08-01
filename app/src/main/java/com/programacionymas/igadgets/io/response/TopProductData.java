package com.programacionymas.igadgets.io.response;

import io.realm.RealmObject;

public class TopProductData extends RealmObject {
    private String product;
    private int quantity;
    private String percent;

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }
}
