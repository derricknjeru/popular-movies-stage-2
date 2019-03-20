package com.derrick.popularmoviesstage2.data;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import com.derrick.popularmoviesstage2.R;
import com.derrick.popularmoviesstage2.data.local.ApiResponse;
import com.derrick.popularmoviesstage2.data.local.ApiReviewResponse;
import com.derrick.popularmoviesstage2.data.local.ApiVideoResponse;
import com.derrick.popularmoviesstage2.data.local.MovieDao;
import com.derrick.popularmoviesstage2.data.local.MovieDatabase;
import com.derrick.popularmoviesstage2.data.local.MoviePreferences;
import com.derrick.popularmoviesstage2.data.local.Result;
import com.derrick.popularmoviesstage2.data.local.ReviewsDao;
import com.derrick.popularmoviesstage2.data.local.ReviewsResult;
import com.derrick.popularmoviesstage2.data.local.TrailersDao;
import com.derrick.popularmoviesstage2.data.local.VideoResult;
import com.derrick.popularmoviesstage2.data.network.MovieNetworkDataSource;
import com.derrick.popularmoviesstage2.utils.AppExecutors;
import com.derrick.popularmoviesstage2.utils.LogUtils;

import java.util.List;

public class MovieRepository {
    private static final String LOG_TAG = MovieRepository.class.getSimpleName();
    private boolean mInitialized = false;

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static MovieRepository sInstance;

    private final MovieDao mMovieDao;
    private final TrailersDao mTrailersDao;
    private final ReviewsDao mReviewsDao;
    private final MovieNetworkDataSource mMovieNetworkDataSource;
    private AppExecutors mAppExecutors;
    private Context mContext;


    // Note the use of MutableLiveData, this allows changes to be made
    @NonNull
    private MutableLiveData<List<Result>> moviesLiveData = new MutableLiveData<>();

    // Note the use of MutableLiveData, this allows changes to be made
    public MutableLiveData<String> noFav = new MutableLiveData<>();

    // Note the use of MutableLiveData, this allows changes to be made
    public MutableLiveData<Throwable> retrofitError = new MutableLiveData<>();


    private MovieRepository(final MovieDatabase movieDb, MovieNetworkDataSource movieNetworkDataSource, AppExecutors appExecutors, Context context) {
        mMovieDao = movieDb.movieDao();
        mTrailersDao = movieDb.trailersDao();
        mReviewsDao = movieDb.reviewsDao();
        mMovieNetworkDataSource = movieNetworkDataSource;
        mAppExecutors = appExecutors;
        mContext = context;
        saveMovies();
        saveTrailers();
        saveReviews();
    }

    /**
     * Saving movies to local database
     */
    private void saveMovies() {
        LiveData<ApiResponse> apiMovies = mMovieNetworkDataSource.getMoviesResponse();
        apiMovies.observeForever(apiResponse -> {
            for (int i = 0; i < apiResponse.getResults().size(); i++) {
                Result result = apiResponse.getResults().get(i);

                result.setSortingValue(apiResponse.getSorted_value());
                mAppExecutors.diskIO().execute(() -> {
                    //saving to database
                    long[] insert = mMovieDao.bulkInsertMovies(result);

                    LogUtils.showLog(LOG_TAG, "@Movie insert" + insert);
                    //positing saved data to MutableLiveData
                    moviesLiveData.postValue(mMovieDao.getMovies(apiResponse.getSorted_value()));

                });


            }

        });


        retrofitError = mMovieNetworkDataSource.getRetrofitError();


    }

    /**
     * Saving review to local database
     */
    private void saveReviews() {
        LiveData<ApiReviewResponse> reviewResultLiveData = mMovieNetworkDataSource.getReviewResultMutableLiveData();
        //saving trailers

        reviewResultLiveData.observeForever(apiReviewResponse -> {
            List<ReviewsResult> reviewsResults = apiReviewResponse.getReviewsResults();
            for (int i = 0; i < reviewsResults.size(); i++) {

                ReviewsResult reviewsResult = reviewsResults.get(i);
                reviewsResult.setVideo_id(String.valueOf(apiReviewResponse.getMovie_id()));

                mAppExecutors.diskIO().execute(() -> {
                    long[] insert = mReviewsDao.bulkInsert(reviewsResult);
                    LogUtils.showLog(LOG_TAG, "@ReviewsActivity insert" + insert);

                });


            }

        });
    }

    /**
     * Saving trailers to local database
     */
    private void saveTrailers() {
        LiveData<ApiVideoResponse> videoResultLiveData = mMovieNetworkDataSource.getVideoResultMutableLiveData();
        //saving trailers

        videoResultLiveData.observeForever(apiVideoResponse -> {
            List<VideoResult> videoResults = apiVideoResponse.getVideoResults();
            for (int i = 0; i < videoResults.size(); i++) {

                VideoResult videoResult = videoResults.get(i);
                videoResult.setVideo_id(apiVideoResponse.getMovie_id());

                mAppExecutors.diskIO().execute(() -> {
                    long[] insert = mTrailersDao.bulkInsertTrailers(videoResult);
                    LogUtils.showLog(LOG_TAG, "@Detail insert" + insert);

                });


            }

        });
    }

    public synchronized static MovieRepository getsInstance(MovieDatabase mMovieDao, MovieNetworkDataSource networkDataSource, AppExecutors appExecutors, Context context) {
        if (sInstance == null) {
            //new repository is created
            synchronized (LOCK) {
                sInstance = new MovieRepository(mMovieDao, networkDataSource, appExecutors, context);
            }

        }
        return sInstance;
    }


    /**
     * Creates periodic sync tasks and checks to see if an immediate sync is required. If an
     * immediate sync is required, this method will take care of making sure that sync occurs.
     */
    private synchronized void initializeData(String mSortingValue) {

        scheduleRecurringFetchMovieSync();

        mAppExecutors.diskIO().execute(() -> {
            if (!localDbHasData(mSortingValue)) {
                if (!mSortingValue.contentEquals(mContext.getString(R.string.pref_sorting_favourite))) {
                    LogUtils.showLog(LOG_TAG, "@Movie calling movie service");
                    callService(mSortingValue, mContext.getString(R.string.action_fetch_movies));
                } else {
                    noFavoriteMovies();
                }
            } else {
                //There are movies in the local db
                LogUtils.showLog(LOG_TAG, "@Movie not calling movie service");
                if (mSortingValue.contentEquals(mContext.getString(R.string.pref_sorting_favourite))) {
                    //fetching favorite movies
                    moviesLiveData.postValue(mMovieDao.getAllFabMovies(1));
                } else {
                    moviesLiveData.postValue(mMovieDao.getMovies(mSortingValue));
                }
            }
        });


    }


    public void noFavoriteMovies() {
        noFav.postValue(mContext.getString(R.string.no_fav));
    }

    public MutableLiveData<Throwable> getRetrofitError() {
        return retrofitError;
    }


    public MutableLiveData<String> getNoFar() {
        return noFav;
    }


    private void scheduleRecurringFetchMovieSync() {
        // Only perform initialization once per app lifetime. If initialization has already been
        // performed, we have nothing to do in this method.
        LogUtils.showLog(LOG_TAG, "@Movie mInitialized" + mInitialized);
        if (!mInitialized) {
            // This method call triggers Sunshine to create its task to synchronize movie data
            // periodically.
            mMovieNetworkDataSource.scheduleRecurringFetchMovieSync();
            mInitialized = true;
        }
    }

    public LiveData<List<Result>> getMoviesLiveData(String mSortingValue) {
        fetchMovies(mSortingValue);
        return moviesLiveData;
    }

    public void fetchMovies(String mSortingValue) {
        initializeData(mSortingValue);
    }


    public LiveData<Result> getSingleMovie(long movie_id) {
        return mMovieDao.getSingeMovie(movie_id);
    }

    //check if local db has movies from @param mSortingValue
    private boolean localDbHasData(String mSortingValue) {
        boolean hasData;
        if (mSortingValue.contentEquals(mContext.getString(R.string.pref_sorting_favourite))) {
            //fetching favorite movies
            hasData = mMovieDao.getAllFabMovies(1) != null && mMovieDao.getAllFabMovies(1).size() != 0;
        } else {
            //fetching favorite movies or popular
            hasData = mMovieDao.getMovies(mSortingValue) != null && mMovieDao.getMovies(mSortingValue).size() != 0;
        }

        return hasData;
    }

    /**
     * Getting fav value
     *
     * @param movie_id
     * @return
     */
    public int getFabValue(long movie_id) {
        return mMovieDao.getFabValue(movie_id);
    }

    /**
     * Updating fab value
     *
     * @param movie_id
     */
    public void upDateFabValue(int fab, long movie_id) {
        mMovieDao.updateFavourite(fab, movie_id);

         //if favorite menu is selected, we post value

        if (MoviePreferences.getSortingQuery(mContext, mContext.getString(R.string.pref_sorting_key)).contentEquals(mContext.getString(R.string.pref_sorting_favourite))) {
            moviesLiveData.postValue(mMovieDao.getAllFabMovies(1));
        }
    }

    public void callService(String extra_data, String action) {
        mMovieNetworkDataSource.StartService(extra_data, action);
    }

    public LiveData<List<VideoResult>> getTrailers(long id) {
        initializeTrailers(id);
        return mTrailersDao.getTrailers(id);
    }

    private void initializeTrailers(long id) {
        mAppExecutors.diskIO().execute(() -> {
            if (!movieHasTrailers(id)) {
                LogUtils.showLog(LOG_TAG, "@Details videos calling service::");
                callService(String.valueOf(id), mContext.getString(R.string.action_fetch_trailers));
            } else {
                LogUtils.showLog(LOG_TAG, "@Details videos not calling service::");

            }
        });

    }


    private boolean movieHasTrailers(long id) {
        return mTrailersDao.getTrailersList(id) != null && mTrailersDao.getTrailersList(id).size() > 0;
    }

    private boolean movieHasReviews(long id) {
        return mReviewsDao.getReviewsList(String.valueOf(id)) != null && mReviewsDao.getReviewsList(String.valueOf(id)).size() > 0;
    }


    public LiveData<List<ReviewsResult>> getReviews(long id) {
        initializeReviews(id);
        return mReviewsDao.getReviews(String.valueOf(id));
    }

    private void initializeReviews(long id) {
        mAppExecutors.diskIO().execute(() -> {
            if (!movieHasReviews(id)) {
                LogUtils.showLog(LOG_TAG, "@Details reviews calling service::");
                callService(String.valueOf(id), mContext.getString(R.string.action_fetch_reviews));
            } else {
                LogUtils.showLog(LOG_TAG, "@Details reviews not calling service::");

            }
        });

    }

}
