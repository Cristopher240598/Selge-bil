package com.example.selge_bil.sellerOptions;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.selge_bil.R;
import com.example.selge_bil.activities.MainOption;
import com.example.selge_bil.activities.comprador.EditInfo;
import com.example.selge_bil.activities.vendedor.EditInfoVendedor;
import com.example.selge_bil.activities.vendedor.RegisterVendedorActivity;
import com.example.selge_bil.buyerOptions.MainActivity_Comprador;
import com.example.selge_bil.providers.AuthProvider;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import dmax.dialog.SpotsDialog;

public class MainActivity_Vendedor extends AppCompatActivity
{

    private AppBarConfiguration mAppBarConfiguration;
    TextView tvUsuario, tvCorreo;
    Button btnLogout;
    AuthProvider mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_seller);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = new AuthProvider();

        FirebaseApp.initializeApp(this);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        if(mAuth.existSession()) {
            tvUsuario = headerView.findViewById(R.id.tV_usuario_nav);
            tvUsuario.setText(mAuth.getName());
            tvCorreo = headerView.findViewById(R.id.tvCorreoVendedor);
            tvCorreo.setText(mAuth.getEmail());
            btnLogout= headerView.findViewById(R.id.btnLogout);
            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog mDialog;
                    mDialog = new SpotsDialog.Builder().setContext(MainActivity_Vendedor.this).setMessage("Cerrando sesion").build();
                    mDialog.show();
                    mAuth.logout();
                    mDialog.hide();
                    startActivity(new Intent(MainActivity_Vendedor.this, MainOption.class));
                    finish();
                }
            });

        }
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_mycars, R.id.nav_sellcar, R.id.nav_sold, R.id.nav_manual, R.id.nav_updateDeleteCar)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    public void editInfo(View view) {


        startActivity(new Intent(MainActivity_Vendedor.this, EditInfoVendedor.class));

    }

}
