package com.example.selge_bil.models;

public class Carro
{
    String id;
    String modelo;
    String precio;
    String traccion;
    String kilometraje;
    String marca;
    String transmision;
    String tipoCarro;
    String anio;
    String combustible;
    String numeroPasajeros;
    String imagen;
    String latitud;
    String longitud;
    String estado;//1 = vendido, 0 = disponible
    String id_usuarioComprador;
    String id_usuarioVendedor;
    String estado_id_usuarioVendedor;
    String estado_tipoCarro;

    public Carro()
    {

    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getModelo()
    {
        return modelo;
    }

    public void setModelo(String modelo)
    {
        this.modelo = modelo;
    }

    public String getPrecio()
    {
        return precio;
    }

    public void setPrecio(String precio)
    {
        this.precio = precio;
    }

    public String getTraccion()
    {
        return traccion;
    }

    public void setTraccion(String traccion)
    {
        this.traccion = traccion;
    }

    public String getKilometraje()
    {
        return kilometraje;
    }

    public void setKilometraje(String kilometraje)
    {
        this.kilometraje = kilometraje;
    }

    public String getMarca()
    {
        return marca;
    }

    public void setMarca(String marca)
    {
        this.marca = marca;
    }

    public String getTransmision()
    {
        return transmision;
    }

    public void setTransmision(String transmision)
    {
        this.transmision = transmision;
    }

    public String getTipoCarro()
    {
        return tipoCarro;
    }

    public void setTipoCarro(String tipoCarro)
    {
        this.tipoCarro = tipoCarro;
    }

    public String getAnio()
    {
        return anio;
    }

    public void setAnio(String anio)
    {
        this.anio = anio;
    }

    public String getCombustible()
    {
        return combustible;
    }

    public void setCombustible(String combustible)
    {
        this.combustible = combustible;
    }

    public String getNumeroPasajeros()
    {
        return numeroPasajeros;
    }

    public void setNumeroPasajeros(String numeroPasajeros)
    {
        this.numeroPasajeros = numeroPasajeros;
    }

    public String getImagen()
    {
        return imagen;
    }

    public void setImagen(String imagen)
    {
        this.imagen = imagen;
    }

    public String getLatitud()
    {
        return latitud;
    }

    public void setLatitud(String latitud)
    {
        this.latitud = latitud;
    }

    public String getLongitud()
    {
        return longitud;
    }

    public void setLongitud(String longitud)
    {
        this.longitud = longitud;
    }

    public String getEstado()
    {
        return estado;
    }

    public void setEstado(String estado)
    {
        this.estado = estado;
    }

    public String getId_usuarioComprador()
    {
        return id_usuarioComprador;
    }

    public void setId_usuarioComprador(String id_usuarioComprador)
    {
        this.id_usuarioComprador = id_usuarioComprador;
    }

    public String getId_usuarioVendedor()
    {
        return id_usuarioVendedor;
    }

    public void setId_usuarioVendedor(String id_usuarioVendedor)
    {
        this.id_usuarioVendedor = id_usuarioVendedor;
    }

    public String getEstado_id_usuarioVendedor()
    {
        return estado_id_usuarioVendedor;
    }

    public void setEstado_id_usuarioVendedor(String estado_id_usuarioVendedor)
    {
        this.estado_id_usuarioVendedor = estado_id_usuarioVendedor;
    }

    public String getEstado_tipoCarro()
    {
        return estado_tipoCarro;
    }

    public void setEstado_tipoCarro(String estado_tipoCarro)
    {
        this.estado_tipoCarro = estado_tipoCarro;
    }

    public String toString()
    {
        return marca + " " + modelo;
    }
}
