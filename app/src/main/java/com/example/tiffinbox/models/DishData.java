package com.example.tiffinbox.models;

public class DishData {
    String dname, ddes, url, id, cname;
    Double dprice;

    public DishData() {

    }

    public DishData(String dname, String ddes, double dprice, String url, String id, String cname) {
        this.dname = dname;
        this.cname = cname;
        this.ddes = ddes;
        this.dprice = dprice;
        this.id = id;
        this.url = url;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Double getDprice() {
        return dprice;
    }

    public void setDprice(Double dprice) {
        this.dprice = dprice;
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
}
