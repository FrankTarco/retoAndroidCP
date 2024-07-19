package com.example.proyectotrabajo.util;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConnectionRest {



    public static final String URL = "https://cp-staging.onrender.com/v1/";
    public static Retrofit retrofit = null;


    public static Retrofit getConnection(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }


}
