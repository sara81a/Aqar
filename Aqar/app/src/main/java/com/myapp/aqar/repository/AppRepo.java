package com.myapp.aqar.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.myapp.aqar.dao.AppDAO;
import com.myapp.aqar.database.AppDB;
import com.myapp.aqar.database.entities.House;
import com.myapp.aqar.database.entities.Manager;
import com.myapp.aqar.database.entities.SubscribedHouses;
import com.myapp.aqar.database.entities.User;

import java.util.List;

public class AppRepo {
    public AppDB instanceOfDB;

    public AppDAO appDAO;

    public AppRepo(Context context) {
        instanceOfDB = AppDB.getInstance(context);
        appDAO = instanceOfDB.appDAO();
    }


    public long insertUser(User user){
        return appDAO.insertUser(user);
    }

    public long subscribeToHouse(SubscribedHouses house){
        return appDAO.subscribeHouse(house);
    }

    public long insertManager(Manager manager){
        return appDAO.insertManager(manager);
    }

    public long createHouse(House house){
        return appDAO.createHouse(house);
    }

    public User loginUser(String userName, String password){
        return appDAO.loginUser(userName, password);
    }

    public Manager loginManager(String userName, String password){
        return appDAO.loginManager(userName, password);
    }

    public int updateUser(User user){
        return appDAO.updateUser(user);
    }

    public int updateManager(Manager manager){
        return appDAO.updateManager(manager);
    }

    public int updateHouse(House house){
        return appDAO.updateHouse(house);
    }

    public House getHouseByName(String houseTitle){
        return appDAO.getHouseByName(houseTitle);
    }

    public LiveData<List<House>> getAllHouse(){
        return appDAO.getAllHouse();
    }

    public LiveData<List<User>> getAllUser(){
        return appDAO.getAllUser();
    }

    public LiveData<List<Manager>> getALlManager(){
        return appDAO.getAllManager();
    }

    public LiveData<List<SubscribedHouses>> getAllSubscribedUser(){
        return appDAO.getAllSubscribedHouse();
    }

}
