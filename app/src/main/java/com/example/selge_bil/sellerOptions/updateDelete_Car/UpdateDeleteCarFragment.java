package com.example.selge_bil.sellerOptions.updateDelete_Car;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.selge_bil.R;
import com.example.selge_bil.models.Carro;
import com.example.selge_bil.providers.AuthProvider;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateDeleteCarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateDeleteCarFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener,
        AdapterView.OnItemSelectedListener
{
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private AuthProvider authProvider;

    private List<Carro> carroList = new ArrayList<>();
    ArrayAdapter<Carro> arrayAdapterCarro;
    private ListView lVCarros;
    private Carro carroSeleccionado;

    private Spinner spnMarcas, spnTransmisiones, spnTiposCarro, spnAnios, spnCombustibles, spnNoPasajeros;
    private EditText eTModelo, eTPrecio, eTTraccion, eTKilometraje;
    private Button btnEliminar, btnActualizar;
    private ImageView iVFoto;
    public static final int REQUEST_TAKE_PHOTO = 1;
    private Uri photoURI;
    private boolean banderaImagen;

    private LatLng ubicacionActual;
    private GoogleMap map;
    private boolean bandera;

    private String marca, transmision, tipoCarro, anio, combustible, noPasajeros, imagen, currentPath, imagenAnterior;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UpdateDeleteCarFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdateDeleteCarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateDeleteCarFragment newInstance(String param1, String param2)
    {
        UpdateDeleteCarFragment fragment = new UpdateDeleteCarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_update_delete_car, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_ActaulizarEliminar);
        mapFragment.getMapAsync(this);
        baseDatos();
        listarDatos();
        componentes(root);
        ubicacionActual = new LatLng(19.4978, -99.1269);
        bandera = false;
        banderaImagen = false;
        lVCarros.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                carroSeleccionado = (Carro) parent.getItemAtPosition(position);
                eTModelo.setText(carroSeleccionado.getModelo());
                eTPrecio.setText(carroSeleccionado.getPrecio());
                eTTraccion.setText(carroSeleccionado.getTraccion());
                eTKilometraje.setText(carroSeleccionado.getKilometraje());
                spnMarcas.setSelection(obtenerIndice(spnMarcas, carroSeleccionado.getMarca()));
                marca = carroSeleccionado.getMarca();
                spnTransmisiones.setSelection(obtenerIndice(spnTransmisiones, carroSeleccionado.getTransmision()));
                transmision = carroSeleccionado.getTransmision();
                spnTiposCarro.setSelection(obtenerIndice(spnTiposCarro, carroSeleccionado.getTipoCarro()));
                tipoCarro = carroSeleccionado.getTipoCarro();
                spnAnios.setSelection(obtenerIndice(spnAnios, carroSeleccionado.getAnio()));
                anio = carroSeleccionado.getAnio();
                spnCombustibles.setSelection(obtenerIndice(spnCombustibles, carroSeleccionado.getCombustible()));
                combustible = carroSeleccionado.getCombustible();
                spnNoPasajeros.setSelection(obtenerIndice(spnNoPasajeros, carroSeleccionado.getNumeroPasajeros()));
                noPasajeros = carroSeleccionado.getNumeroPasajeros();
                iVFoto.setImageResource(R.drawable.icono_foto);
                storageReference.child("carros/" + carroSeleccionado.getImagen()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                {
                    @Override
                    public void onSuccess(Uri uri)
                    {
                        Glide.with(getContext()).load(uri).into(iVFoto);
                    }
                });
                bandera = true;
                LatLng ubicacionCarro = new LatLng(Double.parseDouble(carroSeleccionado.getLatitud()), Double.parseDouble(carroSeleccionado.getLongitud()));
                ubicacionActual = ubicacionCarro;
                onMapReady(map);
            }
        });
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
        lVCarros = root.findViewById(R.id.lV_lista_ActualizarEliminar);

        editTextComponentes(root);
        botones(root);
        spinnerComponentes(root);
    }

    public void listarDatos()
    {
        databaseReference.orderByChild("id_usuarioVendedor").equalTo(authProvider.getId()).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                carroList.clear();
                for (DataSnapshot objDataSnapshot : snapshot.getChildren())
                {
                    if (getActivity() != null)
                    {
                        Carro c = objDataSnapshot.getValue(Carro.class);
                        carroList.add(c);
                        arrayAdapterCarro = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, carroList);
                        lVCarros.setAdapter(arrayAdapterCarro);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }

    public void editTextComponentes(View root)
    {
        eTModelo = root.findViewById(R.id.tIET_modelo_ActualizarEliminar);
        eTPrecio = root.findViewById(R.id.tIET_precio_ActualizarEliminar);
        eTTraccion = root.findViewById(R.id.tIET_traccion_ActualizarEliminar);
        eTKilometraje = root.findViewById(R.id.tIET_kilometraje_ActualizarEliminar);
    }

    public void botones(View root)
    {
        iVFoto = root.findViewById(R.id.iV_foto_ActualizarEliminar);
        btnActualizar = root.findViewById(R.id.btn_actualizar_ActualizarEliminar);
        btnEliminar = root.findViewById(R.id.btn_eliminar_ActualizarEliminar);

        iVFoto.setOnClickListener((View.OnClickListener) this);
        btnActualizar.setOnClickListener((View.OnClickListener) this);
        btnEliminar.setOnClickListener((View.OnClickListener) this);
    }

    public void spinnerComponentes(View root)
    {
        ArrayAdapter<CharSequence> marcasAdapter, transmisionesAdpater, tiposCarroAdapter, aniosAdapter, combustiblesAdapter, noPasajerosAdapter;
        marcasAdapter = ArrayAdapter.createFromResource(getContext(), R.array.marcas, android.R.layout.simple_spinner_item);
        transmisionesAdpater = ArrayAdapter.createFromResource(getContext(), R.array.transmisiones, android.R.layout.simple_spinner_item);
        tiposCarroAdapter = ArrayAdapter.createFromResource(getContext(), R.array.tiposCarro, android.R.layout.simple_spinner_item);
        aniosAdapter = ArrayAdapter.createFromResource(getContext(), R.array.anios, android.R.layout.simple_spinner_item);
        combustiblesAdapter = ArrayAdapter.createFromResource(getContext(), R.array.combustibles, android.R.layout.simple_spinner_item);
        noPasajerosAdapter = ArrayAdapter.createFromResource(getContext(), R.array.noPasajeros, android.R.layout.simple_spinner_item);
        spnMarcas = (Spinner) root.findViewById(R.id.spn_marca_ActualizarEliminar);
        spnMarcas.setAdapter(marcasAdapter);
        spnTransmisiones = (Spinner) root.findViewById(R.id.spn_transmision_ActualizarEliminar);
        spnTransmisiones.setAdapter(transmisionesAdpater);
        spnTiposCarro = (Spinner) root.findViewById(R.id.spn_tipoCarro_ActualizarEliminar);
        spnTiposCarro.setAdapter(tiposCarroAdapter);
        spnAnios = (Spinner) root.findViewById(R.id.spn_anio_ActualizarEliminar);
        spnAnios.setAdapter(aniosAdapter);
        spnAnios.setSelection(80);
        spnCombustibles = (Spinner) root.findViewById(R.id.spn_combustible_ActualizarEliminar);
        spnCombustibles.setAdapter(combustiblesAdapter);
        spnNoPasajeros = (Spinner) root.findViewById(R.id.spn_noPasajeros_ActualizarEliminar);
        spnNoPasajeros.setAdapter(noPasajerosAdapter);

        spnMarcas.setOnItemSelectedListener(this);
        spnTransmisiones.setOnItemSelectedListener(this);
        spnTiposCarro.setOnItemSelectedListener(this);
        spnAnios.setOnItemSelectedListener(this);
        spnCombustibles.setOnItemSelectedListener(this);
        spnNoPasajeros.setOnItemSelectedListener(this);
    }

    private int obtenerIndice(Spinner spinner, String string)
    {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++)
        {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(string))
            {
                index = i;
                break;
            }
        }
        return index;
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        map = googleMap;
        map.clear();
        if (bandera)
        {
            map.addMarker(new MarkerOptions()
                    .position(ubicacionActual)
            );
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionActual, 15f));
        } else
        {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionActual, 0f));
        }

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.iV_foto_ActualizarEliminar:
                banderaImagen = true;
                imagenAnterior = carroSeleccionado.getImagen();
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
            case R.id.btn_eliminar_ActualizarEliminar:
                AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
                dialogo.setTitle("Importante");
                dialogo.setMessage("¿Desea eliminar el carro?");
                dialogo.setCancelable(false);
                dialogo.setPositiveButton("Eliminar carro", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        databaseReference.child(carroSeleccionado.getId()).removeValue();
                        storageReference.child("carros/" + carroSeleccionado.getImagen()).delete().addOnSuccessListener(new OnSuccessListener<Void>()
                        {
                            @Override
                            public void onSuccess(Void aVoid)
                            {

                            }
                        });
                        Toast.makeText(getContext(), "Carro eliminado", Toast.LENGTH_LONG).show();
                        limpiar();
                    }
                });
                dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });
                dialogo.show();
                break;
            case R.id.btn_actualizar_ActualizarEliminar:
                if (eTModelo.getText().toString().equals("") || eTPrecio.getText().toString().equals("")
                        || eTTraccion.getText().toString().equals("") || eTKilometraje.getText().toString().equals(""))
                {
                    Toast.makeText(v.getContext(), "Completa los campos", Toast.LENGTH_LONG).show();
                } else
                {
                    String modelo = eTModelo.getText().toString();
                    String precio = eTPrecio.getText().toString();
                    String traccion = eTTraccion.getText().toString();
                    String kilometraje = eTKilometraje.getText().toString();

                    Carro carro = new Carro();

                    carro.setId(carroSeleccionado.getId());
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
                    if (banderaImagen)
                    {
                        carro.setImagen(imagen);
                        storageReference.child("carros/" + imagenAnterior).delete().addOnSuccessListener(new OnSuccessListener<Void>()
                        {
                            @Override
                            public void onSuccess(Void aVoid)
                            {

                            }
                        });
                    } else
                    {
                        carro.setImagen(carroSeleccionado.getImagen());
                    }
                    carro.setLatitud(carroSeleccionado.getLatitud());
                    carro.setLongitud(carroSeleccionado.getLongitud());
                    carro.setEstado(carroSeleccionado.getEstado());
                    carro.setId_usuarioComprador(carroSeleccionado.getId_usuarioComprador());
                    carro.setId_usuarioVendedor(carroSeleccionado.getId_usuarioVendedor());
                    carro.setEstado_id_usuarioVendedor(carroSeleccionado.getEstado_id_usuarioVendedor());

                    databaseReference.child(carro.getId()).setValue(carro);
                    Toast.makeText(getContext(), "Datos actualizados", Toast.LENGTH_LONG).show();
                    limpiar();
                }
                break;
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
        banderaImagen = false;

        iVFoto.setImageResource(R.drawable.icono_foto);
        bandera = false;
        LatLng ubicacion = new LatLng(19.4978, -99.1269);
        ubicacionActual = ubicacion;
        onMapReady(map);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        switch (parent.getId())
        {
            case R.id.spn_marca_ActualizarEliminar:
                marca = parent.getItemAtPosition(position).toString();
                break;
            case R.id.spn_transmision_ActualizarEliminar:
                transmision = parent.getItemAtPosition(position).toString();
                break;
            case R.id.spn_tipoCarro_ActualizarEliminar:
                tipoCarro = parent.getItemAtPosition(position).toString();
                break;
            case R.id.spn_anio_ActualizarEliminar:
                anio = parent.getItemAtPosition(position).toString();
                break;
            case R.id.spn_combustible_ActualizarEliminar:
                combustible = parent.getItemAtPosition(position).toString();
                break;
            case R.id.spn_noPasajeros_ActualizarEliminar:
                noPasajeros = parent.getItemAtPosition(position).toString();
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
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }
}