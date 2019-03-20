package com.derrick.popularmoviesstage2.data.local;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Video implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<VideoResult> results = null;
    public final static Parcelable.Creator<Video> CREATOR = new Creator<Video>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        public Video[] newArray(int size) {
            return (new Video[size]);
        }

    };

    protected Video(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.results, (VideoResult.class.getClassLoader()));
    }

    public Video() {
    }


    public List<VideoResult> getVideoResults() {
        return results;
    }

    public void setResults(List<VideoResult> results) {
        this.results = results;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeList(results);
    }

    public int describeContents() {
        return 0;
    }

}
