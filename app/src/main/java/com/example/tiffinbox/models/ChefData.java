package com.example.tiffinbox.models;

import java.util.List;

public class ChefData {

    String name, phone, email, purl,id, userType, password, speciality, about, pincode;
    List<String> dpin;

    public List<String> getDpin() {
        return dpin;
    }

    public void setDpin(List<String> dpin) {
        this.dpin = dpin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ChefData() {
    }
    public ChefData(String name, String phone, String email, String purl, String id, String userType, String password, String speciality, String about, List<String> dpin) {
        this.id=id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.purl = purl;
        this.userType= userType;
        this.password=password;
        this.speciality = speciality;
        this.about = about;
        this.dpin = dpin;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
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

    public String getSpeciality() { return speciality; }

    public void setSpeciality(String speciality) { this.speciality = speciality; }

    public String getAbout() { return about; }

    public void setAbout(String about) { this.about = about; }
}



