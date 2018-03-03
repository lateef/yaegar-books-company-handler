package com.yaegar.books.model;

public class Vat {
    private Integer number;
    private String scheme;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    @Override
    public String toString() {
        return "Vat{" +
                "number=" + number +
                ", scheme='" + scheme + '\'' +
                '}';
    }
}
