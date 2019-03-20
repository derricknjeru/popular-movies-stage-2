package com.derrick.popularmoviesstage2.ui.details;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.derrick.popularmoviesstage2.data.MovieRepository;
import com.derrick.popularmoviesstage2.data.local.ReviewsResult;

import java.util.List;

public class ReviewViewModel extends ViewModel {
    private LiveData<List<ReviewsResult>> mReviewsResult;

    public ReviewViewModel(MovieRepository mRepository, long movie_id) {
        mReviewsResult = mRepository.getReviews(movie_id);
    }

    public LiveData<List<ReviewsResult>> getReviewsResult() {
        return mReviewsResult;
    }

}
