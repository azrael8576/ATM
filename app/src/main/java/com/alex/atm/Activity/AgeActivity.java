package com.alex.atm.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.alex.atm.R;

public class AgeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age);
    }
    public void next(View view){
        EditText ageEdit = findViewById(R.id.age);
        int age = Integer.parseInt(ageEdit.getText().toString());
        getSharedPreferences("user",MODE_PRIVATE)
                .edit()
                .putInt("AGE",age)
                .apply();
        Intent gender = new Intent(this,GenderActivity.class);
        startActivity(gender);
    }
}
