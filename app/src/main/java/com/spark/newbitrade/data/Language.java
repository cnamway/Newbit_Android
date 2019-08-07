package com.spark.newbitrade.data;

import java.util.Locale;


public enum Language {
    CH(1, java.util.Locale.SIMPLIFIED_CHINESE),
    EN(2, java.util.Locale.US),
    JA(3, java.util.Locale.JAPAN);

    private int status;
    private Locale Locale;

    Language(int code, Locale locale) {
        this.status = status;
        this.Locale = Locale;
    }

    public java.util.Locale getLocale() {
        return Locale;
    }
}