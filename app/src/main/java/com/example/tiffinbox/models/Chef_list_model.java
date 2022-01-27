package com.example.tiffinbox.models;

public class Chef_list_model {

    String name, phone, email, purl;
    public Chef_list_model() {
    }
    public Chef_list_model(String name, String phone, String email, String purl) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.purl = purl;
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
}



