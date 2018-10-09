package com.example.rcjoshi.chanakyafoundation;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    Button mLogin;
    EditText mUser,mPassword;
    TextView mForgotPw,mSignUptxt;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLogin = (Button) findViewById(R.id.loginbtn);
        mUser = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mForgotPw = (TextView) findViewById(R.id.forgotpwid);
        mSignUptxt = (TextView) findViewById(R.id.signuptxtid);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getName,getPassword;
                SharedPreferences sd=getSharedPreferences("data", Context.MODE_PRIVATE);
                getName = sd.getString("username","");
                getPassword = sd.getString("password","");
                if (mUser.getText().toString().equals("")||mPassword.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please enter all fields", Toast.LENGTH_SHORT).show();
                }
                else if (mUser.getText().toString().equals(getName) && mPassword.getText().toString().equals(getPassword))
                {
                    Intent iStart = new Intent(LoginActivity.this,AdminActivity.class);
                    startActivity(iStart);
                    finish();
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Incorrect Password or Username", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mSignUptxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iSignuppg = new Intent(LoginActivity.this,SignUp.class);
                startActivity(iSignuppg);
                finish();
            }
        });

        mForgotPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(LoginActivity.this,new String[]{Manifest.permission.SEND_SMS},7);
                    return;
                }
                String getMob,getPassword,getName;
                SharedPreferences sd=getSharedPreferences("data", Context.MODE_PRIVATE);
                getMob = sd.getString("mob","");
                getName = sd.getString("username","");
                getPassword = sd.getString("password","");
                SmsManager sms=SmsManager.getDefault();
                String text = "Your Password for username "+getName+" is: "+getPassword;
                if(getMob!="")
                {
                    sms.sendTextMessage(getMob,null,text,null,null);
                    Toast.makeText(LoginActivity.this, "Your password details are sent to your registered no", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "No account created", Toast.LENGTH_SHORT).show();
                    Toast.makeText(LoginActivity.this, "Create new admin account", Toast.LENGTH_SHORT).show();
                    Intent iSignuppg = new Intent(LoginActivity.this,SignUp.class);
                    startActivity(iSignuppg);
                    finish();
                }

            }
        });
    }
}
