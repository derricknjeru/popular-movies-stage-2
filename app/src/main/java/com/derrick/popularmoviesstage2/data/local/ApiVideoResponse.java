package com.derrick.popularmoviesstage2.data.local;

import java.util.List;

public class ApiVideoResponse {
    private int movie_id;

    public int getMovie_id() {
        return movie_id;
    }

    public ApiVideoResponse(int movie_id, List<VideoResult> videoResults) {
        this.movie_id = movie_id;
        this.videoResults = videoResults;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }

    public List<VideoResult> getVideoResults() {
        return videoResults;
    }

    public void setVideoResults(List<VideoResult> videoResults) {
        this.videoResults = videoResults;
    }

    private List<VideoResult>videoResults;
}
