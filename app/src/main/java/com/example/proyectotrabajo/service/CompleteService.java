package com.example.proyectotrabajo.service;
import com.example.proyectotrabajo.entity.Complete;
import com.example.proyectotrabajo.entity.Respuesta;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CompleteService {

    @POST("complete")
    public abstract Call<Respuesta> completarCompra(@Body Complete obj);
}
