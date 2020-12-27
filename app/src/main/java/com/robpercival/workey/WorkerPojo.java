package com.robpercival.workey;

public class WorkerPojo {

    String mobile;
    String password,firstName,lastName,houseNumber,street,area,city,gender,age;

    public WorkerPojo(String mobile, String password, String firstName, String lastName, String houseNumber, String street, String area, String city,String age) {
        this.mobile = mobile;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.houseNumber = houseNumber;
        this.street = street;
        this.area = area;
        this.city = city;
        this.gender = gender;
        this.age = age;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
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

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
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

    public WorkerPojo(String mobile, String password) {
        this.mobile = mobile;
        this.password = password;
    }

    public WorkerPojo() {
        mobile="0";
        password="0";
        firstName="0";
        lastName="0";
        houseNumber="0";
        street="0";
        area= "0";
        city="0";
        gender = "male";
        age = "16";
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
