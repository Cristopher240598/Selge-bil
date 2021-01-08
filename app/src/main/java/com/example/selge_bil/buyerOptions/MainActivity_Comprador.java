package com.example.selge_bil.buyerOptions;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.selge_bil.R;
import com.example.selge_bil.activities.MainOption;
import com.example.selge_bil.activities.comprador.EditInfo;
import com.example.selge_bil.providers.AuthProvider;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;

import dmax.dialog.SpotsDialog;

public class MainActivity_Comprador extends AppCompatActivity{

    private AppBarConfiguration mAppBarConfiguration;
    public static String filter;
    AuthProvider mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
        {
            System.out.println("No tiene el permiso ----------------------->");
        }
//        shouldShowRequestPermissionRationale( Manifest.permission.ACCESS_COARSE_LOCATION);
//        requestPermissions(getApplicationContext(),
//                Manifest.permission.ACCESS_COARSE_LOCATION);
        setContentView(R.layout.activity_main_buyer);
        Toolbar toolbar = findViewById(R.id.toolbar_buyer);
        setSupportActionBar(toolbar);
        mAuth = new AuthProvider();



        FirebaseApp.initializeApp(this);
        final DrawerLayout drawer = findViewById(R.id.drawer_layout_buyer);
        NavigationView navigationView = findViewById(R.id.nav_view_buyer);
        View headerView = navigationView.getHeaderView(0);

        if(mAuth.existSession()){
            TextView tvName=  headerView.findViewById(R.id.tV_usuario_nav_buyer);
            tvName.setText(mAuth.getName());
            Toast.makeText(this, mAuth.getName(), Toast.LENGTH_LONG).show();
        }

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_convertible,
                R.id.nav_coupe,
                R.id.nav_electrico,
                R.id.nav_hatchback,
                R.id.nav_hibrido,
                R.id.nav_pickUp,
                R.id.nav_sedan,
                R.id.nav_suv,
                R.id.nav_utilitario,
                R.id.nav_van,
                R.id.nav_manual_buyer)
                .setDrawerLayout(drawer)
                .build();


        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_buyer);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        setFilter("Convertible");
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
                if (handled) {
                    switch (item.getItemId()) {
                        case R.id.nav_convertible:
                            setFilter("Convertible");
                            break;
                        case R.id.nav_coupe:
                            setFilter("Coupé");
                            break;
                        case R.id.nav_electrico:
                            setFilter("Eléctrico");
                            break;
                        case R.id.nav_hatchback:
                            setFilter("Hatchback");
                            break;
                        case R.id.nav_hibrido:
                            setFilter("Híbrido");
                            break;
                        case R.id.nav_pickUp:
                            setFilter("Pick Up");
                            break;
                        case R.id.nav_sedan:
                            setFilter("Sedán");
                            break;
                        case R.id.nav_suv:
                            setFilter("SUV");
                            break;
                        case R.id.nav_utilitario:
                            setFilter("Utalitario");
                            break;
                        case R.id.nav_van:
                            setFilter("Van");
                            break;
//                        case R.id.nav_manual_buyer:
//                            setCurrentOption(0);
//                            break;
//                        default:
//                            setCurrentOption(-1);
//                            break;
                    }
                }

//                NavigationUI.onNavDestinationSelected(item,navController);
                drawer.closeDrawer(GravityCompat.START);
                return handled;
            }
        });

    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_buyer);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    public static String getFilter() {
        return filter;
    }

    public static void setFilter(String filter) {
        MainActivity_Comprador.filter = filter;
    }

    public void logOut(View view) {

        AlertDialog mDialog;
        mDialog = new SpotsDialog.Builder().setContext(MainActivity_Comprador.this).setMessage("Cerrando sesion").build();
        mDialog.show();
        mAuth.logout();
        mDialog.hide();
        startActivity(new Intent(MainActivity_Comprador.this, MainOption.class));
        finish();
    }
    public void editInfo(View view) {


        startActivity(new Intent(MainActivity_Comprador.this, EditInfo.class));

    }

}