package org.smartregister.anc.library.activity;

import java.io.Serializable;

public class MonthModel implements Serializable {

    private String monthName;
    public static int monthNumber;

    public MonthModel(String monthName, int monthNumber) {
        this.monthName = monthName;
        this.monthNumber = monthNumber;
    }

    public String getMonthName() {
        return monthName;
    }

    public int getMonthNumber() {
        return monthNumber;
    }
}
