package com.example.selge_bil.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.selge_bil.R;
import com.example.selge_bil.activities.comprador.RegisterCompradorActivity;
import com.example.selge_bil.activities.vendedor.RegisterVendedorActivity;

public class SelectOptionAuthActivity extends AppCompatActivity {
    SharedPreferences mPref;

    Button mButonGoToLogin;
    Button mButonGoToRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_option_auth);
        mPref = getApplicationContext().getSharedPreferences("typeUser", MODE_PRIVATE);

        mButonGoToLogin = findViewById(R.id.btnGoToLogin);
        mButonGoToRegister = findViewById(R.id.btnGoToRegister);

        mButonGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLogin();
            }
        });
        mButonGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRegister();
            }
        });
    }
    public void goToLogin() {
        Intent intent= new Intent(SelectOptionAuthActivity.this,LoginActivity.class);
        startActivity(intent);
    }
    public void goToRegister() {
        String typeUser = mPref.getString("user","");
        if(typeUser.equals("comprador")){

            Intent intent= new Intent(SelectOptionAuthActivity.this, RegisterCompradorActivity.class);
            startActivity(intent);
        }
        else{
            Intent intent= new Intent(SelectOptionAuthActivity.this, RegisterVendedorActivity.class);
            startActivity(intent);
        }
    }
}