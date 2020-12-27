package com.robpercival.workey;

import java.util.ArrayList;

public class workerWishlistPojo {
    String workerNumber;
    ArrayList<String> ownerAdvertismentNumber = new ArrayList<>();

    public workerWishlistPojo(String workerNumber, ArrayList<String> ownerAdvertismentNumber) {
        this.workerNumber = workerNumber;
        this.ownerAdvertismentNumber = ownerAdvertismentNumber;
    }

    public String getWorkerNumber() {
        return workerNumber;
    }

    public void setWorkerNumber(String workerNumber) {
        this.workerNumber = workerNumber;
    }

    public ArrayList<String> getOwnerAdvertismentNumber() {
        return ownerAdvertismentNumber;
    }

    public void setOwnerAdvertismentNumber(ArrayList<String> ownerAdvertismentNumber) {
        this.ownerAdvertismentNumber = ownerAdvertismentNumber;
    }
}
