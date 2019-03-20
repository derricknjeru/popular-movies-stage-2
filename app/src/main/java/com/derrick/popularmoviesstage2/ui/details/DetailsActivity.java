package com.derrick.popularmoviesstage2.ui.details;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.derrick.popularmoviesstage2.R;
import com.derrick.popularmoviesstage2.data.local.Result;
import com.derrick.popularmoviesstage2.data.local.ReviewsResult;
import com.derrick.popularmoviesstage2.data.local.VideoResult;
import com.derrick.popularmoviesstage2.databinding.ActivityDetailsBinding;
import com.derrick.popularmoviesstage2.utils.AppBarStateChangeListener;
import com.derrick.popularmoviesstage2.utils.AppExecutors;
import com.derrick.popularmoviesstage2.utils.Base_urls;
import com.derrick.popularmoviesstage2.utils.InjectorUtils;
import com.derrick.popularmoviesstage2.utils.LogUtils;
import com.derrick.popularmoviesstage2.utils.PaletteTransformation;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.derrick.popularmoviesstage2.ui.details.ReviewActivity.EXTRA_MOVIE_ID;


public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_MOVIE_DATA = "movie_data";
    private static final String LOG_TAG = DetailsActivity.class.getSimpleName();

    //data binding instance
    private ActivityDetailsBinding mDetailsBinding;

    private long movie_id;

    private DetailsActivityViewModel mViewModel;
    private VideoAdapter mVideoAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_details);

        setSupportActionBar(mDetailsBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();

        if (i != null && i.hasExtra(EXTRA_MOVIE_DATA)) {
            movie_id = getIntent().getLongExtra(EXTRA_MOVIE_DATA, -1);
        }

        mDetailsBinding.contentLayout.reviewLayout.viewMore.setOnClickListener(this);

        LogUtils.showLog(LOG_TAG, "@Movie movie_id" + movie_id);

        DetailsViewModelFactory factory = InjectorUtils.provideDetailViewModelFactory(getApplicationContext(), movie_id);
        mViewModel = ViewModelProviders.of(this, factory).get(DetailsActivityViewModel.class);
        mVideoAdapter = new VideoAdapter(this);
        mDetailsBinding.contentLayout.videoList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mDetailsBinding.contentLayout.videoList.setAdapter(mVideoAdapter);

        getMovieDetails();


    }

    private void getMovieDetails() {
        //fetch movies initial details
        mViewModel.getSingleMovie().observe(this, result -> {
            if (result == null) {
                // movie data unavailable
                closeOnError();
                return;
            }

            populateUI(result);
            mDetailsBinding.collapsingToolbar.setTitle(result.getOriginalTitle());
        });

        //set favorite status
        settingFavouriteMovie();
        //fetching  videos
        mViewModel.getVideoResult().observe(this, videoResults -> {
            if (videoResults != null && videoResults.size() > 0) {
                populateVideoUI(videoResults);
            }
        });
        //fetching reviews
        mViewModel.getReviewsResult().observe(this, reviewsResults -> {
            if (reviewsResults != null && reviewsResults.size() > 0) {
                populateReviewsUI(reviewsResults);
            } else {
                mDetailsBinding.contentLayout.reviewLayout.cardView.setVisibility(View.GONE);
                mDetailsBinding.contentLayout.reviewLayout.cardView2.setVisibility(View.GONE);
                mDetailsBinding.contentLayout.reviewLayout.viewMore.setVisibility(View.GONE);
            }
        });
    }

    private void populateReviewsUI(List<ReviewsResult> reviewsResults) {
        LogUtils.showLog(LOG_TAG, "@Details reviewsResults size::" + reviewsResults.size());
        if (reviewsResults.size() == 1 || reviewsResults.size() > 1) {
            mDetailsBinding.contentLayout.reviewLayout.cardView.setVisibility(View.VISIBLE);
            ReviewsResult author1 = reviewsResults.get(0);
            mDetailsBinding.contentLayout.reviewLayout.authorName.setText(author1.getAuthor());
            mDetailsBinding.contentLayout.reviewLayout.authorInitialChar.setText(author1.getAuthor().substring(0, 1).toUpperCase());
            mDetailsBinding.contentLayout.reviewLayout.authorReview.setText(author1.getContent());

        } else {
            mDetailsBinding.contentLayout.reviewLayout.cardView.setVisibility(View.GONE);
        }
        if (reviewsResults.size() == 2 || reviewsResults.size() > 2) {
            mDetailsBinding.contentLayout.reviewLayout.cardView2.setVisibility(View.VISIBLE);
            ReviewsResult author2 = reviewsResults.get(1);
            mDetailsBinding.contentLayout.reviewLayout.authorName2.setText(author2.getAuthor());
            mDetailsBinding.contentLayout.reviewLayout.authorInitialChar2.setText(author2.getAuthor().substring(0, 1).toUpperCase());
            mDetailsBinding.contentLayout.reviewLayout.authorReview2.setText(author2.getContent());
        } else {
            mDetailsBinding.contentLayout.reviewLayout.cardView2.setVisibility(View.GONE);
        }

        if (reviewsResults.size() > 3) {
            mDetailsBinding.contentLayout.reviewLayout.viewMore.setVisibility(View.VISIBLE);
        } else {
            mDetailsBinding.contentLayout.reviewLayout.viewMore.setVisibility(View.GONE);
        }
    }

    private void populateVideoUI(List<VideoResult> videoResults) {
        mVideoAdapter.setVideoResults(videoResults);
    }

    private void settingFavouriteMovie() {
        fabButtonListener();
        mDetailsBinding.appbar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state.equals(State.EXPANDED)) {
                    checkFavouriteFromDM();
                }

            }
        });
    }

    private void fabButtonListener() {
        mDetailsBinding.fab.setOnClickListener(v -> AppExecutors.getInstance().diskIO().execute(() -> {
            int fabValue = InjectorUtils.provideRepository(getApplicationContext()).getFabValue(movie_id);
            int updateValue;
            LogUtils.showLog(LOG_TAG, "@Movie fabvalue inside" + fabValue);
            if (fabValue == 0) {
                setFavourite(R.drawable.ic_favorite_selected);
                updateValue = 1;
            } else {
                setFavourite(R.drawable.ic_favorite_default);
                updateValue = 0;
            }
            updateDatabase(updateValue);


        }));
    }

    private void updateDatabase(int updateValue) {
        String message;
        if (updateValue == 1) {
            message = getString(R.string.add_fav);
        } else {
            message = getString(R.string.remove_fab);
        }
        InjectorUtils.provideRepository(getApplicationContext()).upDateFabValue(updateValue, movie_id);
        Snackbar.make(mDetailsBinding.detailsContent, message, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Hiding and showing the button because there is a bug in the material design library 28.0
     * which is already reported. see link below
     *
     * @Soln for {@link @https://issuetracker.google.com/issues/111316656 }
     */
    private void setFavourite(int backgroundImage) {
        runOnUiThread(() -> {
            // WORK on UI thread here
            mDetailsBinding.fab.hide();
            mDetailsBinding.fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), backgroundImage));
            mDetailsBinding.fab.show();

        });
    }

    private void checkFavouriteFromDM() {
        AppExecutors.getInstance().diskIO().execute(() -> {
            int fabValue = InjectorUtils.provideRepository(getApplicationContext()).getFabValue(movie_id);
            LogUtils.showLog(LOG_TAG, "@Movie fabvalue" + fabValue);

            if (fabValue == 0) {
                setFavourite(R.drawable.ic_favorite_default);
            } else {
                setFavourite(R.drawable.ic_favorite_selected);
            }

        });
    }


    private void populateUI(Result result) {
        if (!TextUtils.isEmpty(result.getPosterPath())) {
            //loading poster
            Picasso.get().load(Base_urls.TMDB_IMG_BASE_URL + result.getPosterPath()).into(mDetailsBinding.contentLayout.posterImg);
        }
        if (!TextUtils.isEmpty(result.getBackdropPath())) {
            //loading poster
            Picasso.get()
                    .load(Base_urls.TMDB_IMG_BASE_URL + result.getBackdropPath())
                    .fit().centerCrop()
                    .transform(PaletteTransformation.instance())
                    .into(mDetailsBinding.backdropImg, new Callback.EmptyCallback() {
                        @Override
                        public void onSuccess() {
                            setToolbarColor();

                        }
                    });
        }

        if (result.getVoteAverage() != 0.0d) {
            mDetailsBinding.contentLayout.ratingTv.setVisibility(View.VISIBLE);
            mDetailsBinding.contentLayout.ratingValueTv.setVisibility(View.VISIBLE);
            mDetailsBinding.contentLayout.ratingValueTv.setText(Double.toString(result.getVoteAverage()));
        } else {
            mDetailsBinding.contentLayout.ratingTv.setVisibility(View.GONE);
            mDetailsBinding.contentLayout.ratingValueTv.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(result.getReleaseDate())) {
            mDetailsBinding.contentLayout.releaseDateTv.setVisibility(View.GONE);
            mDetailsBinding.contentLayout.releaseDateValueTv.setVisibility(View.GONE);
        } else {
            mDetailsBinding.contentLayout.releaseDateTv.setVisibility(View.VISIBLE);
            mDetailsBinding.contentLayout.releaseDateValueTv.setVisibility(View.VISIBLE);
            mDetailsBinding.contentLayout.releaseDateValueTv.setText(result.getReleaseDate());
        }

        if (TextUtils.isEmpty(result.getOverview())) {
            mDetailsBinding.contentLayout.synopsisTv.setVisibility(View.GONE);
        } else {
            mDetailsBinding.contentLayout.synopsisTv.setVisibility(View.VISIBLE);
            mDetailsBinding.contentLayout.synopsisTv.setText(result.getOverview());
        }
    }

    // Set the background and text colors of a toolbar given a uisng backdrop bitmap image to match
    private void setToolbarColor() {

        Bitmap bitmap = ((BitmapDrawable) mDetailsBinding.backdropImg.getDrawable()).getBitmap();

        /*
         * Pelette from {@link PaletteTransformation class}
         */
        Palette palette = PaletteTransformation.getPalette(bitmap);

        //getting  the vibrant swatch
        Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();

        // Load default colors
        int backgroundColor = ContextCompat.getColor(getApplicationContext(),
                R.color.colorPrimary);
        int textColor = ContextCompat.getColor(getApplicationContext(),
                R.color.default_title_color);

        int statusColor = ContextCompat.getColor(getApplicationContext(), R.color.black_trans80);


        // Check that the Vibrant swatch is available
        if (vibrantSwatch != null) {
            backgroundColor = vibrantSwatch.getRgb();
            textColor = vibrantSwatch.getTitleTextColor();
        }

        // Set the title text colors
        mDetailsBinding.toolbar.setTitleTextColor(textColor);

        //setting status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(statusColor);
        }
        /*
         * setting toolbar ScrimColor
         */
        mDetailsBinding.collapsingToolbar.setContentScrimColor(backgroundColor);
        mDetailsBinding.collapsingToolbar.setStatusBarScrimColor(statusColor);
        //  mDetailsBinding.fab.setBackgroundTintList(ContextCompat.getColorStateList(this, backgroundColor));
        ViewCompat.setBackgroundTintList(
                mDetailsBinding.fab,
                ColorStateList.valueOf(backgroundColor));

        mDetailsBinding.contentLayout.divider.setBackgroundColor(backgroundColor);


    }

    private void closeOnError() {
        finish();
        Snackbar.make(mDetailsBinding.detailsContent, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    void openReviewsPage() {
        Intent i = new Intent(DetailsActivity.this, ReviewActivity.class);
        i.putExtra(EXTRA_MOVIE_ID, movie_id);
        startActivity(i);
    }

    @Override
    public void onClick(View v) {
        if (mDetailsBinding.contentLayout.reviewLayout.viewMore == v) {
            openReviewsPage();
        }
    }
}
