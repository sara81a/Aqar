package com.myapp.aqar.ui;

import static com.myapp.aqar.constants.AppConstants.MANAGER;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.myapp.aqar.R;
import com.myapp.aqar.adapter.HouseAdapter;
import com.myapp.aqar.appHelper.AppPreferenceHelper;
import com.myapp.aqar.database.entities.House;
import com.myapp.aqar.database.entities.SubscribedHouses;
import com.myapp.aqar.viewmodel.AppViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserSubscibedActivity extends AppCompatActivity {

    @BindView(R.id.userSubscribedHouses)
    RecyclerView recyclerView;

    AppViewModel viewModel;
    AppPreferenceHelper appPreferenceHelper;

    HouseAdapter adapter;
    View.OnClickListener subsribed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_subscibed);
        ButterKnife.bind(this);
        initUI();
    }

    private void initUI() {
        viewModel = ViewModelProviders.of(this).get(AppViewModel.class);
        appPreferenceHelper = AppPreferenceHelper.getInstance(getApplicationContext());

        int currentUserID = appPreferenceHelper.getCurrentUserID();

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        viewModel.getAllHouse().observe(this, new Observer<List<House>>() {
            @Override
            public void onChanged(List<House> houses) {
                List<House> subscribedHouses = new ArrayList<>();

                for(House house: houses){
                    if(house.getSubscribedUserID() == currentUserID)
                        subscribedHouses.add(house);
                }
                adapter = new HouseAdapter(subscribedHouses,UserSubscibedActivity.this,MANAGER,subsribed);
                recyclerView.setAdapter(adapter);
            }
        });
        /*

        viewModel.getAllSubscribedHouse().observe(this, new Observer<List<SubscribedHouses>>() {
            @Override
            public void onChanged(List<SubscribedHouses> subscribedHouses) {
                List<Integer> houseIDs = new ArrayList<>();
                for(SubscribedHouses houses1: subscribedHouses){
                    if(houses1.getUserID() == currentUserID){
                        houseIDs.add(houses1.getHouseID());
                    }
                }
                
                viewModel.getAllHouse().observe(UserSubscibedActivity.this, new Observer<List<House>>() {
                    @Override
                    public void onChanged(List<House> houses) {
                       List<House> subscribedHouse = new ArrayList<>();
                       for(int i=0; i<houseIDs.size();i++){
                           for(House house: houses){
                               if(house.getHouseID() == houseIDs.get(i))
                                   subscribedHouse.add(house);
                           }
                       }
                        adapter = new HouseAdapter(subscribedHouse,UserSubscibedActivity.this,MANAGER,subsribed);
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        });

         */
    }
}