package com.example.registrationform;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;

import java.io.ByteArrayInputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {
    private static final int CALL_REQUEST_CODE = 400;

    Button logout, call;
    TextView userName, userMobile, userEmail, userAddress, userGender, userLanguage;
    User currentUser;
    CircleImageView userImage;

    SharedPreferences mPrefs;
    SQLiteHelper sqliteHelper;

    String[] callPermission;

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = (User) getIntent().getSerializableExtra("user");
        if (currentUser == null && validate()) {
            startActivity(new Intent(UserProfile.this, MainActivity.class));
            finish();
        } else {
            saveLogin();
            setUserDetails();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        sqliteHelper = new SQLiteHelper(this);
        initViews();
        intiPermissions();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPrefs = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                prefsEditor.remove("MyObject");
                prefsEditor.apply();
                startActivity(new Intent(UserProfile.this, MainActivity.class));
                finish();
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkCallPermission()) {
                    requestCallPermission();
                } else {
                    makeCall();
                }

            }
        });
    }

    private boolean checkCallPermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) == (PackageManager.PERMISSION_GRANTED);
    }

    private void requestCallPermission() {
        ActivityCompat.requestPermissions(this, callPermission, CALL_REQUEST_CODE);
    }

    private void intiPermissions() {
        callPermission = new String[]{Manifest.permission.CALL_PHONE};
    }

    private void makeCall() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + currentUser.getMobile()));
        startActivity(intent);
    }

    private void saveLogin() {
        mPrefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(currentUser);
        prefsEditor.putString("MyObject", json);
        prefsEditor.apply();
    }

    private void setUserDetails() {
        userName.setText(getString(R.string.userName, currentUser.getUserName()));
        userMobile.setText(getString(R.string.userMobile, currentUser.getMobile()));
        userEmail.setText(getString(R.string.userEmail, currentUser.getEmail()));
        userAddress.setText(getString(R.string.userAddress, currentUser.getAddress()));
        userGender.setText(getString(R.string.userGender, currentUser.getGender()));
        userLanguage.setText(getString(R.string.userLanguage, currentUser.getLanguage()));
        userImage.setImageBitmap(decodeImage());
    }

    private Bitmap decodeImage() {
        byte[] outImage = currentUser.getImage();
        ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
        return BitmapFactory.decodeStream(imageStream);
    }

    private void initViews() {
        logout = findViewById(R.id.buttonLogout);
        userName = findViewById(R.id.userName);
        userMobile = findViewById(R.id.userMobile);
        userEmail = findViewById(R.id.userEmail);
        userAddress = findViewById(R.id.userAddress);
        userGender = findViewById(R.id.userGender);
        userLanguage = findViewById(R.id.userLanguage);
        userImage = findViewById(R.id.userImage);
        call = findViewById(R.id.buttonCall);
    }

    private boolean validate() {
        Gson gson = new Gson();
        mPrefs = getPreferences(MODE_PRIVATE);
        String json = mPrefs.getString("MyObject", null);
        currentUser = gson.fromJson(json, User.class);
        return currentUser == null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == CALL_REQUEST_CODE) {
            if (grantResults.length > 0) {
                boolean callPermissionAccepted = grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED;
                if (callPermissionAccepted) {
                    makeCall();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

}
