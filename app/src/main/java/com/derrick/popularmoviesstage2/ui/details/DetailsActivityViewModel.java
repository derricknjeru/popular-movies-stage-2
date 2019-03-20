package com.derrick.popularmoviesstage2.ui.details;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.derrick.popularmoviesstage2.data.MovieRepository;
import com.derrick.popularmoviesstage2.data.local.Result;
import com.derrick.popularmoviesstage2.data.local.ReviewsResult;
import com.derrick.popularmoviesstage2.data.local.VideoResult;

import java.util.List;

public class DetailsActivityViewModel extends ViewModel {
    public LiveData<Result> getSingleMovie() {
        return mSingleMovie;
    }

    private LiveData<Result> mSingleMovie;
    private LiveData<List<VideoResult>> mVideoResult;

    public LiveData<List<VideoResult>> getVideoResult() {
        return mVideoResult;
    }

    public LiveData<List<ReviewsResult>> getReviewsResult() {
        return mReviewsResult;
    }

    private LiveData<List<ReviewsResult>> mReviewsResult;

    public DetailsActivityViewModel(MovieRepository mRepository, long movie_id) {
        mSingleMovie = mRepository.getSingleMovie(movie_id);
        mReviewsResult = mRepository.getReviews(movie_id);
        mVideoResult = mRepository.getTrailers(movie_id);
    }
}
