package com.programacionymas.igadgets.io.response;

import java.util.ArrayList;

public class TopProductsResponse {

    private ArrayList<TopProductData> pairs; // product and quantity

    public ArrayList<TopProductData> getPairs() {
        return pairs;
    }

    public void setPairs(ArrayList<TopProductData> pairs) {
        this.pairs = pairs;
    }

    public class TopProductData {
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

}
