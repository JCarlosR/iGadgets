package com.programacionymas.igadgets.io.response;

import io.realm.RealmList;
import io.realm.RealmObject;

public class TopMatrixDay extends RealmObject {

    private RealmList<TopMatrixHour> hours;

    public RealmList<TopMatrixHour> getHours() {
        return hours;
    }

    public void setHours(RealmList<TopMatrixHour> hours) {
        this.hours = hours;
    }


}
