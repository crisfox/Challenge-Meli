package com.java.challengemeli.ui.search;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.java.challengemeli.core.services.MeliServices;
import com.java.challengemeli.core.services.Services;
import com.java.challengemeli.entities.Category;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class SearchViewModel extends ViewModel {
    private MutableLiveData<Boolean> mutableLiveDataLoadError;
    private MutableLiveData<Boolean> mutableLiveDataLoading;
    private MutableLiveData<List<Category>> mutableLiveDataListCategory;
    private Services services;
    private CompositeDisposable disposable;

    public SearchViewModel() {
        init();
    }

    //Inicializo los atributos necesarios
    private void init() {
        services = MeliServices.instance(Services.class);
        disposable = new CompositeDisposable();
        mutableLiveDataListCategory = new MutableLiveData<>();
        mutableLiveDataLoadError = new MutableLiveData<>();
        mutableLiveDataLoading = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> getMutableLiveDataLoadError() {
        return mutableLiveDataLoadError;
    }

    public MutableLiveData<Boolean> getMutableLiveDataLoading() {
        return mutableLiveDataLoading;
    }

    public MutableLiveData<List<Category>> getMutableLiveDataListCategory() {
        return mutableLiveDataListCategory;
    }

    //Seteo en el caso de posibles resultados positivos
    private void setMutableLiveDataCategories(List<Category> productList) {
        mutableLiveDataListCategory.setValue(productList);
        mutableLiveDataLoadError.setValue(false);
        mutableLiveDataLoading.setValue(false);
    }

    //SEteo en el caso de error
    private void setError() {
        mutableLiveDataLoadError.setValue(true);
        mutableLiveDataLoading.setValue(false);
    }

    //Pido la lista de posibles Categorias, lo agrego al disposable y escucho los resultados positivos o negativos.
    public void getServiceListCategories() {
        mutableLiveDataLoading.setValue(true);
        disposable.add(services.getListCategories()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Category>>() {
                    @Override
                    public void onSuccess(List<Category> listResultObject) {
                        setMutableLiveDataCategories(listResultObject);
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
