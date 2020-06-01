package com.java.challengemeli.ui.search;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.java.challengemeli.core.services.MeliServices;
import com.java.challengemeli.core.services.ResultObject;
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
    private CompositeDisposable disposable = new CompositeDisposable();


    public SearchViewModel() {
        init();
    }

    public void init() {
        this.services = MeliServices.instance(Services.class);
        mutableLiveDataListCategory = new MutableLiveData<>();
        mutableLiveDataLoadError = new MutableLiveData<>();
        mutableLiveDataLoading = new MutableLiveData<>();
    }

    public void setMutableLiveDataCategories(List<Category> productList) {
        mutableLiveDataListCategory.setValue(productList);
        mutableLiveDataLoadError.setValue(false);
        mutableLiveDataLoading.setValue(false);
    }

    public MutableLiveData<Boolean> getMutableLiveDataLoadError() {
        return mutableLiveDataLoadError;
    }

    public void setMutableLiveDataLoadError(MutableLiveData<Boolean> mutableLiveDataLoadError) {
        this.mutableLiveDataLoadError = mutableLiveDataLoadError;
    }

    public MutableLiveData<Boolean> getMutableLiveDataLoading() {
        return mutableLiveDataLoading;
    }

    public void setMutableLiveDataLoading(MutableLiveData<Boolean> mutableLiveDataLoading) {
        this.mutableLiveDataLoading = mutableLiveDataLoading;
    }

    public MutableLiveData<List<Category>> getMutableLiveDataListCategory() {
        return mutableLiveDataListCategory;
    }

    public void setMutableLiveDataListCategory(MutableLiveData<List<Category>> mutableLiveDataListCategory) {
        this.mutableLiveDataListCategory = mutableLiveDataListCategory;
    }

    private void setError() {
        mutableLiveDataLoadError.setValue(true);
        mutableLiveDataLoading.setValue(false);
    }

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

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

}
