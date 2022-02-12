package com.example.tiffinbox.models;

public class DishData {
    String dname;
    String ddes;
    String url;
    String chefId;
    String cname;
    String dishId;
    String dprice;
    String DishType;

    public DishData() {

    }



    public DishData(String dname, String ddes, String dprice, String url, String chefId, String dishId, String cname , String DishType) {
        this.dname = dname;
        this.cname = cname;
        this.ddes = ddes;
        this.dprice = dprice;
        this.chefId=chefId;
        this.dishId = dishId;
        this.url = url;
        this.DishType=DishType;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getChefId() {
        return chefId;
    }

    public void setChefId(String chefId) {
        this.chefId = chefId;
    }

    public String getDishId() {
        return dishId;
    }

    public void setDishId(String dishId) {
        this.dishId = dishId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String  getDprice() {
        return dprice;
    }

    public void setDprice(String dprice) {
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
    public String getDishType() {
        return DishType;
    }

    public void setDishType(String dishType) {
        DishType = dishType;
    }
}
