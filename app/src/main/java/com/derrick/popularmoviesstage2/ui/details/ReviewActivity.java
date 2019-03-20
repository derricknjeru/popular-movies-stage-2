package com.derrick.popularmoviesstage2.ui.details;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;

import com.derrick.popularmoviesstage2.R;
import com.derrick.popularmoviesstage2.data.local.ReviewsResult;
import com.derrick.popularmoviesstage2.databinding.ActivityReviewBinding;
import com.derrick.popularmoviesstage2.utils.InjectorUtils;

import java.util.List;

public class ReviewActivity extends AppCompatActivity {
    ActivityReviewBinding mReviewBinding;

    public static final String EXTRA_MOVIE_ID = "movie_id";
    public static final long DEFAULT_ID = -1;
    long movie_id = -1;

    private ReviewViewModel mViewModel;
    private ReviewViewModelFactory factory;
    private ReviewAdapter mReviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mReviewBinding = DataBindingUtil.setContentView(this, R.layout.activity_review);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mReviewBinding.contentReview.reviewList.setLayoutManager(new LinearLayoutManager(this));
        mReviewBinding.contentReview.reviewList.setHasFixedSize(true);

        mReviewAdapter = new ReviewAdapter();
        mReviewBinding.contentReview.reviewList.setAdapter(mReviewAdapter);


        if (getIntent() != null) {
            movie_id = getIntent().getLongExtra(EXTRA_MOVIE_ID, DEFAULT_ID);
        }

        if (movie_id != DEFAULT_ID) {
            factory = InjectorUtils.provideReviewsViewModelFactory(this, movie_id);
            mViewModel = ViewModelProviders.of(this, factory).get(ReviewViewModel.class);

            mViewModel.getReviewsResult().observe(this, new Observer<List<ReviewsResult>>() {
                @Override
                public void onChanged(@Nullable List<ReviewsResult> reviewsResults) {
                    if (reviewsResults != null && reviewsResults.size() > 0) {
                        mReviewAdapter.setReviewsResults(reviewsResults);
                    }
                }
            });

        }
    }
}


