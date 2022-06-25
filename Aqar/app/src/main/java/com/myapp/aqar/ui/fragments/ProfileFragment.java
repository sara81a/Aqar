package com.myapp.aqar.ui.fragments;

import static com.myapp.aqar.constants.AppConstants.MANAGER;
import static com.myapp.aqar.constants.AppConstants.USER;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.myapp.aqar.MainActivity;
import com.myapp.aqar.R;
import com.myapp.aqar.appHelper.AppPreferenceHelper;
import com.myapp.aqar.database.entities.House;
import com.myapp.aqar.database.entities.Manager;
import com.myapp.aqar.database.entities.User;
import com.myapp.aqar.ui.LoginActivity;
import com.myapp.aqar.ui.UserSubscibedActivity;
import com.myapp.aqar.viewmodel.AppViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ProfileFragment extends Fragment {

    private static final int ON_DO_NOT_DISTURB_CALLBACK_CODE = 123;
    @BindView(R.id.img_logoutBtn)
    ImageView logoutBtn;

    @BindView(R.id.et_userName)
    EditText userName;

    @BindView(R.id.et_userEmail)
    EditText userEmail;

    @BindView(R.id.et_userPassword)
    EditText userPassword;

    @BindView(R.id.et_userContactNum)
    EditText userContactNum;

    @BindView(R.id.tv_updateProfile)
    TextView tv_updateProfile;

    @BindView(R.id.subscribeTxt)
    TextView subscribedHouses;

    @BindView(R.id.subscribedHouseCard)
    CardView subscribedHouseCard;

    @BindView(R.id.switch_silentMode)
    Switch silentModeSwitch;

    AppViewModel viewModel;
    AppPreferenceHelper appPreferenceHelper;

    String userIS = "";
    int updateUserID;

    AudioManager audioManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        initUI();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("WrongConstant")
    private void initUI() {
        viewModel = ViewModelProviders.of(this).get(AppViewModel.class);
        appPreferenceHelper = AppPreferenceHelper.getInstance(getContext());

        userIS = appPreferenceHelper.getUserIs();
        if (userIS.equals(USER)) {
            obserUsersViewModel(appPreferenceHelper.getCurrentUserID());
        } else if (userIS.equals(MANAGER)) {
            observeManagerViewModel(appPreferenceHelper.getCurrentUserID());
        }

        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        requestForDoNotDisturbPermissionOrSetDoNotDisturb();

        if (audioManager.getMode() == AudioManager.RINGER_MODE_NORMAL) {
            silentModeSwitch.setChecked(false);
        } else if (audioManager.getMode() == AudioManager.RINGER_MODE_SILENT) {
            silentModeSwitch.setChecked(true);
        }
        silentModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                } else {
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                }
            }
        });


        logoutBtn.setOnClickListener(this::onLogoutBtnClick);
        tv_updateProfile.setOnClickListener(this::onUpdateProfileClick);
        subscribedHouses.setOnClickListener(this::onSubscribedCard);
    }

    private void obserUsersViewModel(int userID) {
        subscribedHouseCard.setVisibility(View.VISIBLE);
        viewModel.getAllUser().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                for (User user : users) {
                    if (user.getUser_id() == userID) {
                        updateUserID = user.getUser_id();
                        userEmail.setText(user.getEmail());
                        userName.setText(user.getUserName());
                        userPassword.setText(user.getPassword());
                        userContactNum.setText(user.getPhoneNumber());
                    }
                }
            }
        });
    }

    private void observeManagerViewModel(int managerID) {
        subscribedHouseCard.setVisibility(View.GONE);
        viewModel.getAllManager().observe(getViewLifecycleOwner(), new Observer<List<Manager>>() {
            @Override
            public void onChanged(List<Manager> managers) {
                for (Manager manager : managers) {
                    if (manager.getManagerID() == managerID) {
                        updateUserID = manager.getManagerID();
                        userEmail.setText(manager.getEmail());
                        userName.setText(manager.getName());
                        userPassword.setText(manager.getPassword());
                        userContactNum.setText(manager.getPhoneNumber());
                    }
                }
            }
        });
    }

    @OnClick(R.id.tv_updateProfile)
    void onUpdateProfileClick(View v) {
        String name = userName.getText().toString();
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();
        String contact = userContactNum.getText().toString();

        if (userIS.equals(MANAGER)) {
            Manager manager = new Manager(name, email, password, contact);
            manager.setManagerID(updateUserID);
            int result = viewModel.updateManager(manager);
            if (result > 0) {
                Toast.makeText(getActivity(), "Account Updated Successfully !", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            } else {
                Toast.makeText(getActivity(), "Failed! Couldn't update account", Toast.LENGTH_SHORT).show();
            }
        } else if (userIS.equals(USER)) {
            User user = new User(name, email, password, contact);
            user.setUser_id(updateUserID);
            int result = viewModel.updateUser(user);

            if (result > 0) {
                Toast.makeText(getActivity(), "Account Updated Successfully !", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            } else {
                Toast.makeText(getActivity(), "Failed! Couldn't update account", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @OnClick(R.id.img_logoutBtn)
    void onLogoutBtnClick(View view) {
        appPreferenceHelper.setUserIs("");
        appPreferenceHelper.setCurrentUserID(0);
        appPreferenceHelper.setLoggedInInfo(false);
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }

    @OnClick(R.id.subscribeTxt)
    void onSubscribedCard(View view) {
        viewModel.getAllHouse().observe(this, new Observer<List<House>>() {
            @Override
            public void onChanged(List<House> houses) {
                List<House> subscribedHouse = new ArrayList<>();
                for (House house : houses) {
                    if (house.getSubscribedUserID() == appPreferenceHelper.getCurrentUserID()) {
                        subscribedHouse.add(house);
                    }
                }

                if (subscribedHouse.size() > 0) {
                    startActivity(new Intent(getActivity(), UserSubscibedActivity.class));
                } else {
                    Toast.makeText(getActivity(), "You haven't subscribed to any house yet !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestForDoNotDisturbPermissionOrSetDoNotDisturb() {

        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        // if user granted access else ask for permission
        if (notificationManager.isNotificationPolicyAccessGranted()) {
            AudioManager audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        } else {
            // Open Setting screen to ask for permisssion
            Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivityForResult(intent, ON_DO_NOT_DISTURB_CALLBACK_CODE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ON_DO_NOT_DISTURB_CALLBACK_CODE) {
            this.requestForDoNotDisturbPermissionOrSetDoNotDisturb();
        }
    }
}