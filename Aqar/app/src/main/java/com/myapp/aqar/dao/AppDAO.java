package com.myapp.aqar.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.myapp.aqar.database.entities.House;
import com.myapp.aqar.database.entities.Manager;
import com.myapp.aqar.database.entities.SubscribedHouses;
import com.myapp.aqar.database.entities.User;

import java.util.List;

@Dao
public interface AppDAO {

    @Insert
    long insertUser(User user);

    @Insert
    long insertManager(Manager manager);

    @Insert
    long createHouse(House house);

    @Insert
    long subscribeHouse(SubscribedHouses house);

    @Update
    int updateUser(User user);

    @Update
    int updateManager(Manager manager);

    @Update
    int updateHouse(House house);


    @Query("SELECT * FROM User WHERE Name= :username AND Password= :password")
    User loginUser(String username, String password);

    @Query("SELECT * FROM Manager WHERE Name= :username AND Password= :password")
    Manager loginManager(String username, String password);

    @Query("SELECT * FROM House")
    LiveData<List<House>> getAllHouse();

    @Query("Select * FROM House where house_title= :hTitle")
    House getHouseByName(String hTitle);

    @Query("SELECT * FROM SubscribedHouse")
    LiveData<List<SubscribedHouses>> getAllSubscribedHouse();

    @Query("SELECT * FROM User")
    LiveData<List<User>> getAllUser();

    @Query("SELECT * FROM Manager")
    LiveData<List<Manager>> getAllManager();


}
