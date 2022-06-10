package com.netcompany.crawltool.model;

import com.google.api.client.util.DateTime;

import java.sql.Timestamp;

public class UpdateTime {
    public Timestamp getSatDate() {
        return satDate;
    }

    public void setSatDate(Timestamp satDate) {
        this.satDate = satDate;
    }

    private Timestamp satDate;

}
