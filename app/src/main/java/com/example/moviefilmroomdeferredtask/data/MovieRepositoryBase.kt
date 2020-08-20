package com.example.moviefilmroomdeferredtask.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.moviefilmroomdeferredtask.data.entity.Movie
import com.example.moviefilmroomdeferredtask.data.entity.MovieItem


class MovieRepositoryBase(application: Application) {
    private var mMovieDao: MovieDao? = null
    private var mAllMovies: LiveData<List<Movie>>? = null
    private var mAllMovies2: List<Movie>? = null

    init {
        val db =
            MovieRoomDatabase.getDatabase(application)
        mMovieDao = db.movieDao()
        mAllMovies = mMovieDao?.getMovies()
    }

    fun getmAllMovie(): LiveData<List<Movie>>? {
        return mAllMovies
    }

    fun getmAllMovie2(): List<Movie>? {
        return mMovieDao?.getMoviesBase()
    }

    fun AddToBase(newBase: List<MovieItem>) {
        val lenBase=newBase.size;
        Log.d("Base","AddToBase len= $lenBase")
        for (i in 0 ..(newBase.size-1)){
            val str = newBase[i].title
            Log.d("Base","name = $str")
            val movie = Movie()
            //movie.id = 1
            movie.favorite = false
            movie.title = newBase[i].title
            movie.picUrl = newBase[i].gitUrl
            MovieRoomDatabase.databaseWriteExecutor.execute {
                mMovieDao!!.insert(movie)
            }
        }
    }

    fun insert(movie: Movie?) {
        MovieRoomDatabase.databaseWriteExecutor.execute {
            mMovieDao!!.insert(movie)
        }
    }
    fun update(movie: Movie?) {
        MovieRoomDatabase.databaseWriteExecutor.execute {
            mMovieDao!!.update(movie)
        }
    }
}