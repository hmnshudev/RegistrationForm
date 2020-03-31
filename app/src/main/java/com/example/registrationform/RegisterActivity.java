package com.example.registrationform;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    private static final int STORAGE_REQUEST_CODE = 400;

    EditText editName, editEmail, editPassword, editMobile, editAddress, editConfirmPassword;
    Button buttonRegister;
    SQLiteHelper sqliteHelper;
    RadioGroup radioGender;
    CheckBox checkHindi, checkEnglish;
    CircleImageView editImage;
    Uri imageUri;
    String[] storagePermission;

    public static Spanned fromHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        sqliteHelper = new SQLiteHelper(this);
        initTextViewLogin();
        initViews();
        intiPermissions();
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkStoragePermission()) {
                    requestStoragePermission();
                } else {
                    pickGallery();
                }
            }
        });
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validate()) {
                    String UserName = editName.getText().toString();
                    String Email = editEmail.getText().toString();
                    String Password = editPassword.getText().toString();
                    String Mobile = editMobile.getText().toString();
                    String Address = editAddress.getText().toString();
                    String Gender = setGender();
                    String Language = setLanguage();
                    byte[] Profile = setImage();
                    DateFormat dateFormat = DateFormat.getDateTimeInstance();
                    String time = dateFormat.format(new Date());
                    if (!sqliteHelper.isEmailExists(Email)) {
                        sqliteHelper.addUser(new User(null, UserName, Email, Password, time, Mobile, Address, Gender, Language, Profile));
                        Toast.makeText(RegisterActivity.this, "User created successfully! Please Login", Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, Snackbar.LENGTH_LONG);
                    } else {
                        Toast.makeText(RegisterActivity.this, "User already exists with same email ", Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });
    }

    private byte[] setImage() {
        Bitmap image;
        try {
            image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            return stream.toByteArray();
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return new byte[0];
        }
    }

    private String setLanguage() {
        if (checkHindi.isChecked() && checkEnglish.isChecked()) {
            return "Hindi, English";
        } else if (checkHindi.isChecked()) {
            return "Hindi";
        } else if (checkEnglish.isChecked()) {
            return "English";
        } else {
            return null;
        }
    }

    private String setGender() {
        String gender = "";
        switch (radioGender.getCheckedRadioButtonId()) {
            case R.id.male:
                gender = "Male";
                break;
            case R.id.female:
                gender = "Female";
                break;
        }
        return gender;
    }

    private void initTextViewLogin() {
        TextView textViewLogin = findViewById(R.id.textViewLogin);
        textViewLogin.setText(fromHtml("<font color='#0c0099'>Back to Login?</font>"));
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initViews() {
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editConfirmPassword = findViewById(R.id.editConfirmPassword);
        editName = findViewById(R.id.editName);
        editMobile = findViewById(R.id.editMobile);
        editAddress = findViewById(R.id.editAddress);
        buttonRegister = findViewById(R.id.buttonRegister);
        radioGender = findViewById(R.id.editGender);
        checkHindi = findViewById(R.id.hindi);
        checkEnglish = findViewById(R.id.english);
        editImage = findViewById(R.id.editImage);
    }

    private void intiPermissions() {
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    public boolean validate() {
        boolean valid = false;
        String UserName = editName.getText().toString();
        String Email = editEmail.getText().toString();
        String Password = editPassword.getText().toString();
        String ConfirmPassword = editConfirmPassword.getText().toString();
        String Mobile = editMobile.getText().toString();
        String Address = editAddress.getText().toString();

        if (imageUri == null) {
            Toast.makeText(this, "Please Choose your Profile Picture", Toast.LENGTH_SHORT).show();
        }

        if (UserName.isEmpty()) {
            editName.setError("Please Enter Name!");
        } else {
            editName.setError(null);
        }

        if (Mobile.isEmpty()) {
            editMobile.setError("Please Enter Mobile Number");
        } else {
            if (Mobile.length() < 10) {
                editMobile.setError("Enter Valid 10 Digits Number!");
            } else {
                editMobile.setError(null);
            }
        }

        if (setLanguage() == null) {
            Toast.makeText(this, "Please choose at least One Language", Toast.LENGTH_SHORT).show();
        }

        if (Email.isEmpty()) {
            editEmail.setError("Please Enter Email");
        } else {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                editEmail.setError("Enter Valid Email!");
            } else {
                editEmail.setError(null);
            }
        }

        if (Password.isEmpty()) {
            editPassword.setError("Please Enter Password");
        } else {
            if (editPassword.length() <= 5) {
                editPassword.setError("Password length must be Greater than 5");
            } else {
                editPassword.setError(null);
            }
        }

        if (Address.isEmpty()) {
            editAddress.setError("Enter Valid Address!");
        } else {
            editAddress.setError(null);
        }

        if (!editPassword.equals(editConfirmPassword)) {
            editConfirmPassword.setError("Both Password must be Same!");
        } else {
            editConfirmPassword.setError(null);
        }

        if (!UserName.isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches() && !Address.isEmpty() && Mobile.length() == 10 && Password.length() > 5 && Password.equals(ConfirmPassword) && setLanguage() != null) {
            editName.setError(null);
            editMobile.setError(null);
            editEmail.setError(null);
            editPassword.setError(null);
            editConfirmPassword.setError(null);
            editAddress.setError(null);
            valid = true;

        }

        return valid;
    }

    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private void pickGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == STORAGE_REQUEST_CODE) {
            if (grantResults.length > 0) {
                boolean writeStorageAccepted = grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED;
                if (writeStorageAccepted) {
                    pickGallery();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED) {
            if (requestCode == PICK_IMAGE) {
                imageUri = data.getData();
                editImage.setImageURI(imageUri);
            } else {
                Toast.makeText(this, "Please select pic", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Request Cancelled", Toast.LENGTH_SHORT).show();
        }

    }
}