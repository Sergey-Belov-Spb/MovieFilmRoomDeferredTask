package com.example.moviefilmroomdeferredtask.data.entity

import com.google.gson.annotations.SerializedName

class MovieItem {

    @SerializedName("poster_path")
    lateinit var gitUrl: String
    @SerializedName("id")
    var id: Int? = null
    @SerializedName("title")
    lateinit var title: String
    @SerializedName("favorite")
    var favorite: Boolean? = null

    /*@SerializedName("git_url")
    lateinit var gitUrl: String*/


    constructor(gitUrl: String) {
        this.gitUrl = gitUrl
    }
}