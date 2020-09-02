package com.example.moviefilmroomdeferredtask.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import com.example.moviefilmroomdeferredtask.data.db.Movie
import com.google.gson.annotations.SerializedName
import java.util.*


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