package com.skeleton.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;


import com.bumptech.glide.Glide;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.skeleton.R;
import com.skeleton.constant.AppConstant;
import com.skeleton.retrofit.APIError;
import com.skeleton.retrofit.ApiInterface;
import com.skeleton.retrofit.MultipartParams;
import com.skeleton.retrofit.ResponseResolver;
import com.skeleton.retrofit.RestClient;
import com.skeleton.util.ValidateEditText;

import com.skeleton.util.imagepicker.ImageChooser;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.RequestBody;


/**
 * created by Rishab
 */
public class SignUpActivity extends AppCompatActivity {

    private ImageView ivImage;
    private EditText tvName, tvContactNo, tvEmail, tvDOB, tvPassword, tvCpassword;
    private CheckBox cbTOS;
    private Button btnSignUp;
    private ImageChooser imageChooser;
    private File imageFile;
    private int checkedId;
    private RadioGroup rgGender;
    private int mGender;
    private String mOrientation = "Gay";
    private Date date;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                imageChooser = new ImageChooser(new ImageChooser.Builder(SignUpActivity.this));
                imageChooser.selectImage(new ImageChooser.OnImageSelectListener() {
                    @Override
                    public void loadImage(final List<ChosenImage> list) {
                        imageFile = new File(list.get(0).getOriginalPath());
                        if (imageFile != null) {
                            Glide.with(SignUpActivity.this)
                                    .load(list.get(0).getQueryUri())
                                    .into(ivImage);

                        }

                    }

                    @Override
                    public void croppedImage(final File mCroppedImage) {

                    }
                });
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                checkedId = rgGender.getCheckedRadioButtonId();
                checkGender();
                if (ValidateEditText.checkName(tvName, true)
                        && ValidateEditText.checkEmail(tvEmail)
                        && ValidateEditText.checkPassword(tvPassword, false)
                        && ValidateEditText.checkPassword(tvCpassword, true)
                        && ValidateEditText.comparePassword(tvPassword, tvCpassword)
                        && ValidateEditText.checkPhoneNumber(tvContactNo)
                        && (checkDOB(tvDOB))) {

                    Log.d("debug", "all valide");
                    HashMap<String, RequestBody> multipartParams = new MultipartParams.Builder()
                            .add(AppConstant.KEY_FRAGMENT_FNAME, tvName.getText().toString())
                            .add(AppConstant.KEY_FRAGMENT_LNAME, tvName.getText().toString())
                            .add(AppConstant.KEY_FRAGMENT_DOB, tvDOB.getText().toString())
                            .add(AppConstant.KEY_FRAGMENT_COUNTRY_CODE, AppConstant.VALUE_PHONE)
                            .add(AppConstant.KEY_FRAGMENT_PHONE, tvContactNo.getText().toString())
                            .add(AppConstant.KEY_FRAGMENT_EMAIL, tvEmail.getText().toString())
                            .add(AppConstant.KEY_FRAGMENT_PASSWORD, tvPassword.getText().toString())
                            .add(AppConstant.KEY_FRAGMENT_LANGUAGE, AppConstant.VALUE_FRAGMENT_LANGUAGE)
                            .add(AppConstant.KEY_FRAGMENT_DEVICE_TYPE, AppConstant.VALUE_FRAGMENT_DEVICE_TYPE)
                            .add(AppConstant.KEY_FRAGMENT_DEVICE_TOKEN, AppConstant.VALUE_RAGMENT_DEVICE_TOKEN)
                            .add(AppConstant.KEY_FRAGMENT_APP_VERSION, AppConstant.VALUE_FRAGMENT_APP_VERSION)
                            .add(AppConstant.KEY_FRAGMENT_GENDER, mGender)
                            .add(AppConstant.KEY_FRAGMENT_ORIENTATION, mOrientation)
                            .add(AppConstant.KEY_FRAGMENT_PROFILE_PIC, imageFile).build().getMap();

                    ApiInterface apiInterface = RestClient.getApiInterface();
                    apiInterface.userRegister(multipartParams)
                            .enqueue(new ResponseResolver<com.skeleton.model.Response>(SignUpActivity.this, true, true) {
                                @Override
                                public void success(final com.skeleton.model.Response response) {
                                    Log.d("debug", "success: " + response.getStatusCode());
                                    Log.d("debug", "success: " + response.getStatusCode());

                                    if ("200".equals(response.getStatusCode().toString())) {

                                        clearEditText(tvName, tvDOB, tvCpassword,
                                                tvEmail, tvPassword, tvContactNo);
                                        Intent intent = new Intent(SignUpActivity.this, DisplayResponseActivity.class);
                                        intent.putExtra("response", response);
                                        startActivity(intent);

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
     * deletes previous
     *
     * @param editText eit texts
     */

    public void clearEditText(final EditText... editText) {
        for (EditText editText1 : editText) {
            editText1.setText("");

        }
    }

    /**
     * init
     */

    public void init() {
        ivImage = (ImageView) findViewById(R.id.display);
        tvName = (EditText) findViewById(R.id.et_fname);
        tvContactNo = (EditText) findViewById(R.id.et_contact);
        tvEmail = (EditText) findViewById(R.id.et_email);
        tvDOB = (EditText) findViewById(R.id.et_DOB);
        tvPassword = (EditText) findViewById(R.id.et_password);
        tvCpassword = (EditText) findViewById(R.id.et_cpassword);
        cbTOS = (CheckBox) findViewById(R.id.tos);
        btnSignUp = (Button) findViewById(R.id.btn_signup);
        rgGender = (RadioGroup) findViewById(R.id.rgGender);

    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        imageChooser.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        imageChooser.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    /**
     * finds which option is selected in radio group
     */
    private void checkGender() {
        Log.d("debug", String.valueOf(checkedId));
        if (checkedId == -1) {
            Log.d("debug", "no data");
        } else if (checkedId == R.id.rbMale) {
            mGender = 0;
            Log.d("debug", "0");
        } else {
            mGender = 1;
            Log.d("debug", "1");
        }

    }

    /**
     * to check dob
     *
     * @param editText dob
     * @return boolean
     */
    private boolean checkDOB(final EditText editText) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String s = editText.getText().toString();
        try {
            date = dateFormat.parse(s);
            Log.d("debug", "valid date");
            return true;

        } catch (ParseException e) {
            editText.setError(getString(R.string.error_invalid_data));
            Log.d("debug", "invalid date");
            return false;
        }
    }
}

