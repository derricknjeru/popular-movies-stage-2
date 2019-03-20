package com.derrick.popularmoviesstage2.ui.details;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.derrick.popularmoviesstage2.data.MovieRepository;

public class ReviewViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final MovieRepository mRepository;
    private final long movie_id;

    public ReviewViewModelFactory(MovieRepository repository, long movie_id) {
        this.mRepository = repository;
        this.movie_id = movie_id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ReviewViewModel(mRepository, movie_id);
    }
}
