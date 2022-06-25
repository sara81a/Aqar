package com.myapp.aqar.database.entities;

import static com.myapp.aqar.constants.AppConstants.PASSWORD;
import static com.myapp.aqar.constants.AppConstants.PHONENUMBER;
import static com.myapp.aqar.constants.AppConstants.USEREMAIL;
import static com.myapp.aqar.constants.AppConstants.USERNAME;
import static com.myapp.aqar.constants.AppConstants.USER_ID;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "User")
public class User {

    @ColumnInfo(name = USER_ID)
    @PrimaryKey(autoGenerate = true)
    private int user_id;

    @ColumnInfo(name = USERNAME)
    private String userName;

    @ColumnInfo(name = USEREMAIL)
    private String email;

    @ColumnInfo(name = PASSWORD)
    private String password;

    @ColumnInfo(name = PHONENUMBER)
    private String phoneNumber;

    public User() {
    }

    public User(String userName, String email, String password, String phoneNumber) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUserName() {
        return userName;
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
