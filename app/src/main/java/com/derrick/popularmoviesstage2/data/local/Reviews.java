package com.derrick.popularmoviesstage2.data.local;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Reviews implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("page")
    @Expose
    private Integer page;

    @SerializedName("results")
    @Expose
    private List<ReviewsResult> reviewsResultList = null;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;
    public final static Parcelable.Creator<Reviews> CREATOR = new Creator<Reviews>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Reviews createFromParcel(Parcel in) {
            return new Reviews(in);
        }

        public Reviews[] newArray(int size) {
            return (new Reviews[size]);
        }

    };

    protected Reviews(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.page = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.reviewsResultList, (ReviewsResult.class.getClassLoader()));
        this.totalPages = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.totalResults = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public Reviews() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public List<ReviewsResult> getReviewsResultList() {
        return reviewsResultList;
    }

    public void setReviewsResultList(List<ReviewsResult> reviewsResultList) {
        this.reviewsResultList = reviewsResultList;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(page);
        dest.writeList(reviewsResultList);
        dest.writeValue(totalPages);
        dest.writeValue(totalResults);
    }

    public int describeContents() {
        return 0;
    }


    @Override
    public String toString() {
        return "ReviewsActivity{" +
                "id=" + id +
                ", page=" + page +
                ", reviewsResultList=" + reviewsResultList +
                ", totalPages=" + totalPages +
                ", totalResults=" + totalResults +
                '}';
    }
}
