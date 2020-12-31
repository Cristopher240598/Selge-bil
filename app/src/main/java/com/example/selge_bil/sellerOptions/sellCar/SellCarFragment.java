package com.example.selge_bil.sellerOptions.sellCar;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.selge_bil.R;
import com.example.selge_bil.models.Carro;
import com.example.selge_bil.providers.AuthProvider;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static androidx.core.content.ContextCompat.getSystemService;


public class SellCarFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener,
        AdapterView.OnItemSelectedListener
{
    private Spinner spnMarcas, spnTransmisiones, spnTiposCarro, spnAnios, spnCombustibles, spnNoPasajeros;
    private String marca, transmision, tipoCarro, anio, combustible, noPasajeros, imagen = "", currentPath;
    private ImageView iVFoto;
    public static final int REQUEST_TAKE_PHOTO = 1;
    private Uri photoURI;
    private EditText eTModelo, eTPrecio, eTTraccion, eTKilometraje;
    private Button btnLimpiar, btnAgregar;
    private GoogleMap map;
    private final static int LOCATION_REQUEST_CODE = 1;
    private final static int SETTINGS_REQUEST_CODE = 2;
    double latitud, longitud;
    private FusedLocationProviderClient fusedLocationClient;
    //private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private AuthProvider authProvider;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_sellcar, container, false);
        //Referencia del mapFragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //Localización
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        //Base de datos
        baseDatos();
        //Campos del formulario
        componentes(root);
        return root;
    }

    private void baseDatos()
    {
        authProvider = new AuthProvider();
        FirebaseApp.initializeApp(getContext());

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Cars");

        storageReference = FirebaseStorage.getInstance().getReference();
    }

    private void componentes(View root)
    {
        editTextComponentes(root);
        botones(root);
        spinnerComponentes(root);
    }

    private void editTextComponentes(View root)
    {
        eTModelo = (EditText) root.findViewById(R.id.tIET_modelo_registrar);
        eTPrecio = (EditText) root.findViewById(R.id.tIET_precio_registrar);
        eTTraccion = (EditText) root.findViewById(R.id.tIET_traccion_registrar);
        eTKilometraje = (EditText) root.findViewById(R.id.tIET_kilometraje_registrar);
    }

    private void botones(View root)
    {
        iVFoto = root.findViewById(R.id.iV_foto_registrar);
        btnLimpiar = root.findViewById(R.id.btn_limpiar_registrar);
        btnAgregar = root.findViewById(R.id.btn_agregar_registrar);

        iVFoto.setOnClickListener((View.OnClickListener) this);
        btnLimpiar.setOnClickListener((View.OnClickListener) this);
        btnAgregar.setOnClickListener((View.OnClickListener) this);
    }

    private void spinnerComponentes(View root)
    {
        ArrayAdapter<CharSequence> marcasAdapter, transmisionesAdpater, tiposCarroAdapter, aniosAdapter, combustiblesAdapter, noPasajerosAdapter;
        marcasAdapter = ArrayAdapter.createFromResource(getContext(), R.array.marcas, android.R.layout.simple_spinner_item);
        transmisionesAdpater = ArrayAdapter.createFromResource(getContext(), R.array.transmisiones, android.R.layout.simple_spinner_item);
        tiposCarroAdapter = ArrayAdapter.createFromResource(getContext(), R.array.tiposCarro, android.R.layout.simple_spinner_item);
        aniosAdapter = ArrayAdapter.createFromResource(getContext(), R.array.anios, android.R.layout.simple_spinner_item);
        combustiblesAdapter = ArrayAdapter.createFromResource(getContext(), R.array.combustibles, android.R.layout.simple_spinner_item);
        noPasajerosAdapter = ArrayAdapter.createFromResource(getContext(), R.array.noPasajeros, android.R.layout.simple_spinner_item);
        spnMarcas = (Spinner) root.findViewById(R.id.spn_marca_registrar);
        spnMarcas.setAdapter(marcasAdapter);
        spnTransmisiones = (Spinner) root.findViewById(R.id.spn_transmision_registrar);
        spnTransmisiones.setAdapter(transmisionesAdpater);
        spnTiposCarro = (Spinner) root.findViewById(R.id.spn_tipoCarro_registrar);
        spnTiposCarro.setAdapter(tiposCarroAdapter);
        spnAnios = (Spinner) root.findViewById(R.id.spn_anio_registrar);
        spnAnios.setAdapter(aniosAdapter);
        spnAnios.setSelection(80);
        spnCombustibles = (Spinner) root.findViewById(R.id.spn_combustible_registrar);
        spnCombustibles.setAdapter(combustiblesAdapter);
        spnNoPasajeros = (Spinner) root.findViewById(R.id.spn_noPasajeros_registrar);
        spnNoPasajeros.setAdapter(noPasajerosAdapter);

        spnMarcas.setOnItemSelectedListener(this);
        spnTransmisiones.setOnItemSelectedListener(this);
        spnTiposCarro.setOnItemSelectedListener(this);
        spnAnios.setOnItemSelectedListener(this);
        spnCombustibles.setOnItemSelectedListener(this);
        spnNoPasajeros.setOnItemSelectedListener(this);
    }
    private boolean gpsActived(){ //si tiene el gps activo

        boolean isActive= false;
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){ //comprobar si tiene el gps activado
            isActive= true;

        }
        return isActive;
    }
    private void showAlertDialogNOGPS(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Por favor activa tu ubicación para continuart")
                .setPositiveButton("Configuraciones", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),SETTINGS_REQUEST_CODE);
                    }
                }).create().show();
    }
    private void checkLocationPermissions(){
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)){
                new AlertDialog.Builder(getContext())
                        .setTitle("Proporciona los permisos para continuar")
                        .setMessage("Esta aplicación requiere de los permisos de ubicación para poder utilizarse")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {//Habilita permisos para ubicacion
                                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST_CODE);

                            }
                        })
                        .create()
                        .show();
            }
            else{
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST_CODE);
            }
        }

    }
    public void fusedLocation(){

    }
    //Mostrar mapa con ubicación actual
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        map = googleMap;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                if(gpsActived()){
                    fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>()
                    {
                        @Override
                        public void onSuccess(Location location)
                        {
                            if (true)
                            {
                                //Toast.makeText(getContext(), "Latitud: " + location.getLatitude() + " Longitud: " + location.getLongitude(), Toast.LENGTH_LONG).show();
                                latitud = location.getLatitude();
                                longitud = location.getLongitude();
                                LatLng ubicacionActual = new LatLng(location.getLatitude(), location.getLongitude());
                                map.addMarker(new MarkerOptions()
                                        .position(ubicacionActual)
                                );
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionActual, 15f));
                            } else
                            {
                                //createLocationRequest();
                                Toast.makeText(getContext(), "Reinicia el servicio de ubicación", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
                else{
                    showAlertDialogNOGPS();
                }
            }else{
                checkLocationPermissions();
            }
        }
        else{
            if(gpsActived()){
                fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>()
                {
                    @Override
                    public void onSuccess(Location location)
                    {
                        if (true)
                        {
                            //Toast.makeText(getContext(), "Latitud: " + location.getLatitude() + " Longitud: " + location.getLongitude(), Toast.LENGTH_LONG).show();
                            latitud = location.getLatitude();
                            longitud = location.getLongitude();
                            LatLng ubicacionActual = new LatLng(location.getLatitude(), location.getLongitude());
                            map.addMarker(new MarkerOptions()
                                    .position(ubicacionActual)
                            );
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionActual, 15f));
                        } else
                        {
                            //createLocationRequest();
                            Toast.makeText(getContext(), "Reinicia el servicio de ubicación", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
            else{
                showAlertDialogNOGPS();
            }
        }




    }

    //Reactivar ubicación
    /*protected void createLocationRequest()
    {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }*/

    //Mostrar aviso de ubicación


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.iV_foto_registrar:
                Intent tomaFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (tomaFoto.resolveActivity(getActivity().getPackageManager()) != null)
                {
                    File photoFile = null;
                    try
                    {
                        photoFile = createImageFile();
                    } catch (IOException e)
                    {
                        Toast.makeText(getContext(), "Error al generar foto", Toast.LENGTH_LONG).show();
                    }
                    if (photoFile != null)
                    {
                        photoURI = FileProvider.getUriForFile(getContext(), "com.example.selge_bil", photoFile);
                        tomaFoto.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(tomaFoto, REQUEST_TAKE_PHOTO);
                    }
                }
                break;
            case R.id.btn_limpiar_registrar:
                limpiar();
                break;
            case R.id.btn_agregar_registrar:
                //System.out.println("-----------------------------------------------------------" + latitud + " " + longitud);
                if (eTModelo.getText().toString().equals("") || eTPrecio.getText().toString().equals("")
                        || eTTraccion.getText().toString().equals("") || eTKilometraje.getText().toString().equals("")
                    /*|| imagen.equals("")*/)
                {
                    Toast.makeText(v.getContext(), "Completa los campos", Toast.LENGTH_LONG).show();
                } else
                {
                    String modelo = eTModelo.getText().toString();
                    String precio = eTPrecio.getText().toString();
                    String traccion = eTTraccion.getText().toString();
                    String kilometraje = eTKilometraje.getText().toString();

                    Carro carro = new Carro();

                    carro.setId(UUID.randomUUID().toString());
                    carro.setModelo(modelo);
                    carro.setPrecio(precio);
                    carro.setTraccion(traccion);
                    carro.setKilometraje(kilometraje);
                    carro.setMarca(marca);
                    carro.setTransmision(transmision);
                    carro.setTipoCarro(tipoCarro);
                    carro.setAnio(anio);
                    carro.setCombustible(combustible);
                    carro.setNumeroPasajeros(noPasajeros);
                    carro.setImagen(imagen);
                    carro.setLatitud(String.valueOf(latitud));
                    carro.setLongitud(String.valueOf(longitud));
                    carro.setEstado("0");
                    carro.setId_usuarioComprador("0");
                    carro.setId_usuarioVendedor(authProvider.getId());
                    carro.setEstado_id_usuarioVendedor("0_" + authProvider.getId());

                    databaseReference.child(carro.getId()).setValue(carro);
                    Toast.makeText(getContext(), "Datos agregados", Toast.LENGTH_SHORT).show();
                    limpiar();
                }
                break;
        }
    }

    private File createImageFile() throws IOException
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFile = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFile, ".jpg", storageDir);
        currentPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK)
        {
            iVFoto.setImageURI(photoURI);
            StorageReference filePath = storageReference.child("carros").child(photoURI.getLastPathSegment());
            filePath.putFile(photoURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
            {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    imagen = photoURI.getLastPathSegment();
                    Toast.makeText(getContext(), "Foto guardada: " + imagen, Toast.LENGTH_LONG).show();
                }
            });
        }
        if (requestCode == SETTINGS_REQUEST_CODE && gpsActived()) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>()
            {
                @Override
                public void onSuccess(Location location)
                {
                    if (true)
                    {
                        //Toast.makeText(getContext(), "Latitud: " + location.getLatitude() + " Longitud: " + location.getLongitude(), Toast.LENGTH_LONG).show();
                        latitud = location.getLatitude();
                        longitud = location.getLongitude();
                        LatLng ubicacionActual = new LatLng(location.getLatitude(), location.getLongitude());
                        map.addMarker(new MarkerOptions()
                                .position(ubicacionActual)
                        );
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionActual, 15f));
                    } else
                    {
                        //createLocationRequest();
                        Toast.makeText(getContext(), "Reinicia el servicio de ubicación", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else{
            showAlertDialogNOGPS();
        }

    }

    public void limpiar()
    {
        eTModelo.setText("");
        eTPrecio.setText("");
        eTTraccion.setText("");
        eTKilometraje.setText("");

        spnMarcas.setSelection(0);
        spnTransmisiones.setSelection(0);
        spnTiposCarro.setSelection(0);
        spnAnios.setSelection(80);
        spnCombustibles.setSelection(0);
        spnNoPasajeros.setSelection(0);

        marca = "";
        transmision = "";
        tipoCarro = "";
        anio = "";
        combustible = "";
        noPasajeros = "";
        imagen = "";

        iVFoto.setImageResource(R.drawable.icono_foto);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        switch (parent.getId())
        {
            case R.id.spn_marca_registrar:
                marca = parent.getItemAtPosition(position).toString();
                break;
            case R.id.spn_transmision_registrar:
                transmision = parent.getItemAtPosition(position).toString();
                break;
            case R.id.spn_tipoCarro_registrar:
                tipoCarro = parent.getItemAtPosition(position).toString();
                break;
            case R.id.spn_anio_registrar:
                anio = parent.getItemAtPosition(position).toString();
                break;
            case R.id.spn_combustible_registrar:
                combustible = parent.getItemAtPosition(position).toString();
                break;
            case R.id.spn_noPasajeros_registrar:
                noPasajeros = parent.getItemAtPosition(position).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {//si el usuario concedio los permisos
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if(gpsActived()){
                        fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>()
                        {
                            @Override
                            public void onSuccess(Location location)
                            {
                                if (true)
                                {
                                    //Toast.makeText(getContext(), "Latitud: " + location.getLatitude() + " Longitud: " + location.getLongitude(), Toast.LENGTH_LONG).show();
                                    latitud = location.getLatitude();
                                    longitud = location.getLongitude();
                                    LatLng ubicacionActual = new LatLng(location.getLatitude(), location.getLongitude());
                                    map.addMarker(new MarkerOptions()
                                            .position(ubicacionActual)
                                    );
                                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionActual, 15f));
                                } else
                                {
                                    //createLocationRequest();
                                    Toast.makeText(getContext(), "Reinicia el servicio de ubicación", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                    else{
                        showAlertDialogNOGPS();
                    }
                } else {
                    checkLocationPermissions();
                }

            } else {
                checkLocationPermissions();
            }
        }
    }


}