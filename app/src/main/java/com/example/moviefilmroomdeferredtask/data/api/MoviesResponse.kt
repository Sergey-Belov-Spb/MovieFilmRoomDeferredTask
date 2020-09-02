package com.example.moviefilmroomdeferredtask.data.api

import com.example.moviefilmroomdeferredtask.data.entity.MovieItem
import com.google.gson.annotations.SerializedName

data class MoviesResponse (

    @SerializedName("page")
    var page: Int = 0,

    @SerializedName("total_results")
    var totalResults: Int = 0,

    @SerializedName("total_pages")
    var totalPages: Int = 0,

    @SerializedName("results")
    var movies: List<MovieItem>? = null
)
