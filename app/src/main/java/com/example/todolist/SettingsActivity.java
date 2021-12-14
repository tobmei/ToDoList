package com.example.todolist;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSave;
    CheckBox cbCheck;
    SharedPreferences sharedPreferences;
    private static final String myPreferences = "settings";
    ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        btnSave = findViewById(R.id.btnSave);
        cbCheck = findViewById(R.id.cbCheck);

        btnSave.setOnClickListener(this);

        sharedPreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE);
        if(sharedPreferences.contains("Check"))
            cbCheck.setChecked(sharedPreferences.getBoolean("Check", false));

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == btnSave.getId()){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("Check", cbCheck.isChecked());
            editor.apply();
            this.finish();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            this.finish();
        return true;
    }
}