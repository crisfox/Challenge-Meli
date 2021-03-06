package com.java.challengemeli.core;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.java.challengemeli.ui.home.HomeViewModel;
import com.java.challengemeli.ui.search.SearchViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {


    public ViewModelFactory() {
    }

    //Seteo creador de ViewModels.
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (HomeViewModel.class.equals(modelClass)) {
            return (T) new HomeViewModel();
        } else if (SearchViewModel.class.equals(modelClass)) {
            return (T) new SearchViewModel();
        }
        return null;
    }
}
