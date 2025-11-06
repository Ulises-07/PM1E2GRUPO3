package com.example.pm1e2grupo3.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    // !! IMPORTANTE !!
    // Cambia esta URL por la IP de tu computadora donde corre el servidor PHP.
    // No uses 'localhost' o '127.0.0.1' porque el emulador/teléfono no lo entenderá.
    // 1. Abre CMD en Windows y escribe 'ipconfig'. Busca tu dirección IPv4.
    // 2. Asegúrate que tu teléfono y tu PC estén en la MISMA red WiFi.
    // 3. El path debe apuntar a la carpeta donde están tus archivos .php

    // Ejemplo: "http://192.168.1.5/pm1e2grupo3/"
    private static final String BASE_URL = "http://192.168.1.43/pm1e2grupo3/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}