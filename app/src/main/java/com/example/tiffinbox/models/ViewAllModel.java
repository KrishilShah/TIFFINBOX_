package com.example.tiffinbox.models;


public class ViewAllModel {
    String ddes,dname,dprice,id,url;
    public ViewAllModel() {
    }

    public ViewAllModel(String ddes, String dname, String dprice, String id, String url) {
        this.ddes = ddes;
        this.dname = dname;
        this.dprice = dprice;
        this.id = id;
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



}
