package com.example.selge_bil.buyerOptions;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.example.selge_bil.R;
import com.example.selge_bil.activities.LoginActivity;
import com.example.selge_bil.activities.MainOption;
import com.example.selge_bil.buyerOptions.convertible.convertible;
import com.example.selge_bil.providers.AuthProvider;
import com.example.selge_bil.sellerOptions.MainActivity_Vendedor;
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
        setContentView(R.layout.activity_main_buyer);
        Toolbar toolbar = findViewById(R.id.toolbar_buyer);
        setSupportActionBar(toolbar);
        mAuth = new AuthProvider();

        FirebaseApp.initializeApp(this);
        final DrawerLayout drawer = findViewById(R.id.drawer_layout_buyer);
        NavigationView navigationView = findViewById(R.id.nav_view_buyer);

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

}