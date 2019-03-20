package com.derrick.popularmoviesstage2.data.network;

import com.derrick.popularmoviesstage2.data.local.Reviews;
import com.derrick.popularmoviesstage2.data.local.ReviewsResult;
import com.derrick.popularmoviesstage2.data.local.Video;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("movie/{sorting_string}")
    Call<Movie> getPopularMovies(@Path("sorting_string") String sorting_string, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<Video> getTrailers(@Path("id") long id, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<Reviews> getReviews(@Path("id") long id, @Query("api_key") String apiKey);


}
