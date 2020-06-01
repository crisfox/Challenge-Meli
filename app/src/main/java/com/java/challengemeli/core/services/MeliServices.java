package com.java.challengemeli.core.services;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MeliServices {
    private static Retrofit retrofit;
    private static String BASE_URL = "https://api.mercadolibre.com";

    //Configuro Retrofit para que solo pida la instancia y asi crear la herramienta service
    public static <S> S instance(final Class<S> serviceClass) {
        if (retrofit == null) {
            Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
            retrofit = retrofitBuilder.build();
        }
        return retrofit.create(serviceClass);
    }
}
