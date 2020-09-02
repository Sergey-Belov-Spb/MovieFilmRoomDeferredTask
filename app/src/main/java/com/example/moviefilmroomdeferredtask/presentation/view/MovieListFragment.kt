package com.example.moviefilmroomdeferredtask.presentation.view

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.moviefilmroomdeferredtask.R
import com.example.moviefilmroomdeferredtask.presentation.viewmodel.MovieListViewModel
import com.bumptech.glide.Glide
import com.example.moviefilmroomdeferredtask.Receiver
import com.example.moviefilmroomdeferredtask.data.db.Movie
import com.google.android.material.snackbar.Snackbar
import java.util.*


class MovieListFragment : Fragment() {
    companion object{
        const val TAG = "MovieListFragment"
    }

    var listener :MovieListListener? = null
    private var viewModel: MovieListViewModel? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: MovieAdapter? = null
    private var progressBar: ProgressBar? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (activity is MovieListListener){
            listener = activity as MovieListListener
        } else {
            throw Exception("Activity must implement MovieListListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecycler()

        progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        viewModel = ViewModelProviders.of(activity!!).get(MovieListViewModel::class.java!!)
        viewModel!!.moviesAll?.observe(this.viewLifecycleOwner, Observer<List<Movie>> { repos ->

            if (repos.size==0) {viewModel!!.onGetDataClick()}
            else
            {adapter!!.setItems(repos,view)}
        })
        viewModel!!.error.observe(this.viewLifecycleOwner, Observer<String> { error ->

            progressBar?.visibility = View.INVISIBLE
            val mySnackbar =  this.view?.let { Snackbar.make(it, "Ошибка соединения с интернетом.", Snackbar.LENGTH_LONG) }
            mySnackbar?.setAction(
                "Повторить запрос?",
                {
                    if (viewModel!!.onGetDataClick() == true){
                        progressBar?.visibility = View.VISIBLE
                        Log.d(TAG,"NEW LOAD!!!! from mySnackbar ")
                    } else {Log.d(TAG,"Block NEW LOAD!!!! from mySnackbar ")}
                })
            mySnackbar?.show()
        })

        /* <!--
        view.findViewById<View>(R.id.getDataBtn).setOnClickListener { v ->
            viewModel!!.onGetDataClick()
            progressBar?.visibility = View.VISIBLE
        }*/

        if (viewModel!!.onGetDataClick() == true)  { progressBar?.visibility = View.VISIBLE}
    }

    private fun initRecycler() {
        adapter = MovieAdapter(LayoutInflater.from(context), object : MovieAdapter.OnMovieSelectedListener {
            override fun onRepoSelect(item: Movie, addToFavorite: Boolean, addSeeLate: Boolean) {
                if ((addToFavorite==false)&&(addSeeLate==false)) { listener?.onMovieSelected(item,false)}
                else
                    if ((addToFavorite==true)&&(addSeeLate==false)){
                    viewModel!!.onMovieSelect(item,addToFavorite)
                    adapter?.notifyDataSetChanged()
                } else
                        if ((addToFavorite == false)&&(addSeeLate==true)) {
                            listener?.onMovieSelected(item,true)
                }
            }
        })
        recyclerView = view!!.findViewById(R.id.recyclerView)
        recyclerView!!.adapter = adapter

        recyclerView?.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                Log.d(TAG, "dx:$dx dy:$dy ")
                val layoutManager= recyclerView?.layoutManager as LinearLayoutManager
                val x = layoutManager.findLastVisibleItemPosition()
                val sizeItems = adapter?.itemCount
                Log.d(TAG,"layoutManager.findLastVisibleItemPosition() = $x sizeItems = $sizeItems")
                if ((layoutManager.findLastVisibleItemPosition()+1)== adapter?.itemCount){

                    //listener?.onMoviesSelected(MoviesItem(1,"1","1",false),-1,0)
                    Log.d(TAG,"NEW LOAD!!!! ")
                    if (viewModel!!.onGetDataClick() == true)  { progressBar?.visibility = View.VISIBLE}
                }
            }
        })
    }
    /* class RecyclerView.ViewHolder */
    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameMovie=itemView.findViewById<TextView>(R.id.nameMovieInAll)
        val picFavorite=itemView.findViewById<ImageView>(R.id.imageFavoriteAll)
        val imageFilm = itemView.findViewById<ImageView>(R.id.imageMovieInAll)

        fun bind(item: Movie) {
            nameMovie.text = item.title
            if (item.favorite== true) {picFavorite.setImageResource(R.drawable.ic_favorite_black_24dp)}
            else {picFavorite.setImageResource(R.drawable.ic_favorite_border_black_24dp)}

            Glide.with(imageFilm.context)
                .load(item.picUrl)
                .placeholder(R.drawable.ic_image_blue)
                .error(R.drawable.ic_error_blue)
                .override(imageFilm.resources.getDimensionPixelSize(R.dimen.image_size))
                .centerCrop()
                .into(imageFilm)
            //(itemView as TextView).text = url
        }
    }
    /*END class RecyclerView.ViewHolder */
    /* class RecyclerView.Adapter */
    class MovieAdapter(private val inflater: LayoutInflater, private val listener: OnMovieSelectedListener) : RecyclerView.Adapter<MovieViewHolder>() {
        private val items = ArrayList<Movie>()

        fun setItems(repos: List<Movie>,view: View) {
            //items.clear()
            view.findViewById<ProgressBar>(R.id.progressBar).visibility = View.INVISIBLE
            items.addAll(repos)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
            return MovieViewHolder(inflater.inflate(R.layout.item_movie, parent, false))
        }

        override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
            holder.bind(items[position])
            holder.itemView.findViewById<ImageView>(R.id.imageFavoriteAll).setOnClickListener{
                    v -> listener.onRepoSelect(items[position],true,false)

            }
            holder.itemView.findViewById<ImageView>(R.id.imageMovieInAll).setOnClickListener{
                    v -> listener.onRepoSelect(items[position],false,false)
            }
            holder.itemView.findViewById<ImageView>(R.id.imageSeeLate).setOnClickListener {
                    v -> listener.onRepoSelect(items[position],false,true)
                //Set Data and time see late
                /*val c = Calendar.getInstance()
                val yr = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)
                val display = DatePickerDialog(it.context, DatePickerDialog.OnDateSetListener {
                        view, year, monthOfYear, dayOfMonth ->
                    //dateColumn.text = ("$dayOfMonth $monthInput, $year")
                    Log.d(MovieSetTimeSeeLate.TAG, "Date :"+ year+" "+ (monthOfYear+1) + " " + dayOfMonth)
                    val newYear = year
                    val newMonth =  monthOfYear+1
                    val newDay = day

                    Log.d(MovieSetTimeSeeLate.TAG, "Set SelectTime ")
                    val hh=c.get(Calendar.HOUR_OF_DAY)
                    val mm=c.get(Calendar.MINUTE)
                    val timePickerDialog: TimePickerDialog = TimePickerDialog(it.context,
                        TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                            //view.findViewById<Button>(R.id.textSetTime).setText( ""+hourOfDay + ":" + minute);
                            Log.d(MovieSetTimeSeeLate.TAG, "Time >"+hourOfDay + ":" + minute)

                            val newMin =minute
                            val newHour = hourOfDay

                            Log.d(MovieSetTimeSeeLate.TAG, "Set Alarm Time ")
                            val curYard = c.get(Calendar.YEAR)
                            val curMonth = c.get(Calendar.MONTH)+1
                            val curDay = c.get(Calendar.DAY_OF_MONTH)
                            val curHour = c.get(Calendar.HOUR_OF_DAY)
                            val curMin =c.get(Calendar.MINUTE)
                            val curData = Date(c.get(Calendar.YEAR),c.get(Calendar.MONTH)+1,c.get(Calendar.DAY_OF_MONTH),c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE)).time
                            val waitData = Date(
                                newYear,
                                newMonth,
                                newDay,
                                newHour,
                                newMin
                            ).time

                            val delta = (waitData-curData)
                            Log.d(MovieSetTimeSeeLate.TAG, "Time cur> "+curData + " Time wait> " + waitData + "delta = " + delta)

                            Log.d(MovieSetTimeSeeLate.TAG, "SetTime min -> "+ delta/1000/60)
                            //getActivity()?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
                            //getActivity()?.supportFragmentManager?.getBackStackEntryAt(2)
                            if (delta>0) {
                                Toast.makeText(it.context,"Фильм добавлен в спиок ожидания просмотра.",Toast.LENGTH_SHORT).show()
                                setAlarm(delta,it.context)
                            }else {
                                Toast.makeText(it.context,"Неправильно задано время просмотра.",Toast.LENGTH_SHORT).show()
                            }

                        },hh,mm,true)
                    timePickerDialog.show()

                }, yr, month, day)
                display.datePicker.minDate = System.currentTimeMillis()
                display.show()*/

            }

        }

        private fun createIntent(action: Long,context : Context): Intent =
            Intent(action.toString(), null, context, Receiver::class.java)


        private fun setAlarm(timeDelayIn_mSec:Long,context : Context) {
            Log.d(MovieSetTimeSeeLate.TAG, "setAlarm")

            val intent = createIntent(MovieSetTimeSeeLate.idMovie as Long,context)
            val pIntentOnce = PendingIntent.getBroadcast(context, 0, intent, 0)
            val am = ContextCompat.getSystemService(context, AlarmManager::class.java)
            am?.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + timeDelayIn_mSec, pIntentOnce)

            Log.d(MovieSetTimeSeeLate.TAG, "SetTime мин "+ 1_000)
            Log.d(MovieSetTimeSeeLate.TAG, "SetTime My "+ timeDelayIn_mSec)
        }

        override fun getItemCount(): Int {
            return items.size
        }

        interface OnMovieSelectedListener {
            fun onRepoSelect(item: Movie, addToFavorite: Boolean, addSeeLate: Boolean)
        }
    }
    /*END  class RecyclerView.Adapter */

    interface MovieListListener {
        fun onMovieSelected(moviesItemDetailed: Movie, addSeeLate: Boolean)
    }
}