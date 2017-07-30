package com.programacionymas.igadgets.io.response;

import com.google.gson.annotations.SerializedName;

public class TopMatrixHour {

/*
{"q":0,"p":0}
*/

    @SerializedName("q")
    private
    int quantity;

    @SerializedName("p")
    private
    String percentage;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
