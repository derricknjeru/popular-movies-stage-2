package com.derrick.popularmoviesstage2.data.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ReviewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] bulkInsert(ReviewsResult... results);

    @Query("SELECT * FROM reviews_table where video_id=:id")
    List<ReviewsResult> getReviewsList(String id);

    @Query("SELECT * FROM reviews_table where video_id=:id")
    LiveData<List<ReviewsResult>> getReviews(String id);
}
