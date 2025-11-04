package com.example.pm1e2grupo3.api;

import java.io.Serializable;
import com.google.gson.annotations.SerializedName;

public class Persona implements Serializable{
    // Nota: El JSON de tu API GetPersons.php usa minúsculas
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

    // Constructor para crear
    public Persona(String nombre, String telefono, String latitud, String longitud, String video) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.latitud = latitud;
        this.longitud = longitud;
        this.video = video;
    }

    // Constructor para actualizar (incluye id)
    public Persona(String id, String nombre, String telefono, String latitud, String longitud, String video) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.latitud = latitud;
        this.longitud = longitud;
        this.video = video;
    }

    // Getters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    public String getLatitud() { return latitud; }
    public String getLongitud() { return longitud; }
    public String getVideo() { return video; }
}
