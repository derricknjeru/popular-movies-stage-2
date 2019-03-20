package com.derrick.popularmoviesstage2.ui.main;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.derrick.popularmoviesstage2.data.MovieRepository;

/**
 * Factory method that allows us to create a ViewModel with a constructor that takes a
 * {@link com.derrick.popularmoviesstage2.data.MovieRepository} and an Sorting_value for the current {@link com.derrick.popularmoviesstage2.data.local.Result}
 */
public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final MovieRepository mRepository;
    private final String sorting_value;

    public MainViewModelFactory(MovieRepository repository, String sorting_value) {
        this.mRepository = repository;
        this.sorting_value = sorting_value;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainActivityViewModel(mRepository, sorting_value);
    }
}
