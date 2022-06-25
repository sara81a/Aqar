package com.myapp.aqar.ui;

import static com.myapp.aqar.constants.AppConstants.MANAGER;
import static com.myapp.aqar.constants.AppConstants.USER;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

public class SignupActivity extends AppCompatActivity {

    @BindView(R.id.userType)
    AutoCompleteTextView userType;

    @BindView(R.id.et_email)
    TextInputEditText email;

    @BindView(R.id.et_name)
    TextInputEditText name;

    @BindView(R.id.et_contact)
    TextInputEditText contactNum;

    @BindView(R.id.et_password)
    TextInputEditText password;

    @BindView(R.id.tv_signupBtn)
    TextView signupBtn;

    @BindView(R.id.tv_alreadyHaveAccount)
    TextView alreadyHaveAccount;

    AppViewModel viewModel;

    String userIs = "";

    AppPreferenceHelper appPreferenceHelper;

    String users[] = {USER, MANAGER};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        initUI();
    }

    private void initUI() {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        viewModel = ViewModelProviders.of(this).get(AppViewModel.class);
        appPreferenceHelper = AppPreferenceHelper.getInstance(getApplicationContext());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, users);
        userType.setAdapter(adapter);

        userType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                userIs = parent.getItemAtPosition(position).toString();
            }
        });

        signupBtn.setOnClickListener(this::onSignupBtnClick);
    }


    @OnClick(R.id.tv_signupBtn)
    void onSignupBtnClick(View view) {
        String userName = name.getText().toString();
        String userEmail = email.getText().toString();
        String phoneNumber = contactNum.getText().toString();
        String userPassword = password.getText().toString();

        if (userName.equals("") || userEmail.equals("") || phoneNumber.equals("") || userPassword.equals("")) {
            Toast.makeText(getApplicationContext(), "Empty fields not acceptable", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (userIs.equals("")) {
                Toast.makeText(getApplicationContext(), "Select User Type", Toast.LENGTH_SHORT).show();
                return;
            } else {
                if (userIs.equals(USER)) {
                    createAccountForUser(userName, userEmail, phoneNumber, userPassword);
                } else if (userIs.equals(MANAGER)) {
                    createAccountForManager(userName, userEmail, phoneNumber, userPassword);
                }
            }
        }

        alreadyHaveAccount.setOnClickListener(this::onAlreadyHaveAnAccountClick);
    }

    private void createAccountForManager(String userName, String userEmail, String phoneNumber, String userPassword) {
        long result = viewModel.insertManager(new Manager(userName, userEmail, userPassword, phoneNumber));
        if (result > 0) {
            Manager manager = viewModel.loginManager(userName, userPassword);
            appPreferenceHelper.setLoggedInInfo(true);
            if (manager != null) {
                appPreferenceHelper.setCurrentUserID(manager.getManagerID());
                appPreferenceHelper.setUserIs(userIs);
            }
            startActivity(new Intent(SignupActivity.this, MainActivity.class));
        } else {
            Toast.makeText(getApplicationContext(), "Creating account failed!", Toast.LENGTH_SHORT).show();
        }
    }

    private void createAccountForUser(String userName, String userEmail, String phoneNumber, String userPassword) {
        long result = viewModel.insertUser(new User(userName, userEmail, userPassword, phoneNumber));
        if (result > 0) {
            User user = viewModel.loginUser(userName, userPassword);
            appPreferenceHelper.setLoggedInInfo(true);

            if (user != null) {
                appPreferenceHelper.setCurrentUserID(user.getUser_id());
                appPreferenceHelper.setUserIs(userIs);
            }
            startActivity(new Intent(SignupActivity.this, MainActivity.class));
        } else {
            Toast.makeText(getApplicationContext(), "Creating account failed!", Toast.LENGTH_SHORT).show();
        }
    }


    @OnClick(R.id.tv_alreadyHaveAccount)
    void onAlreadyHaveAnAccountClick(View view) {
        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        finish();
    }
}