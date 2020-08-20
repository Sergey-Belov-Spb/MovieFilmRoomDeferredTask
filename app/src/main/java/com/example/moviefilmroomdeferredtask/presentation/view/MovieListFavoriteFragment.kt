package com.example.moviefilmroomdeferredtask.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.moviefilmroomdeferredtask.R
import com.example.moviefilmroomdeferredtask.data.entity.Movie
import com.example.moviefilmroomdeferredtask.data.entity.MovieItem
import com.example.moviefilmroomdeferredtask.presentation.viewmodel.MovieListViewModel
import java.util.ArrayList

class MovieListFavoriteFragment: Fragment()  {
    companion object{
        const val TAG = "MovieListFavoriteFragment"
    }
    private var viewModel : MovieListViewModel? = null
    private var adapter : ReposAdapter? = null
    private var recyclerView: RecyclerView? = null

    var listener :MovieListListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //super.onViewCreated(view, savedInstanceState)
        initRecycler()

        viewModel = ViewModelProviders.of(activity!!).get(MovieListViewModel::class.java!!)
        viewModel!!.moviesFavorite.observe(this.viewLifecycleOwner, Observer<List<Movie>> {
                repos -> adapter!!.setItems(repos)
        })

    }

    private fun initRecycler() {
        adapter = MovieListFavoriteFragment.ReposAdapter(
            LayoutInflater.from(context),
            object : MovieListFavoriteFragment.ReposAdapter.OnRepoSelectedListener {
                override fun onRepoSelect(item: Movie, addToFavorite: Boolean) {
                    if (addToFavorite==false) { listener?.onMovieSelected(item,false)}
                    else {
                        viewModel!!.onMovieSelect(item,addToFavorite)
                        adapter?.notifyDataSetChanged()
                    }
                }
            })
        recyclerView = view!!.findViewById(R.id.recyclerView)
        recyclerView!!.adapter = adapter
    }


    class ReposAdapter(private val inflater: LayoutInflater, private val listener: OnRepoSelectedListener) : RecyclerView.Adapter<MovieListFragment.MovieViewHolder>() {
        private val items = ArrayList<Movie>()

        fun setItems(repos: List<Movie>) {
            items.clear()
            for (i in 0..repos.size-1) {
                if (repos[i].favorite == true) {items.add(repos[i])}
            }
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListFragment.MovieViewHolder {
            return MovieListFragment.MovieViewHolder(
                inflater.inflate(
                    R.layout.item_movie,
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: MovieListFragment.MovieViewHolder, position: Int) {
            holder.bind(items[position])
            holder.itemView.findViewById<ImageView>(R.id.imageFavoriteAll).setOnClickListener{ v ->
                listener.onRepoSelect(items[position],true)

            }
            holder.itemView.findViewById<ImageView>(R.id.imageMovieInAll).setOnClickListener{ v ->
                listener.onRepoSelect(items[position],false)
            }
        }

        override fun getItemCount(): Int {
            return items.size
        }

        interface OnRepoSelectedListener {
            fun onRepoSelect(item: Movie,addToFavorite: Boolean)
        }
    }
    interface MovieListListener {
        fun onMovieSelected(moviesItemDetailed: Movie, addSeeLate: Boolean)
    }
}