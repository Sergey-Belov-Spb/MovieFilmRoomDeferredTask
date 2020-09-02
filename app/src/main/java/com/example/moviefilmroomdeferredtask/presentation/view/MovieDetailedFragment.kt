package com.example.moviefilmroomdeferredtask.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.moviefilmroomdeferredtask.R
import com.example.moviefilmroomdeferredtask.data.db.Movie
import com.example.moviefilmroomdeferredtask.presentation.viewmodel.MovieListViewModel
import androidx.lifecycle.Observer

import com.google.android.material.appbar.CollapsingToolbarLayout
import java.util.ArrayList

class MovieDetailedFragment : Fragment() {
    companion object{
        const val TAG = "MovieDetailedFragment"

        const val EXTRA_ID_MOVIE = "EXTRA_ID_MOVIE"

        fun newInstance(idMovie: String): MovieDetailedFragment{
            val fragment = MovieDetailedFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_ID_MOVIE, idMovie)
            fragment.arguments = bundle
            return fragment
        }
        private var viewModel : MovieListViewModel? = null

        private var showIdMovie : Long = 0
        var textView: TextView? = null
        var collapsingToolbarLayout:    CollapsingToolbarLayout?=null
        var imageView: ImageView? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_detailed, container,false )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(activity!!).get(MovieListViewModel::class.java!!)
        viewModel!!.moviesFavorite.observe(this.viewLifecycleOwner, Observer<List<Movie>> {
                repos -> setItems(repos)
        })

        showIdMovie = arguments?.getString(EXTRA_ID_MOVIE,"0")!!.toLong()
        imageView = view.findViewById<ImageView>(R.id.image)

        textView = view.findViewById<TextView>(R.id.description)
        collapsingToolbarLayout = view.findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar)
       /*!!!! val image: String? = arguments?.getString(EXTRA_INDEXPIC," ")
        Glide.with(imageView.context)
            .load(image)
            .placeholder(R.drawable.ic_image_blue)
            .error(R.drawable.ic_error_blue)
            .override(imageView.resources.getDimensionPixelSize(R.dimen.image_size))
            .centerCrop()
            .into(imageView)

        view.findViewById<TextView>(R.id.description).text =arguments?.getString(EXTRA_CONTENS,"Нет содержания")
        view.findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar).title=arguments?.getString(EXTRA_NAME,"Нет имени")*/
    }

    fun setItems(repos: List<Movie>) {

        for (i in 0..repos.size-1) {
            if (repos[i].id == showIdMovie) {

                textView!!.text =" Нет описания"
                collapsingToolbarLayout!!.title = repos[i].title

                val image: String? = repos[i].picUrl
                Glide.with(imageView!!.context)
                    .load(image)
                    .placeholder(R.drawable.ic_image_blue)
                    .error(R.drawable.ic_error_blue)
                    .override(imageView!!.resources.getDimensionPixelSize(R.dimen.image_size))
                    .centerCrop()
                    .into(imageView!!)
            }
        }
    }

}