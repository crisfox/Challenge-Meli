package com.java.challengemeli;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.java.challengemeli.core.ViewModelFactory;
import com.java.challengemeli.core.services.ResultObject;
import com.java.challengemeli.core.services.Services;
import com.java.challengemeli.entities.Product;
import com.java.challengemeli.ui.home.HomeViewModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;

public class ViewModelHomeTest {
    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    @Mock
    Services services;

    @InjectMocks
    HomeViewModel homeViewModel = new ViewModelFactory().create(HomeViewModel.class);

    private Single<ResultObject<List<Product>>> productSingle;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        setUpRxSchedulers();
    }

    private void setUpRxSchedulers() {
        Scheduler immediate = new Scheduler() {
            @Override
            public Worker createWorker() {
                return new ExecutorScheduler.ExecutorWorker(Runnable::run);
            }
        };

        RxJavaPlugins.setInitIoSchedulerHandler(schedulerCallable -> immediate);
        RxJavaPlugins.setInitComputationSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitNewThreadSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitSingleSchedulerHandler(scheduler -> immediate);
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> immediate);
    }

    //Caso Search con 1 solo resultado
    @Test
    public void getProductsSuccess() {
        Product productTest = new Product("1", "Test titulo", 12.5, 1, "imagen", true, "Nueva", "https://www.mercadolibre.com.ar/");
        ArrayList<Product> productsListTest = new ArrayList<>();
        productsListTest.add(productTest);

        ResultObject<List<Product>> resultsTest = new ResultObject<>(productsListTest);

        productSingle = Single.just(resultsTest);

        Mockito.when(services.getSearchProducts("")).thenReturn(productSingle);

        homeViewModel.getSearchProducts("");

        Assert.assertEquals(1, Objects.requireNonNull(homeViewModel.getMutableLiveDataListProducts().getValue()).size());
        Assert.assertEquals(false, homeViewModel.getMutableLiveDataLoadError().getValue());
        Assert.assertEquals(false, homeViewModel.getMutableLiveDataLoading().getValue());
        Assert.assertEquals(false, homeViewModel.getMutableLiveDataSearchError().getValue());
    }

    //Caso Search sin resultados
    @Test
    public void getProductsSuccessSearchError() {
        ArrayList<Product> productsListTest = new ArrayList<>();
        ResultObject<List<Product>> resultsTest = new ResultObject<>(productsListTest);

        productSingle = Single.just(resultsTest);

        Mockito.when(services.getSearchProducts("")).thenReturn(productSingle);

        homeViewModel.getSearchProducts("");

        Assert.assertEquals(0, Objects.requireNonNull(homeViewModel.getMutableLiveDataListProducts().getValue()).size());
        Assert.assertEquals(false, homeViewModel.getMutableLiveDataLoadError().getValue());
        Assert.assertEquals(false, homeViewModel.getMutableLiveDataLoading().getValue());
        Assert.assertEquals(true, homeViewModel.getMutableLiveDataSearchError().getValue());
    }

    //Caso Failure
    @Test
    public void getProductsFailure() {
        productSingle = Single.error(new Throwable());

        Mockito.when(services.getSearchProducts("")).thenReturn(productSingle);

        homeViewModel.getSearchProducts("");

        Assert.assertEquals(true, homeViewModel.getMutableLiveDataLoadError().getValue());
        Assert.assertEquals(false, homeViewModel.getMutableLiveDataLoading().getValue());
        Assert.assertEquals(false, homeViewModel.getMutableLiveDataSearchError().getValue());
    }

}
