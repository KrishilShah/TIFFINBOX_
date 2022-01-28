package com.example.tiffinbox.models;

public class ModelChef {
    String ddes,dname,dprice,url;

    public ModelChef() {

    }

    public ModelChef(String ddes, String dname, String dprice, String url) {
        this.ddes = ddes;
        this.dname = dname;
        this.dprice = dprice;
        this.url = url;
    }

    public String getDdes() {
        return ddes;
    }

    public void setDdes(String ddes) {
        this.ddes = ddes;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getDprice() {
        return dprice;
    }

    public void setDprice(String dprice) {
        this.dprice = dprice;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
