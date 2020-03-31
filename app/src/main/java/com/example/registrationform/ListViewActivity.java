package com.example.registrationform;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ListViewActivity extends AppCompatActivity {
    SQLiteHelper db;
    ListView listView;
    FloatingActionButton fab;

    OTPFragment dialogFragment;
    FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        listView = findViewById(R.id.listView);
        fab = findViewById(R.id.fab);
        db = new SQLiteHelper(this);

        CustomAdapter customAdapter = new CustomAdapter(this, db.getAllUserInfo());
        listView.setAdapter(customAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDialog();
            }
        });

    }

    private void displayDialog() {
        dialogFragment = new OTPFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("notAlertDialog", true);
        dialogFragment.setArguments(bundle);
        ft = getSupportFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        dialogFragment.setCancelable(false);
        dialogFragment.show(ft, "dialog");
    }
}

