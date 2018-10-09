package com.example.rcjoshi.chanakyafoundation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {

    Button mSignupbtn;
    EditText mNewUser,mNewPass,mConfirmPass,mAdminMob;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mSignupbtn = (Button) findViewById(R.id.signupbtn);
        mNewUser = (EditText) findViewById(R.id.setuserid);
        mNewPass = (EditText) findViewById(R.id.setpasswordid);
        mConfirmPass = (EditText) findViewById(R.id.cnfrmpasswordid);
        mAdminMob = (EditText) findViewById(R.id.adminmobid);

        mSignupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNewPass.getText().toString().equals("")||mConfirmPass.getText().toString().equals("")||
                        mNewUser.getText().toString().equals("")||mAdminMob.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please enter all fields", Toast.LENGTH_SHORT).show();
                }
                else if (! mNewPass.getText().toString().equals(mConfirmPass.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(), "Passwords not matching", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    SharedPreferences sd=getSharedPreferences("data", Context.MODE_PRIVATE);
                    SharedPreferences.Editor ed=sd.edit();
                    ed.putString("username",mNewUser.getText().toString());
                    ed.putString("mob",mAdminMob.getText().toString());
                    ed.putString("password",mNewPass.getText().toString());
                    ed.commit();
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    Intent iLogin = new Intent(SignUp.this,LoginActivity.class);
                    startActivity(iLogin);
                    finish();
                }
            }
        });
    }
}
