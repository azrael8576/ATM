package com.alex.atm;

import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {

    private EditText edDate;
    private EditText edInfo;
    private EditText edAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        findViews();
    }

    private void findViews() {
        edDate = findViewById(R.id.ed_date);
        edInfo = findViewById(R.id.ed_info);
        edAmount = findViewById(R.id.ed_amount);
    }

    public void add(View view){
        String date = edDate.getText().toString();
        String info = edInfo.getText().toString();
        int amount = 0;
        if (!"".equals(edAmount.getText().toString())){
            amount = Integer.parseInt(edAmount.getText().toString());
            ExpenseHelper helper = new ExpenseHelper(this);
            ContentValues values = new ContentValues();
            values.put("cdate",date);
            values.put("info",info);
            values.put("amount",amount);
            long id = helper.getWritableDatabase()
                    .insert("expense",null,values);
            if (id > -1){
                Toast.makeText(this,getString(R.string.success),Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(this,getString(R.string.failed),Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(this,getString(R.string.failed),Toast.LENGTH_LONG).show();
        }
    }
}
