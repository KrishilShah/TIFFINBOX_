package com.example.tiffinbox.models;

public class ChefData {

    String name, phone, email, purl,id, userType, password;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ChefData() {
    }
    public ChefData(String name, String phone, String email, String purl, String id, String userType, String password) {
        this.id=id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.purl = purl;
        this.userType= userType;
        this.password=password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}



