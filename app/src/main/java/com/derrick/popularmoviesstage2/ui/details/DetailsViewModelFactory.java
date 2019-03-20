package com.derrick.popularmoviesstage2.ui.details;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.derrick.popularmoviesstage2.data.MovieRepository;

public class DetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final MovieRepository mRepository;
    private final long movie_id;

    public DetailsViewModelFactory(MovieRepository repository, long movie_id) {
        this.mRepository = repository;
        this.movie_id = movie_id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailsActivityViewModel(mRepository, movie_id);
    }
}
