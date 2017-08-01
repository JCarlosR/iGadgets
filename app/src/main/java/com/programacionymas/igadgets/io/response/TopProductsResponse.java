package com.programacionymas.igadgets.io.response;

import java.util.ArrayList;

import io.realm.RealmObject;

public class TopProductsResponse {

    private ArrayList<TopProductData> pairs; // product and quantity

    public ArrayList<TopProductData> getPairs() {
        return pairs;
    }

    public void setPairs(ArrayList<TopProductData> pairs) {
        this.pairs = pairs;
    }

}
