package com.example.registrationform;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    EditText email, password;
    Button Login;
    Button User;

    SQLiteHelper sqliteHelper;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sqliteHelper = new SQLiteHelper(this);
        initCreateAccountTextView();
        initViews();

        User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent UserIntent = new Intent(MainActivity.this, ListViewActivity.class);
                startActivity(UserIntent);

            }
        });


        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validate()) {
                    String Email = email.getText().toString();
                    String Password = password.getText().toString();

                    User currentUser = sqliteHelper.Authenticate(new User(null, null, Email, Password, null, null, null, null, null,null));

                    if (currentUser != null) {
                        Snackbar.make(Login, "Successfully Logged in!", Snackbar.LENGTH_LONG).show();
                        startActivity(new Intent(MainActivity.this, UserProfile.class).putExtra("user", currentUser));
                        finish();
                    } else {
                        Snackbar.make(Login, "Failed to log in , please try again", Snackbar.LENGTH_LONG).show();

                    }
                }

            }

        });

        User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteHelper db = new SQLiteHelper(MainActivity.this);
                int size = db.getAllUserInfo().size();
                if (size > 0) {
                    Intent UserIntent = new Intent(MainActivity.this, ListViewActivity.class);
                    startActivity(UserIntent);
                } else {
                    Toast.makeText(MainActivity.this, "The User List is Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void initCreateAccountTextView() {
        TextView textViewCreateAccount = findViewById(R.id.textViewCreateAccount);
        textViewCreateAccount.setText(fromHtml("<font color='#0c0099'>I don't have Account yet. </font><font color='#0c0099'>Create One?</font>"));
        textViewCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initViews() {
        email = findViewById(R.id.editEmail);
        password = findViewById(R.id.editPassword);
        Login = findViewById(R.id.buttonLogin);
        User = findViewById(R.id.buttonUser);

    }

    //This method is used to validate input given by user
    public boolean validate() {
        boolean valid;

        String Email = email.getText().toString();
        String Password = password.getText().toString();

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            email.setError("Please enter valid email!");
        } else {

            email.setError(null);
        }

        if (Password.isEmpty()) {
            valid = false;
            password.setError("Please enter valid password!");
        } else {
            if (Password.length() > 5) {
                valid = true;
                password.setError(null);
            } else {
                valid = false;
                password.setError("Password is to short!");
            }
        }

        return valid;
    }
}
