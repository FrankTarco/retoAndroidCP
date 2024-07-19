package com.example.proyectotrabajo.service;

import com.example.proyectotrabajo.entity.Items;
import com.example.proyectotrabajo.entity.Premieres;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CandyStoreService {

    @GET("candystore")
    public abstract Call<Items> listarCandyStore();

}
