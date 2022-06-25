package com.myapp.aqar.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.myapp.aqar.database.entities.House;
import com.myapp.aqar.database.entities.Manager;
import com.myapp.aqar.database.entities.SubscribedHouses;
import com.myapp.aqar.database.entities.User;
import com.myapp.aqar.repository.AppRepo;

import java.util.List;

public class AppViewModel extends AndroidViewModel {
    public AppRepo appRepo;

    public AppViewModel(@NonNull Application application) {
        super(application);
        appRepo = new AppRepo(application);
    }

    public long insertUser(User user) {
        return appRepo.insertUser(user);
    }

    public long subscribeToHouse(SubscribedHouses house) {
        return appRepo.subscribeToHouse(house);
    }


    public long insertManager(Manager manager) {
        return appRepo.insertManager(manager);
    }

    public long insertHouse(House house) {
        return appRepo.createHouse(house);
    }

    public int updateUser(User user) {
        return appRepo.updateUser(user);
    }

    public User loginUser(String userName, String password){
        return appRepo.loginUser(userName, password);
    }

    public Manager loginManager(String userName, String password){
        return appRepo.loginManager(userName, password);
    }
    public int updateManager(Manager manager) {
        return appRepo.updateManager(manager);
    }

    public int updateHouse(House house) {
        return appRepo.updateHouse(house);
    }

    public House getHouseByName(String title){
        return appRepo.getHouseByName(title);
    }
    public LiveData<List<House>> getAllHouse() {
        return appRepo.getAllHouse();
    }

    public LiveData<List<User>> getAllUser() {
        return appRepo.getAllUser();
    }

    public LiveData<List<Manager>> getAllManager() {
        return appRepo.getALlManager();
    }

    public LiveData<List<SubscribedHouses>> getAllSubscribedHouse() {
        return appRepo.getAllSubscribedUser();
    }

}
