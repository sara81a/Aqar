package com.myapp.aqar.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.myapp.aqar.LocationModel;
import com.myapp.aqar.R;
import com.myapp.aqar.appHelper.AppPreferenceHelper;
import com.myapp.aqar.database.entities.House;
import com.myapp.aqar.utils.Utils;
import com.myapp.aqar.viewmodel.AppViewModel;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HouseDetailedActivity extends AppCompatActivity {

    private static final int IMAGE_UPLOAD_REQUEST_CODE = 111;
    private static final int LOCATION_REQUEST_CODE = 222;
    @BindView(R.id.homeImage)
    ImageView homeImage;

    @BindView(R.id.uploadImageLayout)
    LinearLayout uploadImageLayout;

    @BindView(R.id.img_uploadImage)
    ImageView img_uploadImage;

    @BindView(R.id.et_houseName)
    EditText houseName;

    @BindView(R.id.et_houseLocation)
    EditText houseLocation;

    @BindView(R.id.et_housePrice)
    EditText housePrice;

    @BindView(R.id.tv_createHomebtn)
    TextView createHomeBtn;

    AppViewModel viewModel;
    AppPreferenceHelper appPreferenceHelper;

    int currentUserID;

    Uri imageURI;

    double latitude;
    double longitude;

    DatabaseReference housesLocationRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_detailed);
        ButterKnife.bind(this);

        initUI();
    }

    private void initUI() {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        viewModel = ViewModelProviders.of(this).get(AppViewModel.class);
        appPreferenceHelper = AppPreferenceHelper.getInstance(getApplicationContext());
        housesLocationRef = FirebaseDatabase.getInstance().getReference().child("house");
        currentUserID = appPreferenceHelper.getCurrentUserID();

        homeImage.setVisibility(View.GONE);

        img_uploadImage.setOnClickListener(this::onImageUploadClick);

        houseLocation.setOnClickListener(this::onSetHouseLocation);
        createHomeBtn.setOnClickListener(this::onClick);

        onNewIntent(getIntent());
    }

    @OnClick(R.id.tv_createHomebtn)
    void onClick(View view) {
        String hName = houseName.getText().toString();
        String hLocation = houseLocation.getText().toString();
        String hPrice = housePrice.getText().toString();

        if (hName.equals("") || hLocation.equals("") || hPrice.equals("")) {
            Toast.makeText(getApplicationContext(), "Empty Fields not acceptable", Toast.LENGTH_SHORT).show();
            return;
        } else if (imageURI == null) {
            Toast.makeText(getApplicationContext(), "You haven't uploaded the image", Toast.LENGTH_SHORT).show();
            return;
        } else {

            InputStream iStream = null;
            try {
                iStream = getContentResolver().openInputStream(imageURI);
                byte[] image = getBytes(iStream);

                House house = new House(hName, Integer.parseInt(hPrice), image, latitude, longitude, currentUserID);
                long result = viewModel.insertHouse(house);

                if (result > 0) {
                    int hID = viewModel.getHouseByName(hName).getHouseID();
                    housesLocationRef.child("" + hID).setValue(new LocationModel(latitude, longitude)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), "House Created Successfully !", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(HouseDetailedActivity.this, LoginActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("TAG", "onFailure: " + e.getMessage());
                        }
                    });
                } else {
                    Log.d("TAG", "onClick: " + result);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.img_uploadImage)
    void onImageUploadClick(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_UPLOAD_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_UPLOAD_REQUEST_CODE && resultCode == RESULT_OK) {
            uploadImageLayout.setVisibility(View.GONE);
            homeImage.setVisibility(View.VISIBLE);
            homeImage.setImageURI(data.getData());
            imageURI = data.getData();

        } else if (requestCode == LOCATION_REQUEST_CODE) {
            if (data.getExtras() != null) {
                latitude = data.getExtras().getDouble("latitude");
                longitude = data.getExtras().getDouble("longitude");
                houseLocation.setText(Utils.getLocationText(HouseDetailedActivity.this, latitude, longitude));
            } else {
                return;
            }
        }
    }


    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    @OnClick(R.id.et_houseLocation)
    void onSetHouseLocation(View view) {
        Intent intent = new Intent(HouseDetailedActivity.this, MapsActivity.class);
        startActivityForResult(intent, LOCATION_REQUEST_CODE);
    }

}