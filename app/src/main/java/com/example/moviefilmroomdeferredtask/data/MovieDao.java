package com.example.moviefilmroomdeferredtask.data;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.moviefilmroomdeferredtask.data.entity.Movie;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * from movie_table ORDER BY id ")
    LiveData<List<Movie>> getMovies();

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    void insert(Movie movie);

    @Query("DELETE FROM movie_table")
    void deleteAll();

    @Query("SELECT * from movie_table")
    List<Movie> getMoviesBase();

    @Update
    void update(Movie movie);

    class MovieRoomDatabase {
    }
}
