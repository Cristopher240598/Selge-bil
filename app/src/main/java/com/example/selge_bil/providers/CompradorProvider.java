package com.example.selge_bil.providers;

import com.example.selge_bil.models.Comprador;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class CompradorProvider
{
    DatabaseReference mDatabase;

    public CompradorProvider()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Compradores");
    }

    public Task<Void> create(Comprador client)
    {
        Map<String, Object> map = new HashMap<>();
        map.put("name", client.getName());
        map.put("email", client.getEmail());
        map.put("paterno", client.getApPaterno());
        map.put("materno", client.getApMaterno());
        map.put("phone", client.getTelefono());
        map.put("direction", client.getDomicilio());
        return mDatabase.child(client.getId()).setValue(map);
    }
}
