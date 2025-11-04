package com.example.pm1e2grupo3.api;

public class RespuestaApi {
    @SerializedName("issuccess")
    private boolean issuccess;

    @SerializedName("message")
    private String message;

    public boolean isIssuccess() { return issuccess; }
    public String getMessage() { return message; }
}
