package com.derrick.popularmoviesstage2.data.network;

import android.os.Parcel;
import android.os.Parcelable;

import com.derrick.popularmoviesstage2.data.local.Result;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Movie implements Parcelable {
    @SerializedName("page")
    @Expose
    private Long page;
    @SerializedName("total_results")
    @Expose
    private Long totalResults;
    @SerializedName("total_pages")
    @Expose
    private Long totalPages;
    @SerializedName("results")
    @Expose
    private ArrayList<Result> results = null;
    public final static Parcelable.Creator<Movie> CREATOR = new Creator<Movie>() {

        @SuppressWarnings({
                "unchecked"
        })
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return (new Movie[size]);
        }

    };

    protected Movie(Parcel in) {
        this.page = ((Long) in.readValue((Long.class.getClassLoader())));
        this.totalResults = ((Long) in.readValue((Long.class.getClassLoader())));
        this.totalPages = ((Long) in.readValue((Long.class.getClassLoader())));
        in.readList(this.results, (Result.class.getClassLoader()));
    }

    /**
     * No args constructor for use in serialization
     */
    public Movie() {
    }

    /**
     * @param results
     * @param totalResults
     * @param page
     * @param totalPages
     */
    public Movie(Long page, Long totalResults, Long totalPages, ArrayList<Result> results) {
        super();
        this.page = page;
        this.totalResults = totalResults;
        this.totalPages = totalPages;
        this.results = results;
    }

    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public Long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Long totalResults) {
        this.totalResults = totalResults;
    }

    public Long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Long totalPages) {
        this.totalPages = totalPages;
    }

    public ArrayList<Result> getResults() {
        return results;
    }

    public void setResults(ArrayList<Result> results) {
        this.results = results;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(page);
        dest.writeValue(totalResults);
        dest.writeValue(totalPages);
        dest.writeList(results);
    }

    public int describeContents() {
        return 0;
    }


    @Override
    public String toString() {
        return "Movie{" +
                "page=" + page +
                ", totalResults=" + totalResults +
                ", totalPages=" + totalPages +
                ", results=" + results +
                '}';
    }
}