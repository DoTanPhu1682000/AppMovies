package com.phudt7.movie.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.phudt7.movie.model.Reminder;
import com.phudt7.movie.model.Result;

@Database(entities = {Result.class, Reminder.class}, version = 3, exportSchema = false)
public abstract class MoviesDatabase extends RoomDatabase {
    private static MoviesDatabase moviesDatabase;

    public abstract MoviesDAO moviesDAO();

    public static MoviesDatabase getInstance(Context context) {
        if (moviesDatabase == null) {
            moviesDatabase = Room.databaseBuilder(context, MoviesDatabase.class, "Movies.db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return moviesDatabase;
    }
}
