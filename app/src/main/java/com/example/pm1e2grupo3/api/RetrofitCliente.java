package com.example.pm1e2grupo3.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitCliente {
    // CAMBIA ESTA URL por tu IP o 10.0.2.2 si usas emulador
    private static final String BASE_URL = "http://localhost/pm1e2grupo3";

    private static Retrofit retrofit = null;

    public static ApiService getCliente() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}
