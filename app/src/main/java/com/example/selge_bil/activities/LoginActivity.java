package com.example.selge_bil.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.selge_bil.sellerOptions.MainActivity_Vendedor;
import com.example.selge_bil.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity {
    SharedPreferences mPref;
    TextInputEditText mTextInputEmail;
    TextInputEditText mTextInputPassword;
    Button mButtonLogin;



    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    AlertDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mTextInputEmail = findViewById(R.id.textInputEmail);
        mTextInputPassword = findViewById(R.id.textInputPassword);
        mButtonLogin = findViewById(R.id.btnLogin);

        mPref = getApplicationContext().getSharedPreferences("typeUser", MODE_PRIVATE);



        mDialog= new SpotsDialog.Builder().setContext(LoginActivity.this).setMessage("Espere un momento").build();


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }
    private void login() {
        String email = mTextInputEmail.getText().toString();
        String password = mTextInputPassword.getText().toString();
        if (!email.isEmpty() && !password.isEmpty()) {
            if (password.length() >= 6) {
                mDialog.show();
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() { //el meetodo indica que se hara cuando se termine de completa la tarea
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso!", Toast.LENGTH_SHORT).show();
                            String user = mPref.getString("user","");
                            if(user.equals("comprador")){
                                Intent intent = new Intent(LoginActivity.this, MainActivity_Vendedor.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK); //cuando se crea el usuario ya no puedes regresar a la pantalla de formulario de registro
                                startActivity(intent);
                            }
                            else{
                                Intent intent = new Intent(LoginActivity.this, MainActivity_Vendedor.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK); //cuando se crea el usuario ya no puedes regresar a la pantalla de formulario de registro
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "El email o la contraseña son incorrectos", Toast.LENGTH_SHORT).show();
                        }
                        mDialog.dismiss();
                    }
                });

            }else{
                Toast.makeText(LoginActivity.this, "La contraseña debe contener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(LoginActivity.this, "El email y la contraseña son obligatorios", Toast.LENGTH_SHORT).show();
        }
    }
}