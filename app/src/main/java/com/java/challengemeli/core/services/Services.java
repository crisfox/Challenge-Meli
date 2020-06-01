package com.java.challengemeli.core.services;

import com.java.challengemeli.entities.Category;
import com.java.challengemeli.entities.Product;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface Services {

    //Trae todas las categorias
    @Headers({"Accept: application/json",
            "Content-Type: application/json"})
    @GET("sites/MLA/categories")
    Single<List<Category>> getListCategories();

    //Trae una lista de productos segun el string buscado
    @Headers({"Accept: application/json",
            "Content-Type: application/json"})
    @GET("sites/MLA/search")
    Single<ResultObject<List<Product>>> getSearchProducts(@Query("q") String query);
}
