package com.example.rishikumar.m_share;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

/**
 * Created by rishi.kumar on 11/21/2017.
 */

public class SignUporLogInActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_or_login);
        Button  login = (Button) findViewById(R.id.login);
        Button  signup = (Button) findViewById(R.id.signup);
        login.setOnClickListener(this);
        signup.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {


            case R.id.signup:
                Intent startSignUp = new Intent(SignUporLogInActivity.this, SignUpActivity.class);
                startActivity(startSignUp);
                finish();

                break;

            case R.id.login:
                Intent startLogin = new Intent(SignUporLogInActivity.this, LoginActivity.class);
                startActivity(startLogin);
                finish();

                break;

        }






    }
}
