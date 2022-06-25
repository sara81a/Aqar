package com.myapp.aqar.database;

import static com.myapp.aqar.constants.AppConstants.DATABASE_NAME;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.myapp.aqar.dao.AppDAO;
import com.myapp.aqar.database.entities.House;
import com.myapp.aqar.database.entities.Manager;
import com.myapp.aqar.database.entities.SubscribedHouses;
import com.myapp.aqar.database.entities.User;

@Database(entities = {User.class, Manager.class, House.class, SubscribedHouses.class}, version = 1)
public abstract class AppDB extends RoomDatabase {

    public static AppDB instance;

    public static AppDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, AppDB.class, DATABASE_NAME).
                    allowMainThreadQueries().
                    fallbackToDestructiveMigration().build();
        }

        return instance;
    }

    public abstract AppDAO appDAO();


}