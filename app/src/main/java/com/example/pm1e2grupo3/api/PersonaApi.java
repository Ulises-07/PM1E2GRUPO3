package com.example.pm1e2grupo3.api;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HTTP;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface PersonaApi {

    @POST("PostPersons.php")
    Call<Void> createPerson(@Body Persona persona);

    @GET("GetPersons.php")
    Call<List<Persona>> getPersons();

    @PUT("UpdatePersons.php")
    Call<Void> updatePerson(@Body Persona persona);

    @HTTP(method = "DELETE", path = "DeletePersons.php", hasBody = true)
    Call<Void> deletePerson(@Body Persona persona);
}
