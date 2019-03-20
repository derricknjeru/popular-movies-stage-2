package com.derrick.popularmoviesstage2.data.local;

import android.arch.persistence.room.TypeConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link TypeConverter} for String to {@link List}
 * <p>
 * This stores the genres as a String in the database, but returns it as a {@link List}
 */

public class GenreConverter {
    @TypeConverter
    public List<Long> gettingListFromString(String genreIds) {
        List<Long> list = new ArrayList<>();

        String[] array = genreIds.split(",");

        for (String s : array) {
            if (!s.isEmpty()) {
                list.add(Long.parseLong(s));
            }
        }
        return list;
    }

    @TypeConverter
    public String writingStringFromList(List<Long> list) {
        String genreIds = "";
        for (long i : list) {
            genreIds += "," + i;
        }
        return genreIds;
    }
}