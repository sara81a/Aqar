package com.myapp.aqar.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.myapp.aqar.R;
import com.myapp.aqar.database.entities.House;
import com.myapp.aqar.utils.Utils;
import com.myapp.aqar.viewmodel.AppViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HouseInformationActivity extends AppCompatActivity {

    @BindView(R.id.home_img)
    ImageView homeImage;
    @BindView(R.id.home_title)
    TextView homeTitle;
    @BindView(R.id.home_location)
    TextView homeLocation;
    @BindView(R.id.home_price)
    TextView homePrice;

    AppViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_information);
        ButterKnife.bind(this);

        viewModel = ViewModelProviders.of(this).get(AppViewModel.class);
        onNewIntent(getIntent());
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Bundle extras = intent.getExtras();
        if (extras != null) {
            String name = extras.getString("Title");
            if (name.equals("")) {

            } else {
                House house = viewModel.getHouseByName(name);

                homeTitle.setText(house.getTitle());
                homePrice.setText("$ "+house.getPrice());

                Bitmap bmp = BitmapFactory.decodeByteArray(house.getImage(), 0, house.getImage().length);
                homeImage.setImageBitmap(bmp);

                String loc = Utils.getLocationText(HouseInformationActivity.this, house.getLatitude(), house.getLongitude());
                homeLocation.setText(loc);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
    }
}