package com.example.pm1e2grupo3.api;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HTTP;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface PersonaApi {

    // Endpoint para Crear (POST) - PostPersons.php
    @POST("PostPersons.php")
    Call<Void> createPerson(@Body Persona persona);

    // Endpoint para Leer (GET) - GetPersons.php
    @GET("GetPersons.php")
    Call<List<Persona>> getPersons();

    // Endpoint para Actualizar (PUT) - UpdatePersons.php
    @PUT("UpdatePersons.php")
    Call<Void> updatePerson(@Body Persona persona);

    // Endpoint para Eliminar (DELETE) - DeletePersons.php
    // Usamos HTTP con hasBody = true porque el PHP espera un JSON con el ID
    @HTTP(method = "DELETE", path = "DeletePersons.php", hasBody = true)
    Call<Void> deletePerson(@Body Persona persona);
}
