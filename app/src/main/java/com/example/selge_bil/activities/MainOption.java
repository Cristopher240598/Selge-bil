package com.example.selge_bil.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.selge_bil.sellerOptions.MainActivity_Vendedor;
import com.example.selge_bil.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainOption extends AppCompatActivity
{
    Button mButtonIAmClient;
    Button getmButtonIAmDriver;

    SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_option);
        mPref = getApplicationContext().getSharedPreferences("typeUser", MODE_PRIVATE);
        final SharedPreferences.Editor editor = mPref.edit();

        mButtonIAmClient = findViewById(R.id.btnIAmComprador);
        getmButtonIAmDriver = findViewById(R.id.btnIAmVendedor);


        mButtonIAmClient.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                editor.putString("user", "comprador");
                editor.apply();
                goToSelectAuth();
            }
        });
        getmButtonIAmDriver.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                editor.putString("user", "vendedor");
                editor.apply();
                goToSelectAuth();
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            String user = mPref.getString("user", "");
            if (user.equals("comprador"))
            {
                Intent intent = new Intent(MainOption.this, MainActivity_Vendedor.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK); //cuando se crea el usuario ya no puedes regresar a la pantalla de formulario de registro
                startActivity(intent);
            } else
            {
                Intent intent = new Intent(MainOption.this, MainActivity_Vendedor.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK); //cuando se crea el usuario ya no puedes regresar a la pantalla de formulario de registro
                startActivity(intent);
            }
        }

    }

    private void goToSelectAuth()
    {
        Intent intent = new Intent(MainOption.this, SelectOptionAuthActivity.class);
        startActivity(intent);
    }
}