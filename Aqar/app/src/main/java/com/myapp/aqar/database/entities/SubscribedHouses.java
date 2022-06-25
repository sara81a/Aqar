package com.myapp.aqar.database.entities;

import static com.myapp.aqar.constants.AppConstants.SUBSCRIBED_HOUSE;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = SUBSCRIBED_HOUSE)
public class SubscribedHouses {
    @PrimaryKey
    int userID;
    int houseID;

    public SubscribedHouses(int userID, int houseID) {
        this.userID = userID;
        this.houseID = houseID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getHouseID() {
        return houseID;
    }

    public void setHouseID(int houseID) {
        this.houseID = houseID;
    }
}
