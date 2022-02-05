package com.example.tiffinbox.models;

public class MyCartModel {
    String dishName;
    int totalPrice;
    String dishDate;
    String durl;
    String totalQuantity;

    public MyCartModel(){

    }
    public MyCartModel(String dishName, int totalPrice, String dishDate, String durl, String totalQuantity) {
        this.dishName = dishName;
        this.totalPrice = totalPrice;
        this.dishDate = dishDate;
        this.durl = durl;
        this.totalQuantity = totalQuantity;
    }


    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getdishDate() {
        return dishDate;
    }

    public void setdishDate(String dishDate) {
        dishDate = dishDate;
    }

    public String getdurl() {
        return durl;
    }

    public void setdurl(String durl) {
        this.durl = durl;
    }

    public String gettotalQuantity() {
        return totalQuantity;
    }

    public void settotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
}
