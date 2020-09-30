package com.example.moviefilmroomdeferredtask.domain

import com.example.moviefilmroomdeferredtask.data.api.Service
import com.example.moviefilmroomdeferredtask.data.entity.MovieItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MovieInteractor(private val movieService: Service) {

    fun getNewMovies(callback: GetMoviesCallback) {
        //githubService.getUserRepos(username).enqueue(object : Callback<List<MovieItem>> {
        movieService.getListMovie().enqueue(object : Callback<List<MovieItem>> {
            override fun onResponse(call: Call<List<MovieItem>>, response: Response<List<MovieItem>>) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.body()!!)
                } else {
                    callback.onError(response.code().toString() + "")
                }
            }
            override fun onFailure(call: Call<List<MovieItem>>, t: Throwable) {
                callback.onError("Network error probably...")
            }
        })
    }

    interface GetMoviesCallback {
        fun onSuccess(newMovies: List<MovieItem>)
        fun onError(error: String)
    }
}