package com.example.selge_bil.buyerOptions;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.selge_bil.R;
import com.example.selge_bil.models.Carro;
import com.example.selge_bil.models.Vendedor;
import com.example.selge_bil.providers.AuthProvider;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class detailsCar extends Fragment {

    private RecyclerView rV_carrosList;
    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private Button btnBack, btnBuy;
    public StorageReference storageReference;
    private AuthProvider authProvider;
    Query query;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_convertible, container, false);
        baseDatos();
        TextView tvhome = root.findViewById(R.id.text_home_buyer);
        tvhome.setText(MainActivity_Comprador.getFilter());
        rV_carrosList = (RecyclerView) root.findViewById(R.id.my_recycler_view);
        rV_carrosList.setHasFixedSize(true);
        rV_carrosList.setLayoutManager(new LinearLayoutManager(getContext()));


        return root;
    }

    private void baseDatos() {
        authProvider = new AuthProvider();
        FirebaseApp.initializeApp(getContext());
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Cars");
        databaseReference.keepSynced(true);
        query = databaseReference.orderByChild("id").equalTo(showListCar.getIdCarro());
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }
    private void baseDatos(String id) {
        authProvider = new AuthProvider();
        FirebaseApp.initializeApp(getContext());
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users/Vendedores");
//        databaseReference.keepSynced(true);
//        query = databaseReference.orderByChild("id").equalTo(id);
//        storage = FirebaseStorage.getInstance();
//        storageReference = storage.getReference();
    }

    public void back() {
        final showListCar f = new showListCar();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();//comenzamos transacción
        fragmentTransaction.replace(R.id.nav_host_fragment_buyer, f);
        fragmentTransaction.commit();
    }

    @Override
    public void onStart() {

        super.onStart();
        FirebaseRecyclerOptions<Carro> options = new FirebaseRecyclerOptions.Builder<Carro>()
//                .setQuery(query, Carro.class)
                .setQuery(query, Carro.class)
                .build();
        FirebaseRecyclerAdapter<Carro, detalles_Carro_buyer> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Carro, detalles_Carro_buyer>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final detalles_Carro_buyer holder, int position, @NonNull Carro model) {
                holder.setModelo(model.getModelo());
                holder.setPrecio(model.getPrecio());
                holder.setTraccion(model.getTraccion());
                holder.setKilomeraje(model.getKilometraje());
                holder.setMarca(model.getMarca());
                holder.setTransmision(model.getTransmision());
                holder.setTipoCarro(model.getTipoCarro());
                holder.setAnio(model.getAnio());
                holder.setCombustible(model.getCombustible());
                holder.setNoPasajeros(model.getNumeroPasajeros());
                holder.setImagen(model.getImagen());
                holder.setMapLocation(Double.parseDouble(model.getLatitud()), Double.parseDouble(model.getLongitud()));
                System.out.println(model.getId_usuarioVendedor()  +  "              <------------------");
                baseDatos(model.getId_usuarioVendedor());
                databaseReference.child(model.getId_usuarioVendedor()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Vendedor vendedor = snapshot.getValue(Vendedor.class);
                        holder.setName(vendedor.getName(), vendedor.getApPaterno(), vendedor.getApMaterno());
                        holder.setMail(vendedor.getEmail());
                        holder.setPhone(vendedor.getTelefono());
                        System.out.println(vendedor.getEmail()  +  "              <------------------");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                holder.setMapLocation(Double.parseDouble(model.getLatitud()), Double.parseDouble(model.getLongitud()));
            }

            @NonNull
            @Override
            public detalles_Carro_buyer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_row_details, parent, false);
                btnBack = view.findViewById(R.id.btnBack);
                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        back();
                    }
                });
                btnBuy = view.findViewById(R.id.btnBuy_carroDetalles);
                btnBuy.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        baseDatos();
                        databaseReference.child(showListCar.getIdCarro()).addValueEventListener(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot)
                            {
                                Carro carroSelec = snapshot.getValue(Carro.class);
                                Carro carro = new Carro();
                                carro.setId(carroSelec.getId());
                                carro.setModelo(carroSelec.getModelo());
                                carro.setPrecio(carroSelec.getPrecio());
                                carro.setTraccion(carroSelec.getTraccion());
                                carro.setKilometraje(carroSelec.getKilometraje());
                                carro.setMarca(carroSelec.getMarca());
                                carro.setTransmision(carroSelec.getTransmision());
                                carro.setTipoCarro(carroSelec.getTipoCarro());
                                carro.setAnio(carroSelec.getAnio());
                                carro.setCombustible(carroSelec.getCombustible());
                                carro.setNumeroPasajeros(carroSelec.getNumeroPasajeros());
                                carro.setImagen(carroSelec.getImagen());
                                carro.setLatitud(carroSelec.getLatitud());
                                carro.setLongitud(carroSelec.getLongitud());
                                carro.setEstado("1");
                                carro.setId_usuarioComprador(authProvider.getId());
                                carro.setId_usuarioVendedor(carroSelec.getId_usuarioVendedor());
                                carro.setEstado_id_usuarioVendedor("1_"+carro.getId_usuarioVendedor());
                                carro.setEstado_tipoCarro("1_"+carro.getTipoCarro());
                                databaseReference.child(carro.getId()).setValue(carro);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error)
                            {

                            }
                        });
                    }
                });
                return new detalles_Carro_buyer(view);
            }
        };
        firebaseRecyclerAdapter.startListening();
        rV_carrosList.setAdapter(firebaseRecyclerAdapter);

    }


    public static class detalles_Carro_buyer extends RecyclerView.ViewHolder {
        View view;
        public MapView mapView;

        protected GoogleMap mGoogleMap;
        protected LatLng latLng;

        final FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();

        public detalles_Carro_buyer(final View itemView) {
            super(itemView);
            view = itemView;
            mapView = (MapView) itemView.findViewById(R.id.map_misCarros_details);
            mapView.onCreate(null);
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {

                    mGoogleMap = googleMap;
                    MapsInitializer.initialize(itemView.getContext());
                    googleMap.getUiSettings().setMapToolbarEnabled(true);
                    if (latLng != null) {
                        updateMapContents();
                    }
                }
            });
        }

        protected void updateMapContents() {
            mGoogleMap.clear();
            // Update the mapView feature data and camera position.
            mGoogleMap.addMarker(new MarkerOptions().position(latLng));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(latLng);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
        }

        public void setMapLocation(double lat, double lon) {
            latLng = new LatLng(lat, lon);

            if (mGoogleMap != null) {
                updateMapContents();
            }
        }

        public void setModelo(String modelo) {
            TextView tV_modelo = (TextView) view.findViewById(R.id.tV_modelo_misCarros_details);
            tV_modelo.setText("Modelo: " + modelo);
        }

        public void setPrecio(String precio) {
            TextView tV_precio = (TextView) view.findViewById(R.id.tV_precio_misCarros_details);
            tV_precio.setText("Precio: $" + precio);
        }

        public void setTraccion(String traccion) {
            TextView tV_traccion = (TextView) view.findViewById(R.id.tV_traccion_misCarros_details);
            tV_traccion.setText("Tracción: " + traccion);
        }

        public void setKilomeraje(String kilomeraje) {
            TextView tV_kilometraje = (TextView) view.findViewById(R.id.tV_kilometraje_misCarros_details);
            tV_kilometraje.setText("Kilometraje: " + kilomeraje);
        }

        public void setMarca(String marca) {
            TextView tV_marca = (TextView) view.findViewById(R.id.tV_marca_misCarros_details);
            tV_marca.setText("Marca: " + marca);
        }

        public void setTransmision(String transmision) {
            TextView tV_transmision = (TextView) view.findViewById(R.id.tV_transmision_misCarros_details);
            tV_transmision.setText("Transmision: " + transmision);
        }

        public void setTipoCarro(String tipoCarro) {
            TextView tV_tipoCarro = (TextView) view.findViewById(R.id.tV_tipoCarros_misCarros_details);
            tV_tipoCarro.setText("Tipo de carro: " + tipoCarro);
        }

        public void setAnio(String anio) {
            TextView tV_anio = (TextView) view.findViewById(R.id.tV_anio_misCarros_details);
            tV_anio.setText("Año: " + anio);
        }

        public void setCombustible(String combustible) {
            TextView tV_combustible = (TextView) view.findViewById(R.id.tV_combustible_misCarros_details);
            tV_combustible.setText("Combustible: " + combustible);
        }

        public void setNoPasajeros(String noPasajeros) {
            TextView tV_noPasajeros = (TextView) view.findViewById(R.id.tV_noPasajeros_misCarros_details);
            tV_noPasajeros.setText("Pasajeros: " + noPasajeros);
        }

        public void setName(String nombre, String apPat, String apMat) {
            TextView tV_anio = (TextView) view.findViewById(R.id.seller_name);
            tV_anio.setText(nombre + " " + apPat + " "+ apMat);
        }

        public void setPhone(String combustible) {
            TextView tV_combustible = (TextView) view.findViewById(R.id.seller_phone);
            tV_combustible.setText("Combustible: " + combustible);
        }

        public void setMail(String noPasajeros) {
            TextView tV_noPasajeros = (TextView) view.findViewById(R.id.seller_email);
            tV_noPasajeros.setText("Pasajeros: " + noPasajeros);
        }

        public void setImagen(String imagen) {
            final ImageView iV_imagen = (ImageView) view.findViewById(R.id.iV_foto_misCarros_details);
            Glide.with(view).load(storageRef.child("carros/" + imagen)).into(iV_imagen);
            storageRef.child("carros/" + imagen).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(view).load(uri).into(iV_imagen);
                }
            });
        }
    }
}
