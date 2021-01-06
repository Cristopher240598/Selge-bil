package com.example.selge_bil.sellerOptions.sold;

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
import com.example.selge_bil.models.Comprador;
import com.example.selge_bil.providers.AuthProvider;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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

public class SoldFragment extends Fragment
{

    private RecyclerView rV_carrosVendidosList;
    private DatabaseReference databaseReference;
    private AuthProvider authProvider;
    Query query;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_sold, container, false);
        baseDatos();

        rV_carrosVendidosList = (RecyclerView) root.findViewById(R.id.my_recycler_view_vendidos);
        rV_carrosVendidosList.setHasFixedSize(true);
        rV_carrosVendidosList.setLayoutManager(new LinearLayoutManager(getContext()));

        return root;
    }

    private void baseDatos()
    {
        authProvider = new AuthProvider();
        FirebaseApp.initializeApp(getContext());
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Cars");
        query = databaseReference.orderByChild("estado_id_usuarioVendedor").equalTo("1_" + authProvider.getId());

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
                holder.setId_usuarioComprador(model.getId_usuarioComprador());
            }

            @NonNull
            @Override
            public CarroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sold_car_row, parent, false);
                return new CarroViewHolder(view);
            }
        };
        firebaseRecyclerAdapter.startListening();
        rV_carrosVendidosList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class CarroViewHolder extends RecyclerView.ViewHolder
    {
        View view;
        final FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();


        private DatabaseReference databaseReference_Carro = FirebaseDatabase.getInstance().getReference().child("Users").child("Compradores");

        public CarroViewHolder(View itemView)
        {
            super(itemView);
            view = itemView;
        }

        public void setModelo(String modelo)
        {
            TextView tV_modelo = (TextView) view.findViewById(R.id.tV_modelo_carrosVenidos);
            tV_modelo.setText("Modelo: " + modelo);
        }

        public void setPrecio(String precio)
        {
            TextView tV_precio = (TextView) view.findViewById(R.id.tV_precio_carrosVendidos);
            tV_precio.setText("Precio: $" + precio);
        }

        public void setTraccion(String traccion)
        {
            TextView tV_traccion = (TextView) view.findViewById(R.id.tV_traccion_carrosVendidos);
            tV_traccion.setText("Tracción: " + traccion);
        }

        public void setKilomeraje(String kilomeraje)
        {
            TextView tV_kilometraje = (TextView) view.findViewById(R.id.tV_kilometraje_carrosVendidos);
            tV_kilometraje.setText("Kilometraje: " + kilomeraje);
        }

        public void setMarca(String marca)
        {
            TextView tV_marca = (TextView) view.findViewById(R.id.tV_marca_carrosVendidos);
            tV_marca.setText("Marca: " + marca);
        }

        public void setTransmision(String transmision)
        {
            TextView tV_transmision = (TextView) view.findViewById(R.id.tV_transmision_carrosVendidos);
            tV_transmision.setText("Transmision: " + transmision);
        }

        public void setTipoCarro(String tipoCarro)
        {
            TextView tV_tipoCarro = (TextView) view.findViewById(R.id.tV_tipoCarros_carrosVendidos);
            tV_tipoCarro.setText("Tipo de carro: " + tipoCarro);
        }

        public void setAnio(String anio)
        {
            TextView tV_anio = (TextView) view.findViewById(R.id.tV_anio_carrosVendidos);
            tV_anio.setText("Año: " + anio);
        }

        public void setCombustible(String combustible)
        {
            TextView tV_combustible = (TextView) view.findViewById(R.id.tV_combustible_carrosVendidos);
            tV_combustible.setText("Combustible: " + combustible);
        }

        public void setNoPasajeros(String noPasajeros)
        {
            TextView tV_noPasajeros = (TextView) view.findViewById(R.id.tV_noPasajeros_carrosVendidos);
            tV_noPasajeros.setText("Pasajeros: " + noPasajeros);
        }

        public void setImagen(String imagen)
        {
            final ImageView iV_imagen = (ImageView) view.findViewById(R.id.iV_foto_carrosVendidos);
            storageRef.child("carros/" + imagen).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
            {
                @Override
                public void onSuccess(Uri uri)
                {
                    Glide.with(view).load(uri).into(iV_imagen);
                }
            });
        }

        public void setId_usuarioComprador(final String idUsuarioComprador)
        {
            final TextView tV_idUsuarioComprador = (TextView) view.findViewById(R.id.tV_nombreComprador_carrosVendidos);
            final TextView tV_correoComprador = (TextView) view.findViewById(R.id.tV_correoComprador_carrosVendidos);
            final TextView tV_telefonoComprador = (TextView) view.findViewById(R.id.tV_telefonoComprador_carrosVendidos);
            final TextView tV_direccionComprador = (TextView) view.findViewById(R.id.tV_direccionComprador_carrosVendidos);
            databaseReference_Carro.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    if (snapshot.hasChild(idUsuarioComprador))
                    {
                        Comprador comprador = snapshot.child(idUsuarioComprador).getValue(Comprador.class);
                        tV_idUsuarioComprador.setText(comprador.getName() + " " + comprador.getApPaterno() + " " + comprador.getApMaterno());
                        tV_correoComprador.setText(comprador.getEmail());
                        tV_telefonoComprador.setText(comprador.getTelefono());
                        tV_direccionComprador.setText(comprador.getDomicilio());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error)
                {

                }
            });
        }
    }
}