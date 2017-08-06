package com.programacionymas.igadgets.io.response;

import java.util.ArrayList;

public class TopProductsResponse {

    private ArrayList<TopProductData> products; // product and quantity

    public ArrayList<TopProductData> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<TopProductData> products) {
        this.products = products;
    }

}
