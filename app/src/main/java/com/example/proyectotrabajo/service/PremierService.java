package com.example.proyectotrabajo.service;

import com.example.proyectotrabajo.entity.Premieres;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PremierService {

    @GET("premieres")
    public abstract Call<Premieres> listarPremieres();

}
