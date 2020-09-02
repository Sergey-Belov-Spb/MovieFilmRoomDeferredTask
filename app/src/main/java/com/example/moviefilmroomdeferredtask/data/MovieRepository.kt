package com.example.moviefilmroomdeferredtask.data

import com.example.moviefilmroomdeferredtask.data.entity.MovieItem
import java.util.ArrayList


class MovieRepository {
    private val cachedRepos = ArrayList<MovieItem>()
    private val fakeRepos = ArrayList<MovieItem>()

    val cachedOrFakeRepos: List<MovieItem>
        get() = if (cachedRepos.size > 0)
            cachedRepos
        else
            fakeRepos

    init {
        fakeRepos.add(MovieItem("mock repo 1"))
        fakeRepos.add(MovieItem("mock repo 2"))
        fakeRepos.add(MovieItem("mock repo 3"))
        fakeRepos.add(MovieItem("mock repo 4"))
    }

    fun addToCache(repos: List<MovieItem>) {
        this.cachedRepos.addAll(repos)
    }
}