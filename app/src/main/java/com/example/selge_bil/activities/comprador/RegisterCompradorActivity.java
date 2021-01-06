package com.example.selge_bil.activities.comprador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.selge_bil.buyerOptions.MainActivity_Comprador;
import com.example.selge_bil.sellerOptions.MainActivity_Vendedor;
import com.example.selge_bil.R;
import com.example.selge_bil.models.Comprador;
import com.example.selge_bil.providers.AuthProvider;
import com.example.selge_bil.providers.CompradorProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import dmax.dialog.SpotsDialog;

public class RegisterCompradorActivity extends AppCompatActivity {
    SharedPreferences mPref;
    AuthProvider mAuthProvider;
    CompradorProvider mCompradorProvider;


    //VIEWS
    Button mButtonRegister;
    TextInputEditText mTextInputEmail;
    TextInputEditText mTextInputName;
    TextInputEditText mTextInputPassword;
    TextInputEditText mTextInputPaterno;
    TextInputEditText mTextInputMaterno;
    TextInputEditText mTextInputTelefono;
    TextInputEditText mTextInputDireccion;


    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_comprador);
        mAuthProvider = new AuthProvider();
        mCompradorProvider = new CompradorProvider();

        mDialog = new SpotsDialog.Builder().setContext(RegisterCompradorActivity.this).setMessage("Espere un momento").build();


        mPref = getApplicationContext().getSharedPreferences("typeUser", MODE_PRIVATE);

        mButtonRegister = findViewById(R.id.btnRegister);
        mTextInputName = findViewById(R.id.textInputName);
        mTextInputPassword = findViewById(R.id.textInputPassword);
        mTextInputEmail = findViewById(R.id.textInputEmail);
        mTextInputMaterno = findViewById(R.id.textInputMaterno);
        mTextInputPaterno = findViewById(R.id.textInputPaterno);
        mTextInputDireccion = findViewById(R.id.textInputDireccion);
        mTextInputTelefono = findViewById(R.id.textInputTelefono);


        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickRegister();
            }
        });
    }

    void clickRegister() {
        final String name = mTextInputName.getText().toString();
        final String email = mTextInputEmail.getText().toString();
        final String password = mTextInputPassword.getText().toString();
        final String paterno = mTextInputPaterno.getText().toString();
        final String materno = mTextInputMaterno.getText().toString();
        final String telefono = mTextInputTelefono.getText().toString();
        final String direccion = mTextInputDireccion.getText().toString();

        if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !materno.isEmpty() && !paterno.isEmpty() && !telefono.isEmpty() && !direccion.isEmpty()) {
            if (password.length() >= 6) {
                mDialog.show();
                register(name, email, password, paterno, materno, telefono, direccion);
            } else {
                Toast.makeText(this, "La contraseña debe contener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Ingrese todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    void register(final String name, final String email, String password, final String paterno, final String materno, final String telefono, final String direccion) {
        mAuthProvider.register(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mDialog.hide();
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name).build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                    }
                                }
                            });
                    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Comprador client = new Comprador(id, name, email, paterno, materno, telefono, direccion);
                    create(client);
                } else {
                    Toast.makeText(RegisterCompradorActivity.this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void create(Comprador client) {
        mCompradorProvider.create(client).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //Toast.makeText(RegisterActivity.this, "El registro se realizo exitosamente", Toast.LENGTH_SHORT).show();                }
                    Intent intent = new Intent(RegisterCompradorActivity.this, MainActivity_Comprador.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK); //cuando se crea el usuario ya no puedes regresar a la pantalla de formulario de registro
                    startActivity(intent);
                } else {
                    Toast.makeText(RegisterCompradorActivity.this, "No se pudo crear el Comprador", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}