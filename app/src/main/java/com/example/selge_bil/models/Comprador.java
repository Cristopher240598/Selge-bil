package com.example.selge_bil.models;

public class Comprador
{
    String id;
    String name;
    String email;

    public Comprador()
    {
    }

    public Comprador(String id, String name, String email)
    {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }
}
