package com.example.tiffinbox.models;

public class ChefOrderData {
    String userID, dishName,dishTime,dishPrice,dishDate,id,totalQuantity,durl,dishDescription,chefID,orderStatus;
    int totalPrice;
    double latitude, longitude;

    public ChefOrderData(){

    }
    public ChefOrderData(String userID,String dishName, String dishTime, String dishPrice, String dishDate, String id, String totalQuantity, String durl, String dishDescription, String chefID, String orderStatus, int totalPrice,double latitude, double longitude) {
        this.userID = userID;
        this.dishName = dishName;
        this.dishTime = dishTime;
        this.dishPrice = dishPrice;
        this.dishDate = dishDate;
        this.id = id;
        this.totalQuantity = totalQuantity;
        this.durl = durl;
        this.dishDescription = dishDescription;
        this.chefID = chefID;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
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

    public String getDishDate() {
        return dishDate;
    }

    public void setDishDate(String dishDate) {
        this.dishDate = dishDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getDurl() {
        return durl;
    }

    public void setDurl(String durl) {
        this.durl = durl;
    }

    public String getDishDescription() {
        return dishDescription;
    }

    public void setDishDescription(String dishDescription) {
        this.dishDescription = dishDescription;
    }

    public String getChefID() {
        return chefID;
    }

    public void setChefID(String chefID) {
        this.chefID = chefID;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
