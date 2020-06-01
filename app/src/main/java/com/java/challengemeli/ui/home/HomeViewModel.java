package com.java.challengemeli.ui.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.java.challengemeli.core.services.MeliServices;
import com.java.challengemeli.core.services.ResultObject;
import com.java.challengemeli.core.services.Services;
import com.java.challengemeli.entities.Product;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<Boolean> mutableLiveDataLoadError;
    private MutableLiveData<Boolean> mutableLiveDataSearchError;
    private MutableLiveData<Boolean> mutableLiveDataLoading;
    private Services services;
    private MutableLiveData<List<Product>> mutableLiveDataListProducts;
    private CompositeDisposable disposable;

    public HomeViewModel() {
        init();
    }

    //Inicializo los atributos necesarios
    private void init() {
        services = MeliServices.instance(Services.class);
        disposable =  new CompositeDisposable();
        mutableLiveDataListProducts = new MutableLiveData<>();
        mutableLiveDataLoadError = new MutableLiveData<>();
        mutableLiveDataSearchError = new MutableLiveData<>();
        mutableLiveDataLoading = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> getMutableLiveDataLoadError() {
        return mutableLiveDataLoadError;
    }

    public MutableLiveData<List<Product>> getMutableLiveDataListProducts() {
        return mutableLiveDataListProducts;
    }

    public MutableLiveData<Boolean> getMutableLiveDataSearchError() {
        return mutableLiveDataSearchError;
    }

    public MutableLiveData<Boolean> getMutableLiveDataLoading() {
        return mutableLiveDataLoading;
    }

    //Seteo en el caso de recibir posibles productos
    private void setMutableLiveDataProducts(List<Product> productList) {
        mutableLiveDataListProducts.setValue(productList);
        mutableLiveDataLoadError.setValue(false);
        mutableLiveDataLoading.setValue(false);
        if (productList.size() == 0) {
            mutableLiveDataSearchError.setValue(true);
        } else {
            mutableLiveDataSearchError.setValue(false);
        }
    }

    //Seteo en el caso de error
    private void setError() {
        mutableLiveDataLoadError.setValue(true);
        mutableLiveDataLoading.setValue(false);
        mutableLiveDataSearchError.setValue(false);
    }

    //Pido la lista de posibles productos segun el texto buscado y lo agrego al disposable, también observo posibles resultados negativos o positivos
    public void getSearchProducts(String textSearch) {
        mutableLiveDataLoading.setValue(true);
        disposable.add(services.getSearchProducts(textSearch)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ResultObject<List<Product>>>() {
                    @Override
                    public void onSuccess(ResultObject<List<Product>> listResultObject) {
                        setMutableLiveDataProducts(listResultObject.getObject());
                    }

                    @Override
                    public void onError(Throwable e) {
                        setError();
                    }
                }));
    }

    //Limpio el disposable
    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

}
