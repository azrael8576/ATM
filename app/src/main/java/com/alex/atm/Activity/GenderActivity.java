package com.alex.atm.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.alex.atm.R;

public class GenderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender);
    }
    public void next(View view){
        EditText genderEdit = findViewById(R.id.gender);
        int gender = Integer.parseInt(genderEdit.getText().toString());
        getSharedPreferences("user",MODE_PRIVATE)
                .edit()
                .putInt("GENDER",gender)
                .apply();
        Intent main = new Intent(this,MainActivity.class);
        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(main);
    }
}
