package com.example.tiffinbox.models;

public class CouponModel {
    String Id, timestamp, couponDescription, price, minimumorderprice, couponCode, expiryDate;

    public CouponModel(){

    }

    public CouponModel(String id, String timestamp, String couponDescription, String price, String minimumorderprice, String couponCode, String expiryDate) {
        this.Id = id;
        this.timestamp = timestamp;
        this.couponDescription = couponDescription;
        this.price = price;
        this.minimumorderprice = minimumorderprice;
        this.couponCode = couponCode;
        this.expiryDate = expiryDate;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getCouponDescription() {
        return couponDescription;
    }

    public void setCouponDescription(String couponDescription) {
        this.couponDescription = couponDescription;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMinimumorderprice() {
        return minimumorderprice;
    }

    public void setMinimumorderprice(String minimumorderprice) {
        this.minimumorderprice = minimumorderprice;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}