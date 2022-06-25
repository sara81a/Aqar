package com.myapp.aqar.database.entities;

import static com.myapp.aqar.constants.AppConstants.MANAGER_ID;
import static com.myapp.aqar.constants.AppConstants.NAME;
import static com.myapp.aqar.constants.AppConstants.PASSWORD;
import static com.myapp.aqar.constants.AppConstants.PHONENUMBER;
import static com.myapp.aqar.constants.AppConstants.USEREMAIL;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Manager {

    @ColumnInfo(name = MANAGER_ID)
    @PrimaryKey(autoGenerate = true)
    private int managerID;

    @ColumnInfo(name = NAME)
    private String name;

    @ColumnInfo(name = USEREMAIL)
    private String email;

    @ColumnInfo(name = PASSWORD)
    private String password;

    @ColumnInfo(name = PHONENUMBER)
    private String phoneNumber;


    public Manager() {
    }

    public Manager(String name, String email, String password, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public int getManagerID() {
        return managerID;
    }

    public void setManagerID(int managerID) {
        this.managerID = managerID;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
