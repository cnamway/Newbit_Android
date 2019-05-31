package com.spark.newbitrade.data;

import java.util.Locale;


public enum Language {
    CH(1, java.util.Locale.CHINESE),
    EN(2, java.util.Locale.ENGLISH),
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