package com.example.pm1e2grupo3.api;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;


public class Persona implements Serializable {


    @SerializedName("id")
    private String id;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("telefono")
    private String telefono;

    @SerializedName("latitud")
    private String latitud;

    @SerializedName("longitud")
    private String longitud;

    @SerializedName("video")
    private String video;

    public Persona(String nombre, String telefono, String latitud, String longitud, String video) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.latitud = latitud;
        this.longitud = longitud;
        this.video = video;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    public String getLatitud() { return latitud; }
    public String getLongitud() { return longitud; }
    public String getVideo() { return video; }

    public void setId(String id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setLatitud(String latitud) { this.latitud = latitud; }
    public void setLongitud(String longitud) { this.longitud = longitud; }
    public void setVideo(String video) { this.video = video; }
}
