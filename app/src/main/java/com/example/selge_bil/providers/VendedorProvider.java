package com.example.selge_bil.providers;

import com.example.selge_bil.models.Comprador;
import com.example.selge_bil.models.Vendedor;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class VendedorProvider {
    DatabaseReference mDatabase;

    public VendedorProvider(){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Vendedores");
    }
    public Task<Void> create(Vendedor client){
        Map<String,Object> map=new HashMap<>();
        map.put("name",client.getName());
        map.put("email",client.getEmail());
        return mDatabase.child(client.getId()).setValue(map);
    }
}
