package com.example.moviefilmroomdeferredtask.data.api

import com.example.moviefilmroomdeferredtask.data.entity.MovieItem
import retrofit2.Call
import retrofit2.http.GET
import java.util.*
import rx.Observable

interface Service {
    //@GET("popular?page=1")
   @GET("films")
    //@GET("popular?page=2&api_key=9a1a4d8d07b89f0c57458dbaf6d58a99")
    fun getListMovie(): Call<List<MovieItem>>

    @GET("films")
    fun popularMovies(): Observable<ArrayList<MovieItem>>
    //@GET("users/{user}/repos")
    //fun getUserRepos(@Path("user") user: String): Call<List<MovieItem>>
}