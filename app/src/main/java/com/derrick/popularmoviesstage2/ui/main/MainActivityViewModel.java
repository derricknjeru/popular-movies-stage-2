package com.derrick.popularmoviesstage2.ui.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.derrick.popularmoviesstage2.data.MovieRepository;
import com.derrick.popularmoviesstage2.data.local.Result;
import com.derrick.popularmoviesstage2.utils.LogUtils;

import java.util.List;

/**
 * {@link ViewModel} for {@link MainActivity}
 */

public class MainActivityViewModel extends ViewModel {
    // Note the use of MutableLiveData, this allows changes to be made
    public LiveData<String> noFav;

    public LiveData<String> getNoFav() {
        return noFav;
    }

    public LiveData<Throwable> getRetrofitError() {
        return retrofitError;
    }

    // Note the use of MutableLiveData, this allows changes to be made
    public LiveData<Throwable> retrofitError;

    private static final String LOG_TAG = MainActivityViewModel.class.getSimpleName();

    public LiveData<List<Result>> getMoviesList() {
        return mMoviesList;
    }

    private LiveData<List<Result>> mMoviesList;

    public MainActivityViewModel(MovieRepository repository, String sorting_value) {
        LogUtils.showLog(LOG_TAG, "@Movie MainActivityViewModel sorting_value" + sorting_value);
        mMoviesList = repository.getMoviesLiveData(sorting_value);
        noFav = repository.getNoFar();
        retrofitError = repository.getRetrofitError();

    }
}
