package com.alex.atm.Activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.alex.atm.Bean.HelloService;
import com.alex.atm.Fragment.NewsFragment;
import com.alex.atm.R;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int REQUEST_CODE_CAMERA = 5;
    private EditText edUserid;
    private EditText edPasswd;
    private CheckBox cbRemember;
    private Intent helloService;
    private String passwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Fragment
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.add(R.id.container_news, NewsFragment.getInstance());
        fragmentTransaction.commit();
        //Service
        helloService = new Intent(this,HelloService.class);
        helloService.putExtra("NAME","T1");
        startService(helloService);
        helloService.putExtra("NAME","T2");
        startService(helloService);
        helloService.putExtra("NAME","T3");
        startService(helloService);
//        camera();
//        settingTest();
        findViews();
        new TestTask().execute("http://google.com.tw");

    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: Hello" + intent.getAction());
        }
    };

    public void map (View view){
        startActivity(new Intent(this,MapsActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(HelloService.Companion.getACTION_HELLO_DONE());
        registerReceiver(receiver,filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopService(helloService);
        unregisterReceiver(receiver);
    }

    public class TestTask extends AsyncTask<String, Void, Integer>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "onPreExecute: ");
            Toast.makeText(LoginActivity.this,"onPreExecute",Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            Log.d(TAG, "onPostExecute: ");
            Toast.makeText(LoginActivity.this,"onPostExecute:" + integer,Toast.LENGTH_LONG).show();
        }

        @Override
        protected Integer doInBackground(String... strings) {
            int data = 0;
            try {
                URL url = new URL(strings[0]);
                data = url.openStream().read();
                Log.d(TAG, "TestTask: " + data);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }
    }

    private void settingTest() {
        getSharedPreferences("atm",MODE_PRIVATE)
                .edit()
                .putInt("LEVEL",3)
                .putString("NAME","Alex")
                .commit();
        int level = getSharedPreferences("atm",MODE_PRIVATE)
                .getInt("LEVEL",0);
        Log.d(TAG, "onCreate:" + level);
    }

    private void camera() {
        int permission = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA);
        //詢問是否允許權限
        if (permission == PackageManager.PERMISSION_GRANTED){
            takePhoto();
        }else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},REQUEST_CODE_CAMERA);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==REQUEST_CODE_CAMERA){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                takePhoto();
            }
        }
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivity(intent);
    }

    private void findViews() {
        edUserid = findViewById(R.id.userid);
        edPasswd = findViewById(R.id.passwd);
        cbRemember = findViewById(R.id.cd_rem_userid);
        cbRemember.setChecked(
                getSharedPreferences("atm",MODE_PRIVATE)
                    .getBoolean("REMEMBER_USERID",false));
        cbRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getSharedPreferences("atm",MODE_PRIVATE)
                        .edit()
                        .putBoolean("REMEMBER_USERID",isChecked)
                        .apply();
            }
        });
        String userid = getSharedPreferences("atm",MODE_PRIVATE)
                .getString("USERID","");
        edUserid.setText(userid);
    }

    public void login(View view){
        final String userid = edUserid.getText().toString();
        final String passwd = edPasswd.getText().toString();

        FirebaseDatabase.getInstance().getReference("users").child(userid).child("password")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override

                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String pw = (String) dataSnapshot.getValue();
                        if (pw == null)
                            pw = "";
                        if (!"".equals(passwd) && !"".equals(userid) && pw.equals(passwd)){
//                            boolean remember = getSharedPreferences("atm", MODE_PRIVATE)
//                                    .getBoolean("REMEMBER_USERID", false);
                            boolean remember = true;
                            if (remember) {
                                //save userid
                                getSharedPreferences("atm", MODE_PRIVATE)
                                        .edit()
                                        .putString("USERID", userid)
                                        .apply();
                            }
                            setResult(RESULT_OK);
                            finish();
                        }
                        else {
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle(R.string.login_failed)
                                    .setMessage(R.string.login_failed_info)
                                    .setPositiveButton(R.string.ok,null)
                                    .show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }
    public void signup(View view){
        final String userid = edUserid.getText().toString();
        final String passwd = edPasswd.getText().toString();
        new AlertDialog.Builder(LoginActivity.this)
                .setTitle(R.string.signup)
                .setMessage("帳號: " + userid + " 是否註冊?")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference("users")
                                .child(userid).child("password").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String pw = (String) dataSnapshot.getValue();
                                        if (pw == null && passwd !=""){
                                            FirebaseDatabase.getInstance().getReference("users")
                                                    .child(userid).child("password").setValue(passwd);
                                            new AlertDialog.Builder(LoginActivity.this)
                                                    .setTitle(R.string.reg_success)
                                                    .setPositiveButton(R.string.ok,null)
                                                    .show();
                                        }
                                        else {
                                            new AlertDialog.Builder(LoginActivity.this)
                                                    .setTitle(R.string.sign_up_failed)
                                                    .setMessage(R.string.sign_up_failed_info)
                                                    .setPositiveButton(R.string.ok,null)
                                                    .show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                    }
                })
                .setNegativeButton(R.string.cancel,null)
                .show();
    }
}
