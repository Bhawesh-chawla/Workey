package com.robpercival.workey;


public class OwnerPojo {
    String mobile;
    String password,firstName,lastName,shopNumber,street,area,city,gender;

    public OwnerPojo(String mobile, String password, String firstName, String lastName, String shopNumber, String street, String area, String city) {
        this.mobile = mobile;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.shopNumber = shopNumber;
        this.street = street;
        this.area = area;
        this.city = city;
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFirstName() {

        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getShopNumber() {
        return shopNumber;
    }

    public void setShopNumber(String shopNumber) {
        this.shopNumber = shopNumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public OwnerPojo(String mobile, String password) {
        this.mobile = mobile;
        this.password = password;
    }

    public OwnerPojo() {
        mobile="0";
        password="0";
        firstName="0";
        lastName="0";
        shopNumber="0";
        street="0";
        area= "0";
        city="0";
        gender = "male";
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
