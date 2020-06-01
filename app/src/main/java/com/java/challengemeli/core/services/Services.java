package com.java.challengemeli.core.services;

import com.java.challengemeli.entities.Category;
import com.java.challengemeli.entities.Product;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface Services {

    @Headers({"Accept: application/json",
            "Content-Type: application/json"})
    @GET("sites/MLA/categories")
    Single<List<Category>> getListCategories();

    @Headers({"Accept: application/json",
            "Content-Type: application/json"})
    @GET("sites/MLA/search")
    Single<ResultObject<List<Product>>> getSearchProducts(@Query("q") String query);
}
