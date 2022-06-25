package com.myapp.aqar.ui;

import static com.myapp.aqar.constants.AppConstants.MANAGER;
import static com.myapp.aqar.constants.AppConstants.USER;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;
import com.myapp.aqar.MainActivity;
import com.myapp.aqar.R;
import com.myapp.aqar.appHelper.AppPreferenceHelper;
import com.myapp.aqar.database.entities.Manager;
import com.myapp.aqar.database.entities.User;
import com.myapp.aqar.viewmodel.AppViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.et_username)
    TextInputEditText et_UserName;

    @BindView(R.id.et_password)
    TextInputEditText et_password;

    @BindView(R.id.notHaveAccount)
    TextView notHaveAccount;

    @BindView(R.id.tv_loginBtn)
    TextView loginBtn;

    AppViewModel viewModel;

    AppPreferenceHelper appPreferenceHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initUI();
    }

    private void initUI() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        viewModel = ViewModelProviders.of(this).get(AppViewModel.class);
        appPreferenceHelper = AppPreferenceHelper.getInstance(getApplicationContext());

        if(appPreferenceHelper.getLoggedInfo()){
            Log.d("TAG", "LoggedInInfo login True: ");
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }
        loginBtn.setOnClickListener(this::onClick);
        notHaveAccount.setOnClickListener(this::onNotHaveAccountClick);
    }


    @OnClick(R.id.tv_loginBtn)
    void onClick(View view){
        String userName = et_UserName.getText().toString();
        String password= et_password.getText().toString();

        if(userName.equals("") || password.equals("")){
            Toast.makeText(getApplicationContext(), "Empty Fields not acceptable !", Toast.LENGTH_SHORT).show();
            return;
        }else{
            Manager manager = viewModel.loginManager(userName,password);
            if(manager == null){
                User user = viewModel.loginUser(userName,password);
                if(user == null){
                    Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }else{
                    appPreferenceHelper.setLoggedInInfo(true);
                    appPreferenceHelper.setUserIs(USER);
                    appPreferenceHelper.setCurrentUserID(user.getUser_id());
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("comingFor",USER);
                    startActivity(intent);
                }
            }else{
                appPreferenceHelper.setLoggedInInfo(true);
                appPreferenceHelper.setUserIs(MANAGER);
                appPreferenceHelper.setCurrentUserID(manager.getManagerID());

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("comingFor",MANAGER);
                startActivity(intent);
            }
        }
    }

    @OnClick(R.id.notHaveAccount)
    void onNotHaveAccountClick(View view){
        startActivity(new Intent(LoginActivity.this,SignupActivity.class));
        finish();
    }
}