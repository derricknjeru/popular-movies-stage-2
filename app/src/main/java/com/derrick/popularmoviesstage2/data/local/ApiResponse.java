package com.derrick.popularmoviesstage2.data.local;

import java.util.List;

/**
 * Used to model api response
 */
public class ApiResponse {
    String sorted_value;

    public String getSorted_value() {
        return sorted_value;
    }

    public List<Result> getResults() {
        return results;
    }

    List<Result> results;

    public ApiResponse(String sorted_value, List<Result> results) {
        this.sorted_value = sorted_value;
        this.results = results;
    }


}
