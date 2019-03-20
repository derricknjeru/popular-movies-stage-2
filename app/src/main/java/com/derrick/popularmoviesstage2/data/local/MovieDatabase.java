package com.derrick.popularmoviesstage2.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.derrick.popularmoviesstage2.utils.LogUtils;

@Database(entities = {Result.class, VideoResult.class, ReviewsResult.class}, version = 1, exportSchema = false)
@TypeConverters({GenreConverter.class})
public abstract class MovieDatabase extends RoomDatabase {

    private static final String LOG_TAG = MovieDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "movie_db";

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static MovieDatabase sInstance;

    public static MovieDatabase getInstance(Context context) {
        LogUtils.showLog(LOG_TAG, "Getting the database");
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(context, MovieDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration().build();
        }

        return sInstance;
    }

    // The associated DAOs for the database
    public abstract MovieDao movieDao();

    public abstract TrailersDao trailersDao();

    public abstract ReviewsDao reviewsDao();
}
