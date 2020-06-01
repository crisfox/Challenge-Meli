package com.java.challengemeli.core.services;

import com.google.gson.annotations.SerializedName;

public class ResultObject<T> {

    @SerializedName("results")
    private T object;

    public ResultObject(T productTest) {
        object = productTest;
    }

    public T getObject() {
        return object;
    }

}
