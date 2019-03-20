package com.derrick.popularmoviesstage2.data.network;

import android.app.IntentService;
import android.content.Intent;

import com.derrick.popularmoviesstage2.R;
import com.derrick.popularmoviesstage2.utils.InjectorUtils;

public class MovieIntentService extends IntentService {


    private static final String LOG_TAG = MovieIntentService.class.getSimpleName();
    public static final String EXTRA_SERVICE_DATA = "extra_service_data";
    public static final long DEFAULT_ID = -1;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     * <p>
     * LOG_TAG Used to name the worker thread, important only for debugging.
     */
    public MovieIntentService() {
        super(LOG_TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();

        if (action.contentEquals(getString(R.string.action_fetch_movies))) {
            fetchMovies(intent);
        }
        if (action.contentEquals(getString(R.string.action_fetch_trailers))) {
            fetchTrailer(intent);
        }
        if (action.contentEquals(getString(R.string.action_fetch_reviews))) {
            fetchReviews(intent);
        }

    }

    private void fetchReviews(Intent intent) {
        if (intent != null) {
            String movie_id = intent.getStringExtra(EXTRA_SERVICE_DATA);
            InjectorUtils.provideNetworkDataSource(this).fetchReviews(Long.parseLong(movie_id));
        }
    }

    private void fetchTrailer(Intent intent) {
        if (intent != null) {
            String movie_id = intent.getStringExtra(EXTRA_SERVICE_DATA);
            InjectorUtils.provideNetworkDataSource(this).fetchTrailers(Long.parseLong(movie_id));
        }
    }

    private void fetchMovies(Intent intent) {
        if (intent != null) {
            String sorting = intent.getStringExtra(EXTRA_SERVICE_DATA);
            InjectorUtils.provideNetworkDataSource(this).fetchMovies(sorting);
        }
    }
}
