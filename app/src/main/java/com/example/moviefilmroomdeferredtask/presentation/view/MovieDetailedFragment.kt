package com.example.moviefilmroomdeferredtask.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.moviefilmroomdeferredtask.R

import com.google.android.material.appbar.CollapsingToolbarLayout

class MovieDetailedFragment : Fragment() {
    companion object{
        const val TAG = "MovieDetailedFragment"

        const val EXTRA_NAME = "EXTRA_NAME"
        const val EXTRA_INDEXPIC = "EXTRA_INDEXPIC"
        const val EXTRA_CONTENS = "EXTRA_CONTENS"

        fun newInstance(name: String,image: String ): MovieDetailedFragment{
            val fragment = MovieDetailedFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_NAME, name)
            bundle.putString(EXTRA_INDEXPIC, image)
            fragment.arguments = bundle
            return fragment
        }
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

        val imageView = view.findViewById<ImageView>(R.id.image)

        val image: String? = arguments?.getString(EXTRA_INDEXPIC," ")
        Glide.with(imageView.context)
            .load(image)
            .placeholder(R.drawable.ic_image_blue)
            .error(R.drawable.ic_error_blue)
            .override(imageView.resources.getDimensionPixelSize(R.dimen.image_size))
            .centerCrop()
            .into(imageView)

        view.findViewById<TextView>(R.id.description).text =arguments?.getString(EXTRA_CONTENS,"Нет содержания")
        view.findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar).title=arguments?.getString(EXTRA_NAME,"Нет имени")
    }
}