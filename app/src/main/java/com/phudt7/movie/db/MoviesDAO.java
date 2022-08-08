package com.phudt7.movie.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.phudt7.movie.model.Reminder;
import com.phudt7.movie.model.Result;

import java.util.List;

@Dao
public interface MoviesDAO {

    @Query("SELECT * FROM movies WHERE id = :moviesId")
    Result getMoviesId(int moviesId);

    @Query("SELECT * FROM movies")
    List<Result> getMovies();

    @Insert
    void insertMovies(Result... results);

    @Update
    void updateMovies(Result... results);

    @Delete
    void deleteMovies(Result... results);

    @Query("SELECT COUNT(*) FROM movies")
    int coutMovie();

    @Insert
    void insertReminder(Reminder... reminders);

    @Query("SELECT * FROM reminder")
    List<Reminder> getReminder();

    @Delete
    void deleteReminder(Reminder... reminders);
}
