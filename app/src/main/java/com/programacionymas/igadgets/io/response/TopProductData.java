package com.programacionymas.igadgets.io.response;

import io.realm.RealmObject;

public class TopProductData extends RealmObject {
    private String product;
    private int quantity;
    private String percent;
    private int mobile;
    private int desktop;

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

    public int getMobile() {
        return mobile;
    }

    public void setMobile(int mobile) {
        this.mobile = mobile;
    }

    public int getDesktop() {
        return desktop;
    }

    public void setDesktop(int desktop) {
        this.desktop = desktop;
    }
}
