package com.example.selge_bil.activities.vendedor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.selge_bil.R;

import com.example.selge_bil.models.Vendedor;
import com.example.selge_bil.models.Vendedor;
import com.example.selge_bil.providers.AuthProvider;
import com.example.selge_bil.providers.VendedorProvider;
import com.example.selge_bil.sellerOptions.MainActivity_Vendedor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import dmax.dialog.SpotsDialog;

public class EditInfoVendedor extends AppCompatActivity {
    Button mButtonActualizar;
    TextInputEditText mTextInputEmail;
    TextInputEditText mTextInputName;
    TextInputEditText mTextInputPassword;
    TextInputEditText mTextInputPaterno;
    TextInputEditText mTextInputMaterno;
    TextInputEditText mTextInputTelefono;
    TextInputEditText mTextInputDireccion;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    VendedorProvider mVendedorProvider;
    boolean bandera;
    AuthProvider mAuth;
    Vendedor vendedorSelected;
    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialog = new SpotsDialog.Builder().setContext(EditInfoVendedor.this).setMessage("Espere un momento").build();
        mAuth = new AuthProvider();
        setContentView(R.layout.activity_register_comprador);
        mVendedorProvider= new VendedorProvider();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Users").child("Vendedores");

        mButtonActualizar = findViewById(R.id.btnRegister);
        mTextInputName = findViewById(R.id.textInputName);
        mTextInputPassword = findViewById(R.id.textInputPassword);
        mTextInputEmail = findViewById(R.id.textInputEmail);
        mTextInputMaterno = findViewById(R.id.textInputMaterno);
        mTextInputPaterno = findViewById(R.id.textInputPaterno);
        mTextInputDireccion = findViewById(R.id.textInputDireccion);
        mTextInputTelefono = findViewById(R.id.textInputTelefono);

        mTextInputPassword.setVisibility(View.GONE);
        ImageView img= findViewById(R.id.imgViewPass);
        img.setVisibility(View.GONE);
        mButtonActualizar.setText("Actualizar");
        mTextInputEmail.setEnabled(false);

        if (mAuth.existSession()) {
            Query query = databaseReference.orderByChild("email").equalTo(mAuth.getEmail());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                            vendedorSelected = objSnapshot.getValue(Vendedor.class);
                            mTextInputName.setText(vendedorSelected.getName());
                            mTextInputEmail.setText(vendedorSelected.getEmail());
                            mTextInputMaterno.setText(vendedorSelected.getApMaterno());
                            mTextInputPaterno.setText(vendedorSelected.getApPaterno());
                            mTextInputDireccion.setText(vendedorSelected.getDomicilio());
                            mTextInputTelefono.setText(vendedorSelected.getTelefono());

                        }


                    } else {
                        if (EditInfoVendedor.this != null) {

                            Toast.makeText(EditInfoVendedor.this, "No se han encontrado resultados", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            mButtonActualizar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String name = mTextInputName.getText().toString();
                    final String email = mTextInputEmail.getText().toString();

                    final String paterno = mTextInputPaterno.getText().toString();
                    final String materno = mTextInputMaterno.getText().toString();
                    final String telefono = mTextInputTelefono.getText().toString();
                    final String direccion = mTextInputDireccion.getText().toString();
                    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    if (!name.isEmpty() && !email.isEmpty()  && !materno.isEmpty() && !paterno.isEmpty() && !telefono.isEmpty() && !direccion.isEmpty()) {

                        mDialog.show();
                        Vendedor client = new Vendedor(id, name, email, paterno, materno, telefono, direccion);
                        create(client);

                    } else {
                        Toast.makeText(EditInfoVendedor.this, "Ingrese todos los campos", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

        // Toast.makeText(getContext(), query+"query", Toast.LENGTH_SHORT).show();
        // Toast.makeText(getContext(), "Mensaje", Toast.LENGTH_SHORT).show();


    }
    void create(Vendedor client) {

        mVendedorProvider.updateVendedor(client).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDialog.hide();
                if (task.isSuccessful()) {
                    //Toast.makeText(RegisterActivity.this, "El registro se realizo exitosamente", Toast.LENGTH_SHORT).show();                }
                    Intent intent = new Intent(EditInfoVendedor.this, MainActivity_Vendedor.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK); //cuando se crea el usuario ya no puedes regresar a la pantalla de formulario de registro
                    startActivity(intent);
                } else {
                    Toast.makeText(EditInfoVendedor.this, "No se pudo actualizar el Vendedor", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}