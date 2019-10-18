package com.example.walkin_clinic_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class WelcomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
    }


    public void onLogOut(View view){

        Intent returnIntent = new Intent();

        setResult(RESULT_OK, returnIntent);

        finish();
    }
}
