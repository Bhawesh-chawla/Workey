package com.robpercival.workey;
public class OwnerAdvertismentPojo {

    String shopName,workDescription,workerDescription,shopNumber,street,area,city,salaryRange,number;

    public OwnerAdvertismentPojo(String shopName, String workDescription, String workerDescription, String shopNumber, String street, String area, String city, String salaryRange,String number) {
        this.shopName = shopName;
        this.workDescription = workDescription;
        this.workerDescription = workerDescription;
        this.shopNumber = shopNumber;
        this.street = street;
        this.area = area;
        this.city = city;
        this.salaryRange = salaryRange;
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public OwnerAdvertismentPojo() {
        shopName = "0";
        workDescription = "0";
        workerDescription = "0";
        shopNumber = "0";
        street = "0";
        area = "0";
        city = "0";
        salaryRange = "0";
        number = "0";
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getWorkDescription() {
        return workDescription;
    }

    public void setWorkDescription(String workDescription) {
        this.workDescription = workDescription;
    }

    public String getWorkerDescription() {
        return workerDescription;
    }

    public void setWorkerDescription(String workerDescription) {
        this.workerDescription = workerDescription;
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

    public String getSalaryRange() {
        return salaryRange;
    }

    public void setSalaryRange(String salaryRange) {
        this.salaryRange = salaryRange;
    }
}
