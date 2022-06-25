package com.myapp.aqar.utils;

import static com.myapp.aqar.constants.AppConstants.CHANNEL_DESCRIPTTON;
import static com.myapp.aqar.constants.AppConstants.CHANNEL_ID;
import static com.myapp.aqar.constants.AppConstants.CHANNEL_NAME;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.myapp.aqar.R;
import com.myapp.aqar.ui.HouseInformationActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Utils {


    public static String getLocationText(Activity activity,double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        String s = null;
        try {
            List<Address> addressList = geocoder.getFromLocation(latitude,longitude,1);
            if(addressList.get(0) != null){
                String address = addressList.get(0).getAddressLine(0);
                String city = addressList.get(0).getLocality();
                String country = addressList.get(0).getCountryName();

                s = address+", "+city+", "+country;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return s;
    }

    public static void displayNotification(Activity activity, String houseTitle, String notificationMessage) {
        createNotificationChannel(activity);
        Intent notificationIntent = new Intent(activity, HouseInformationActivity.class);
        notificationIntent.putExtra("Title", houseTitle);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_subscribe)
                .setContentTitle(activity.getString(R.string.app_name))
                .setContentText(notificationMessage)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(activity);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, builder.build());
    }

    public static  void createNotificationChannel(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = CHANNEL_NAME;
            String description = CHANNEL_DESCRIPTTON;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after activity
            NotificationManager notificationManager = activity.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
