package com.derrick.popularmoviesstage2.data.local;

import java.util.List;

/**
 * Used to model reviews api response
 */
public class ApiReviewResponse {
    public int getMovie_id() {
        return movie_id;
    }

    public ApiReviewResponse(int movie_id, List<ReviewsResult> reviewsResults) {
        this.movie_id = movie_id;
        this.reviewsResults = reviewsResults;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }

    public List<ReviewsResult> getReviewsResults() {
        return reviewsResults;
    }

    public void setReviewsResults(List<ReviewsResult> reviewsResults) {
        this.reviewsResults = reviewsResults;
    }

    private int movie_id;
    private List<ReviewsResult> reviewsResults;
}
