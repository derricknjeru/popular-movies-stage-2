package com.derrick.popularmoviesstage2.data.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface TrailersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] bulkInsertTrailers(VideoResult... result);

    @Query("SELECT * FROM trailers where video_id=:id")
    LiveData<List<VideoResult>> getTrailers(long id);

    @Query("SELECT * FROM trailers where video_id=:id")
    List<VideoResult> getTrailersList(long id);

    @Query("SELECT * FROM trailers")
    List<VideoResult> getAllTrailersList();
}
