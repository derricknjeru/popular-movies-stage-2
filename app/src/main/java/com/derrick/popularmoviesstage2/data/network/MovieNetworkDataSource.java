package com.derrick.popularmoviesstage2.data.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;

import com.derrick.popularmoviesstage2.BuildConfig;
import com.derrick.popularmoviesstage2.data.local.ApiResponse;
import com.derrick.popularmoviesstage2.data.local.ApiReviewResponse;
import com.derrick.popularmoviesstage2.data.local.ApiVideoResponse;
import com.derrick.popularmoviesstage2.data.local.Reviews;
import com.derrick.popularmoviesstage2.data.local.ReviewsResult;
import com.derrick.popularmoviesstage2.data.local.Video;
import com.derrick.popularmoviesstage2.data.local.VideoResult;
import com.derrick.popularmoviesstage2.utils.LogUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.derrick.popularmoviesstage2.data.network.MovieIntentService.DEFAULT_ID;
import static com.derrick.popularmoviesstage2.data.network.MovieIntentService.EXTRA_SERVICE_DATA;

public class MovieNetworkDataSource {

    private static final String LOG_TAG = MovieNetworkDataSource.class.getSimpleName();
    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static MovieNetworkDataSource sInstance;

    private Context mContext;


    // LiveData storing api response
    private final MutableLiveData<ApiResponse> MoviesResponse;
    // Note the use of MutableLiveData, this allows changes to be made
    public MutableLiveData<Throwable> retrofitError = new MutableLiveData<>();


    public MutableLiveData<ApiVideoResponse> videoResultMutableLiveData = new MutableLiveData<>();


    public MutableLiveData<ApiReviewResponse> reviewsResultMutableLiveData = new MutableLiveData<>();


    private MovieNetworkDataSource(Context context) {
        mContext = context;
        MoviesResponse = new MutableLiveData<>();
    }


    public static MovieNetworkDataSource getsInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new MovieNetworkDataSource(context);
            }
        }
        return sInstance;
    }

    void fetchMovies(String sorting_string) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        LogUtils.showLog(LOG_TAG, "@Movies sorting_string::" + sorting_string);

        Call<Movie> call = apiService.getPopularMovies(sorting_string, BuildConfig.API_KEY);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, final Response<Movie> response) {
                //successful api response
                if (response.body().getResults() != null && response.body().getResults().size() > 0) {
                    LogUtils.showLog(LOG_TAG, "@Movies fetched::" + response.body().getResults().get(0).getOriginalTitle());
                    ApiResponse apiResponse = new ApiResponse(sorting_string, response.body().getResults());
                    MoviesResponse.postValue(apiResponse);
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                retrofitError.postValue(t);
            }
        });
    }

    public void StartService(String sorting_value, String action) {
        Intent i = new Intent(mContext, MovieIntentService.class);
        i.setAction(action);
        i.putExtra(EXTRA_SERVICE_DATA, sorting_value);
        mContext.startService(i);
    }

    public MutableLiveData<ApiResponse> getMoviesResponse() {
        return MoviesResponse;
    }

    //TODO create a Jobscheduler
    public void scheduleRecurringFetchMovieSync() {

    }

    public MutableLiveData<Throwable> getRetrofitError() {
        return retrofitError;
    }

    void fetchTrailers(long id) {
        if (id != DEFAULT_ID) {
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<Video> call = apiService.getTrailers(id, BuildConfig.API_KEY);

            call.enqueue(new Callback<Video>() {
                @Override
                public void onResponse(Call<Video> call, Response<Video> response) {
                    if (response.body().getVideoResults() != null && response.body().getVideoResults().size() > 0) {

                        ApiVideoResponse apiResponse = new ApiVideoResponse((int) id, response.body().getVideoResults());

                        videoResultMutableLiveData.postValue(apiResponse);
                    }

                }

                @Override
                public void onFailure(Call<Video> call, Throwable t) {

                }
            });
        }
    }

    void fetchReviews(long id) {
        if (id != DEFAULT_ID) {

            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<Reviews> resultCall = apiService.getReviews(id, BuildConfig.API_KEY);
            resultCall.enqueue(new Callback<Reviews>() {
                @Override
                public void onResponse(Call<Reviews> call, Response<Reviews> response) {
                    LogUtils.showLog(LOG_TAG, "@Review response" + response.body().getReviewsResultList());
                    if (response.body().getReviewsResultList() != null && response.body().getReviewsResultList().size() > 0) {

                        ApiReviewResponse apiResponse = new ApiReviewResponse((int) id, response.body().getReviewsResultList());

                        reviewsResultMutableLiveData.postValue(apiResponse);
                    }

                }

                @Override
                public void onFailure(Call<Reviews> call, Throwable t) {

                }
            });

        }
    }

    //get trailers
    public LiveData<ApiVideoResponse> getVideoResultMutableLiveData() {
        return videoResultMutableLiveData;
    }

    //get trailers
    public LiveData<ApiReviewResponse> getReviewResultMutableLiveData() {
        return reviewsResultMutableLiveData;
    }

}
