package com.example.moviefilmroomdeferredtask.data.api

import com.example.moviefilmroomdeferredtask.data.entity.MovieItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface Service {
    //@GET("popular?page=1")
    @GET("films")
    fun getListMovie(): Call<List<MovieItem>>

    //@GET("users/{user}/repos")
    //fun getUserRepos(@Path("user") user: String): Call<List<MovieItem>>
}