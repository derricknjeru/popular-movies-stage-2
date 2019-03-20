package com.derrick.popularmoviesstage2.data.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MovieDao {
    /**
     * Inserts a list of {@link Result} into the movies table. If there is a conflicting id
     * or movie_id the movie entry uses the {@link OnConflictStrategy} of replacing the movie
     * data. The required uniqueness of these values is defined in the {@link Result}.
     *
     * @param result an object of movies to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] bulkInsertMovies(Result... result);

    /**
     * get all movies
     *
     * @return
     */
    @Query("SELECT * FROM result_table where sortingValue=:sorting_value")
    List<Result> getMovies(String sorting_value);

    /**
     * get all movies
     *
     * @return
     */
    @Query("SELECT * FROM result_table where sortingValue=:sorting_value")
    LiveData<Result> getAllMovies(String sorting_value);


    /**
     * Delete the whole table
     */
    @Query("DELETE FROM result_table")
    void deleteOldMovies();

    /**
     * get a single movie as a livedata object
     */
    @Query("SELECT * FROM result_table where id=:movie_id")
    LiveData<Result> getSingeMovie(long movie_id);

    /**
     * Updating the favourite value
     * By movie id
     */
    @Query("UPDATE result_table SET favourite=:fav WHERE id = :movie_id")
    void updateFavourite(int fav, long movie_id);

    /**
     * getting the fav value
     *
     * @return
     */
    @Query("SELECT favourite from result_table where id=:movie_id")
    int getFabValue(long movie_id);

    /**
     * get all movies
     *
     * @return
     */
    @Query("SELECT * FROM result_table where favourite=:fab")
    List<Result> getAllFabMovies(long fab);
}
