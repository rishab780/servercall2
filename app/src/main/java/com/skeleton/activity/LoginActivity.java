package com.skeleton.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.skeleton.R;
import com.skeleton.constant.AppConstant;
import com.skeleton.retrofit.APIError;
import com.skeleton.retrofit.ApiInterface;
import com.skeleton.retrofit.CommonParams;
import com.skeleton.retrofit.CommonResponse;
import com.skeleton.retrofit.ResponseResolver;
import com.skeleton.retrofit.RestClient;
import com.skeleton.util.ValidateEditText;

import java.util.HashMap;

/**
 * created by Rishab
 */
public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = (Button) findViewById(R.id.btn_login);
        init();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                init();
                if (validate()) {
                    HashMap<String, String> hashMap = new CommonParams.Builder()
                            .add(AppConstant.KEY_FRAGMENT_EMAIL, etEmail.getText().toString())
                            .add(AppConstant.KEY_FRAGMENT_PASSWORD, etPassword.getText().toString())
                            .add(AppConstant.KEY_FRAGMENT_DEVICE_TYPE, AppConstant.VALUE_FRAGMENT_DEVICE_TYPE)
                            .add(AppConstant.KEY_FRAGMENT_LANGUAGE, AppConstant.VALUE_FRAGMENT_LANGUAGE)
                            .add(AppConstant.KEY_FRAGMENT_DEVICE_TOKEN, AppConstant.VALUE_RAGMENT_DEVICE_TOKEN)
                            .add(AppConstant.KEY_FRAGMENT_FLUSH_PREVIOUS_SESSIOINS, AppConstant.VALUE_FRAGMENT_FLUSH_PREVIOUS_SESSIOINS)
                            .add(AppConstant.KEY_FRAGMENT_APP_VERSION, AppConstant.VALUE_FRAGMENT_APP_VERSION).build().getMap();
                    ApiInterface apiInterface = RestClient.getApiInterface();
                    apiInterface.userLogin(null, hashMap).enqueue(new ResponseResolver<CommonResponse>(LoginActivity.this, false, false) {
                        @Override
                        public void success(final CommonResponse commonResponse) {
                            if ("200".equals(commonResponse.getStatusCode())) {
                                Log.d("debug", "accEss ALLOWED");


                            }
                        }

                        @Override
                        public void failure(final APIError error) {

                        }




                        });
                }
            }
        });
    }


    /**
     * main function to start
     */
    public void init() {
        etEmail = (EditText) findViewById(R.id.et_lemail);
        etPassword = (EditText) findViewById(R.id.et_lpassword);
    }

    /**
     *
     * @return returns true or false
     */
    public Boolean validate() {
        if (ValidateEditText.checkEmail(etEmail)
            && ValidateEditText.checkPassword(etPassword, false)) {
            return true;
        }
        return false;

    }

}
