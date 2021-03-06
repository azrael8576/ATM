package com.alex.atm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alex.atm.UIkit.Function;
import com.alex.atm.R;
import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOGIN = 100;
    private static final String TAG = MainActivity.class.getSimpleName();
    boolean logon = false;
    private List<Function> functions;

    //    String [] functions = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (!logon){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivityForResult(intent,REQUEST_LOGIN);
//            startActivity(intent);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nickname = getSharedPreferences("user",MODE_PRIVATE)
                        .getString("NICKNAME",null);
                Snackbar.make(view, "Welcome "+ nickname + ".", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //Recycler
        setupFunctions();

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,3));
        //Adapter
//        FunctionAdapter adapter = new FunctionAdapter(this);
        IconAdapter adapter = new IconAdapter();
        recyclerView.setAdapter(adapter);


//        CrashTest();
    }

    private void CrashTest() {
        Button crashButton = new Button(this);
        crashButton.setText("Crash!");
        crashButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Crashlytics.getInstance().crash(); // Force a crash
            }
        });

        addContentView(crashButton, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private void setupFunctions() {
        functions = new ArrayList<>();
        String[] funes = getResources().getStringArray(R.array.functions);
        functions.add(new Function(funes[0],R.drawable.func_transaction));
        functions.add(new Function(funes[1],R.drawable.func_balance));
        functions.add(new Function(funes[2],R.drawable.func_finance));
        functions.add(new Function(funes[3],R.drawable.func_contacts));
        functions.add(new Function(funes[4],R.drawable.func_exit));
    }

    public class IconAdapter extends RecyclerView.Adapter<IconAdapter.IconHolder>{

        @NonNull
        @Override
        public IconHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = getLayoutInflater().inflate(R.layout.item_icon,viewGroup,false);
            return new IconHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull IconHolder iconHolder, int i) {
            final Function function = functions.get(i);
            iconHolder.nameText.setText(function.getName());
            iconHolder.iconImage.setImageResource(function.getIcon());
            iconHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClicked(function);
                }
            });
        }

        @Override
        public int getItemCount() {
            return functions.size();
        }

        public class IconHolder extends RecyclerView.ViewHolder{
            ImageView iconImage;
            TextView nameText;
            public IconHolder(@NonNull View itemView) {
                super(itemView);
                iconImage = itemView.findViewById(R.id.item_icon);
                nameText = itemView.findViewById(R.id.item_name);
            }
        }
    }

    private void itemClicked(Function function) {
        Log.d(TAG, "itemClicked: " + function.getName());
        switch (function.getIcon()){
            case R.drawable.func_transaction:
                startActivity(new Intent(this,TransActivity.class));
            break;
            case R.drawable.func_balance:
                break;
            case R.drawable.func_finance:
                Intent finance = new Intent(this,FinanceActivity.class);
                startActivity(finance);
                break;
            case R.drawable.func_contacts:
                Intent contacts = new Intent(this,ContactActivity.class);
                startActivity(contacts);
                break;
            case R.drawable.func_exit:
                finish();
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==REQUEST_LOGIN){
            if (resultCode == RESULT_OK){
                logon = true;
                String nick = getSharedPreferences("user",MODE_PRIVATE)
                        .getString("NICKNAME",null);
                int age = getSharedPreferences("user",MODE_PRIVATE)
                        .getInt("AGE",0);
                int gender = getSharedPreferences("user",MODE_PRIVATE)
                        .getInt("GENDER",0);
                if (nick == null || age == 0 || gender ==0) {
                    //進入Nickname Activity
                    Intent nickname = new Intent(this, NicknameActivity.class);
                    startActivity(nickname);
                }
            }
            else{
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
