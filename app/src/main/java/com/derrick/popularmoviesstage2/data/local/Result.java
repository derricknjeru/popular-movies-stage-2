package com.derrick.popularmoviesstage2.data.local;

import java.util.ArrayList;
import java.util.List;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "result_table")
public class Result implements Parcelable {
    @SerializedName("vote_count")
    @Expose
    private Long voteCount;
    @NonNull
    @SerializedName("id")
    @Expose
    @PrimaryKey
    private Long id;
    @SerializedName("video")
    @Expose
    private Boolean video;
    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("popularity")
    @Expose
    private Double popularity;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("original_language")
    @Expose
    private String originalLanguage;
    @SerializedName("original_title")
    @Expose
    private String originalTitle;
    @SerializedName("genre_ids")
    @Expose
    private List<Long> genreIds = new ArrayList<>();
    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;
    @SerializedName("adult")
    @Expose
    private Boolean adult;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;

    @SerializedName("sorting_value")
    @Expose
    private String sortingValue;

    @SerializedName("favourite")
    @Expose
    private int favourite = 0;
    public final static Parcelable.Creator<Result> CREATOR = new Creator<Result>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        public Result[] newArray(int size) {
            return (new Result[size]);
        }

    };

    protected Result(Parcel in) {
        this.voteCount = ((Long) in.readValue((Long.class.getClassLoader())));
        this.id = ((Long) in.readValue((Long.class.getClassLoader())));
        this.video = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.voteAverage = ((Double) in.readValue((Double.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.popularity = ((Double) in.readValue((Double.class.getClassLoader())));
        this.posterPath = ((String) in.readValue((String.class.getClassLoader())));
        this.originalLanguage = ((String) in.readValue((String.class.getClassLoader())));
        this.originalTitle = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.genreIds, (java.lang.Long.class.getClassLoader()));
        this.backdropPath = ((String) in.readValue((String.class.getClassLoader())));
        this.adult = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.overview = ((String) in.readValue((String.class.getClassLoader())));
        this.releaseDate = ((String) in.readValue((String.class.getClassLoader())));
        this.sortingValue = ((String) in.readValue((String.class.getClassLoader())));
        this.favourite = in.readInt();
    }

    /**
     * No args constructor for use in serialization
     * We use ignore because room does not need this constructor
     */
    @Ignore
    public Result() {
    }

    /**
     * @param genreIds
     * @param id
     * @param title
     * @param releaseDate
     * @param overview
     * @param posterPath
     * @param originalTitle
     * @param voteAverage
     * @param originalLanguage
     * @param adult
     * @param backdropPath
     * @param voteCount
     * @param video
     * @param popularity
     */
    public Result(Long voteCount, Long id, Boolean video, Double voteAverage, String title, Double popularity, String posterPath, String originalLanguage, String originalTitle, List<Long> genreIds, String backdropPath, Boolean adult, String overview, String releaseDate, String sortingValue, Integer favourite) {
        super();
        this.voteCount = voteCount;
        this.id = id;
        this.video = video;
        this.voteAverage = voteAverage;
        this.title = title;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.genreIds = genreIds;
        this.backdropPath = backdropPath;
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.sortingValue = sortingValue;
        this.favourite = favourite;
    }

    public Long getVoteCount() {
        return voteCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getVideo() {
        return video;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public Double getPopularity() {
        return popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public List<Long> getGenreIds() {
        return genreIds;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public Boolean getAdult() {
        return adult;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getSortingValue() {
        return sortingValue;
    }

    public void setSortingValue(String sortingValue) {
        this.sortingValue = sortingValue;
    }

    public int getFavourite() {
        return favourite;
    }

    public void setFavourite(int favourite) {
        this.favourite = favourite;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(voteCount);
        dest.writeValue(id);
        dest.writeValue(video);
        dest.writeValue(voteAverage);
        dest.writeValue(title);
        dest.writeValue(popularity);
        dest.writeValue(posterPath);
        dest.writeValue(originalLanguage);
        dest.writeValue(originalTitle);
        dest.writeList(genreIds);
        dest.writeValue(backdropPath);
        dest.writeValue(adult);
        dest.writeValue(overview);
        dest.writeValue(releaseDate);
        dest.writeValue(sortingValue);
        dest.writeValue(favourite);
    }

    public int describeContents() {
        return 0;
    }


}
