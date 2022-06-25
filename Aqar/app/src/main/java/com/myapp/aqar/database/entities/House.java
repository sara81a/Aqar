package com.myapp.aqar.database.entities;

import static com.myapp.aqar.constants.AppConstants.HOUSE_ID;
import static com.myapp.aqar.constants.AppConstants.HOUSE_PRICE;
import static com.myapp.aqar.constants.AppConstants.HOUSE_TITLE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class House {
    @ColumnInfo(name = HOUSE_ID)
    @PrimaryKey(autoGenerate = true)
    private int houseID;

    @ColumnInfo(name = HOUSE_TITLE)
    private String title;

    @ColumnInfo(name = HOUSE_PRICE)
    private int price;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] image;

    private double latitude;

    private double longitude;

    private int ownerID;

    private int subscribedUserID;

    public House(String title, int price, byte[] image, double latitude, double longitude, int ownerID) {
        this.title = title;
        this.price = price;
        this.image = image;
        this.latitude = latitude;
        this.longitude = longitude;
        this.ownerID = ownerID;
    }

    public void setSubscribedUserID(int subscribedUserID) {
        this.subscribedUserID = subscribedUserID;
    }

    public int getSubscribedUserID() {
        return subscribedUserID;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public void setHouseID(int houseID) {
        this.houseID = houseID;
    }

    public int getHouseID() {
        return houseID;
    }

    public byte[] getImage() {
        return image;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public String getTitle() {
        return title;
    }

    public int getPrice() {
        return price;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
