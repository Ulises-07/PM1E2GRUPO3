package com.example.pm1e2grupo3.api;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.HTTP; // Importante para DELETE con Body

public class ApiService {
    @GET("GetPersons.php")
    Call<List<Persona>> getPersonas();

    @POST("PostPersons.php")
    Call<RespuestaApi> createPersona(@Body Persona persona);

    @PUT("UpdatePersons.php")
    Call<RespuestaApi> updatePersona(@Body Persona persona);

    // DELETE en Retrofit con @Body es especial, usamos @HTTP
    @HTTP(method = "DELETE", path = "DeletePersons.php", hasBody = true)
    Call<RespuestaApi> deletePersona(@Body Persona persona);
}
