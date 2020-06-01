package com.java.challengemeli;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.java.challengemeli.core.ViewModelFactory;
import com.java.challengemeli.core.services.Services;
import com.java.challengemeli.entities.Category;
import com.java.challengemeli.ui.search.SearchViewModel;

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

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;

public class ViewModelSearchTest {
    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    @Mock
    Services services;

    @InjectMocks
    SearchViewModel searchViewModel = new ViewModelFactory().create(SearchViewModel.class);

    private Single<List<Category>> categorySingle;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        setUpRxSchedulers();
    }

    public void setUpRxSchedulers() {
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

    @Test
    public void getCategorySuccess() {
        Category categoryTest = new Category("1", "Test titulo Categoria");
        List<Category> categoryArrayList = new ArrayList<>();
        categoryArrayList.add(categoryTest);

        categorySingle = Single.just(categoryArrayList);

        Mockito.when(services.getListCategories()).thenReturn(categorySingle);

        searchViewModel.getServiceListCategories();

        Assert.assertEquals(1, searchViewModel.getMutableLiveDataListCategory().getValue().size());
        Assert.assertEquals(false, searchViewModel.getMutableLiveDataLoadError().getValue());
        Assert.assertEquals(false, searchViewModel.getMutableLiveDataLoading().getValue());
    }

    @Test
    public void getProductsFailure() {
        categorySingle = Single.error(new Throwable());

        Mockito.when(services.getListCategories()).thenReturn(categorySingle);

        searchViewModel.getServiceListCategories();

        Assert.assertEquals(true, searchViewModel.getMutableLiveDataLoadError().getValue());
        Assert.assertEquals(false, searchViewModel.getMutableLiveDataLoading().getValue());
    }

}
