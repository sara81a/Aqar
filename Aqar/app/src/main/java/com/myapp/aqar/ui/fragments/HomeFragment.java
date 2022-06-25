package com.myapp.aqar.ui.fragments;

import static com.myapp.aqar.constants.AppConstants.MANAGER;
import static com.myapp.aqar.constants.AppConstants.USER;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myapp.aqar.LocationModel;
import com.myapp.aqar.R;
import com.myapp.aqar.adapter.HouseAdapter;
import com.myapp.aqar.appHelper.AppPreferenceHelper;
import com.myapp.aqar.database.entities.House;
import com.myapp.aqar.ui.HouseDetailedActivity;
import com.myapp.aqar.utils.Utils;
import com.myapp.aqar.viewmodel.AppViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeFragment extends Fragment {

    private static final int LOCATION_REQUEST_CODE = 222;
    AppViewModel viewModel;

    AppPreferenceHelper appPreferenceHelper;

    @BindView(R.id.house_recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.no_house_layout)
    ConstraintLayout noHouseLayout;

    @BindView(R.id.tv_createFirstHouse)
    TextView createFirstHouseBtn;

    @BindView(R.id.createNewHouse)
    ImageView createNewHouse;

    HouseAdapter adapter;

    View.OnClickListener subscrirbe;

    int currentUserID;

    DatabaseReference mLocationDatabaseRef;

    FusedLocationProviderClient fusedLocationProviderClient;
    String userIS;
    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult.getLastLocation() != null) {
                double latitude = locationResult.getLastLocation().getLatitude();
                double longitude = locationResult.getLastLocation().getLongitude();

                if (appPreferenceHelper.getUserIs().equals(MANAGER)) {
                    updateUserLocation(MANAGER, latitude, longitude);
                } else if (appPreferenceHelper.getUserIs().equals(USER))
                    updateUserLocation(USER, latitude, longitude);

            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        initUI();
        return view;
    }

    private void initUI() {
        viewModel = ViewModelProviders.of(this).get(AppViewModel.class);
        appPreferenceHelper = AppPreferenceHelper.getInstance(getContext());
        currentUserID = appPreferenceHelper.getCurrentUserID();
        mLocationDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Locations");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        userIS = appPreferenceHelper.getUserIs();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//        String userIs = appPreferenceHelper.getUserIs();

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location == null) {
                        requestForLocationUpdate();
                    } else {
                        updateUserLocation(userIS, location.getLatitude(), location.getLongitude());
                    }
                }
            });
        }else{
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        }

        if (userIS.equals(USER)) {
            createNewHouse.setVisibility(View.GONE);
            noHouseLayout.setVisibility(View.GONE);
            observeUserViewModel();
        } else if (userIS.equals(MANAGER)) {
            observeManagerViewModel();
        }


        createNewHouse.setOnClickListener(this::onCreateNewHouseClick);

        subscrirbe = v -> {
            House house = (House) v.getTag();
            house.setSubscribedUserID(currentUserID);
            int result = viewModel.updateHouse(house);
            if (result > 0) {
                Toast.makeText(getActivity(), "Subscribed ! You will be notified regarding this house.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Failed to subscribe the house !", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void checkForLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            requestForLocationUpdate();
        } else {
        }
    }

    private void requestForLocationUpdate() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private void updateUserLocation(String userType, double latitude, double longitude) {
        LocationModel currentLoc = new LocationModel(latitude, longitude);

        mLocationDatabaseRef.child(userType).child("" + currentUserID).setValue(currentLoc).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("TAG", "UserLocation Updated: ");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "onFailure: " + e.getMessage());
            }
        });
    }

    private void observeManagerViewModel() {
        createNewHouse.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        noHouseLayout.setVisibility(View.VISIBLE);
        viewModel.getAllHouse().observe(getActivity(), new Observer<List<House>>() {
            @Override
            public void onChanged(List<House> houses) {
                if (houses.size() > 0) {
                    List<House> myHouse = new ArrayList<>();
                    for (House house : houses) {
                        if (house.getOwnerID() == currentUserID)
                            myHouse.add(house);
                    }
                    if (myHouse.size() > 0) {
                        createNewHouse.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        noHouseLayout.setVisibility(View.GONE);
                    }
                    adapter = new HouseAdapter(myHouse, getActivity(), MANAGER, subscrirbe);
                    recyclerView.setAdapter(adapter);
                }
            }
        });


    }

    private void observeUserViewModel() {
        viewModel.getAllHouse().observe(getActivity(), new Observer<List<House>>() {
            @Override
            public void onChanged(List<House> houses) {
                if (houses.size() > 0) {
                    List<House> noSubscribedHouse = new ArrayList<>();
                    List<House> subscribedHouse = new ArrayList<>();

                    for (House house : houses) {
                        if (house.getSubscribedUserID() != currentUserID)
                            noSubscribedHouse.add(house);
                        else
                            subscribedHouse.add(house);
                    }

                    adapter = new HouseAdapter(noSubscribedHouse, getActivity(), USER, subscrirbe);
                    recyclerView.setAdapter(adapter);

                    checkForNearLocation(subscribedHouse);
                    adapter.setmClickListener((view, position) -> {

                    });
                    noHouseLayout.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void checkForNearLocation(List<House> subscribedHouse) {
        mLocationDatabaseRef.child(USER).child("" + currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    LocationModel model = snapshot.getValue(LocationModel.class);
                    for (House house : subscribedHouse) {
                        Log.d("TAG", "onDataChange: " + house.getLatitude() + ", " + house.getLongitude() + ", " + model.getLatitude() + ", " + model.getLongitude());
                        double distanceInMiles = distance(house.getLatitude(), house.getLongitude(), model.getLatitude(), model.getLongitude());
                        if (distanceInMiles < 2) {
                            String notificationMessage = "Hi, We found you near to your subscribed house, Come, Let's have a look.";
                            Utils.displayNotification(getActivity(), house.getTitle(), notificationMessage);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @OnClick(R.id.tv_createFirstHouse)
    void onCreateNewHouseClick(View view) {
        Intent intent = new Intent(getActivity(), HouseDetailedActivity.class);
        intent.putExtra("comingFrom", "FirstHome");
        startActivity(intent);
    }

    private double distance(double hLat, double hLong, double ULat, double ULong) {
        double theta = hLong - ULong;
        double dist = Math.sin(deg2rad(hLat))
                * Math.sin(deg2rad(ULat))
                + Math.cos(deg2rad(hLat))
                * Math.cos(deg2rad(ULat))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            requestForLocationUpdate();
        } else {
            Toast.makeText(getActivity(), "Permission Denied !", Toast.LENGTH_SHORT).show();
        }
    }
}