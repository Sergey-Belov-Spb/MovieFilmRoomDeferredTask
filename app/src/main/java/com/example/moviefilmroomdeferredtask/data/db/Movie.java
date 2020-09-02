package com.example.moviefilmroomdeferredtask.data.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movie_table")
public class Movie {

    @PrimaryKey (autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    public long id;
    public String title;
    public String picUrl;
    public Boolean favorite;
    /*@ColumnInfo(name = "movie")
    private String mMovie;

    public Movie(@NonNull String movie) { this.mMovie = movie; }

    @NonNull
    public String getMovie() { return this.mMovie; }*/
}