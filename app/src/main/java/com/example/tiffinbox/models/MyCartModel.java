package com.example.tiffinbox.models;

public class MyCartModel {
    String dishName,dishTime,dishPrice,dishDate,id,totalQuantity,durl,dishDescription,chefID,orderStatus;
    int totalPrice;

    public MyCartModel(){

    }
    public MyCartModel(String dishName, int totalPrice, String dishDate, String dishTime, String durl, String totalQuantity, String dishPrice, String dishDescription, String id, String chefID, String orderStatus) {
        this.dishName = dishName;
        this.totalPrice = totalPrice;
        this.dishDate = dishDate;
        this.dishDescription = dishDescription;
        this.dishTime = dishTime;
        this.id = id;
        this.durl = durl;
        this.totalQuantity = totalQuantity;
        this.dishPrice = dishPrice;
        this.chefID=chefID;
        this.orderStatus=orderStatus;
    }

    public String getChefID() {
        return chefID;
    }

    public void setChefID(String chefID) {
        this.chefID = chefID;
    }

    public String getDishDescription() {
        return dishDescription;
    }

    public void setDishDescription(String dishDescription) {
        this.dishDescription = dishDescription;
    }

    public String getDurl() {
        return durl;
    }

    public void setDurl(String durl) {
        this.durl = durl;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDishTime() {
        return dishTime;
    }

    public void setDishTime(String dishTime) {
        this.dishTime = dishTime;
    }

    public String getDishPrice() {
        return dishPrice;
    }

    public void setDishPrice(String dishPrice) {
        this.dishPrice = dishPrice;
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

    public String getDishDate() {
        return dishDate;
    }

    public void setDishDate(String dishDate) {
        this.dishDate = dishDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }





}
