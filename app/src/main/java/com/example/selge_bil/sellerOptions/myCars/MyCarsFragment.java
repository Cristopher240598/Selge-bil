package com.example.selge_bil.sellerOptions.myCars;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.selge_bil.R;
import com.example.selge_bil.models.Carro;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MyCarsFragment extends Fragment
{

    private RecyclerView rV_carrosList;
    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    public StorageReference storageReference;
    private AuthProvider authProvider;
    Query query;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_mycars, container, false);
        baseDatos();

        rV_carrosList = (RecyclerView) root.findViewById(R.id.my_recycler_view);
        rV_carrosList.setHasFixedSize(true);
        rV_carrosList.setLayoutManager(new LinearLayoutManager(getContext()));

        return root;
    }

    private void baseDatos()
    {
        authProvider = new AuthProvider();
        FirebaseApp.initializeApp(getContext());
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Cars");
        databaseReference.keepSynced(true);
        query = databaseReference.orderByChild("estado_id_usuarioVendedor").equalTo("0_" + authProvider.getId());
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    @Override
    public void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Carro> options = new FirebaseRecyclerOptions.Builder<Carro>()
                .setQuery(query, Carro.class)
                .build();
        FirebaseRecyclerAdapter<Carro, CarroViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Carro, CarroViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull CarroViewHolder holder, int position, @NonNull Carro model)
            {
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
            }

            @NonNull
            @Override
            public CarroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_row, parent, false);
                return new CarroViewHolder(view);
            }
        };
        firebaseRecyclerAdapter.startListening();
        rV_carrosList.setAdapter(firebaseRecyclerAdapter);
    }


    public static class CarroViewHolder extends RecyclerView.ViewHolder
    {
        View view;
        public MapView mapView;

        protected GoogleMap mGoogleMap;
        protected LatLng latLng;

        final FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();

        public CarroViewHolder(final View itemView)
        {
            super(itemView);
            view = itemView;
            mapView = (MapView) itemView.findViewById(R.id.map_misCarros);
            mapView.onCreate(null);
            mapView.getMapAsync(new OnMapReadyCallback()
            {
                @Override
                public void onMapReady(GoogleMap googleMap)
                {

                    mGoogleMap = googleMap;
                    MapsInitializer.initialize(itemView.getContext());
                    googleMap.getUiSettings().setMapToolbarEnabled(true);
                    if (latLng != null)
                    {
                        updateMapContents();
                    }
                }
            });
        }

        protected void updateMapContents()
        {
            mGoogleMap.clear();
            // Update the mapView feature data and camera position.
            mGoogleMap.addMarker(new MarkerOptions().position(latLng));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(latLng);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
        }

        public void setMapLocation(double lat, double lon)
        {
            latLng = new LatLng(lat, lon);

            if (mGoogleMap != null)
            {
                updateMapContents();
            }
        }

        public void setModelo(String modelo)
        {
            TextView tV_modelo = (TextView) view.findViewById(R.id.tV_modelo_misCarros);
            tV_modelo.setText("Modelo: " + modelo);
        }

        public void setPrecio(String precio)
        {
            TextView tV_precio = (TextView) view.findViewById(R.id.tV_precio_misCarros);
            tV_precio.setText("Precio: $" + precio);
        }

        public void setTraccion(String traccion)
        {
            TextView tV_traccion = (TextView) view.findViewById(R.id.tV_traccion_misCarros);
            tV_traccion.setText("Tracción: " + traccion);
        }

        public void setKilomeraje(String kilomeraje)
        {
            TextView tV_kilometraje = (TextView) view.findViewById(R.id.tV_kilometraje_misCarros);
            tV_kilometraje.setText("Kilometraje: " + kilomeraje);
        }

        public void setMarca(String marca)
        {
            TextView tV_marca = (TextView) view.findViewById(R.id.tV_marca_misCarros);
            tV_marca.setText("Marca: " + marca);
        }

        public void setTransmision(String transmision)
        {
            TextView tV_transmision = (TextView) view.findViewById(R.id.tV_transmision_misCarros);
            tV_transmision.setText("Transmision: " + transmision);
        }

        public void setTipoCarro(String tipoCarro)
        {
            TextView tV_tipoCarro = (TextView) view.findViewById(R.id.tV_tipoCarros_misCarros);
            tV_tipoCarro.setText("Tipo de carro: " + tipoCarro);
        }

        public void setAnio(String anio)
        {
            TextView tV_anio = (TextView) view.findViewById(R.id.tV_anio_misCarros);
            tV_anio.setText("Año: " + anio);
        }

        public void setCombustible(String combustible)
        {
            TextView tV_combustible = (TextView) view.findViewById(R.id.tV_combustible_misCarros);
            tV_combustible.setText("Combustible: " + combustible);
        }

        public void setNoPasajeros(String noPasajeros)
        {
            TextView tV_noPasajeros = (TextView) view.findViewById(R.id.tV_noPasajeros_misCarros);
            tV_noPasajeros.setText("Pasajeros: " + noPasajeros);
        }

        public void setImagen(String imagen)
        {
            final ImageView iV_imagen = (ImageView) view.findViewById(R.id.iV_foto_misCarros);
            Glide.with(view).load(storageRef.child("carros/" + imagen)).into(iV_imagen);
            storageRef.child("carros/" + imagen).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
            {
                @Override
                public void onSuccess(Uri uri)
                {
                    Glide.with(view).load(uri).into(iV_imagen);
                }
            });
        }
    }
}