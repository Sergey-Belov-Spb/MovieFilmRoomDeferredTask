package com.example.moviefilmroomdeferredtask.presentation.viewmodel

//import com.example.moviesfilmroom.data.MovieRepositoryBase
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moviefilmroomdeferredtask.App
import com.example.moviefilmroomdeferredtask.data.entity.Movie
import com.example.moviefilmroomdeferredtask.data.entity.MovieItem
import com.example.moviefilmroomdeferredtask.domain.MovieInteractor
import com.example.moviefilmroomdeferredtask.presentation.view.MainActivity
import java.util.*


public class MovieListViewModel(application: Application): AndroidViewModel(application) {

    val app = application
    private val allMoviesLiveData = MutableLiveData<List<Movie>>()
    //private val favoriteLiveData = MutableLiveData<List<Movie>>()
    private val errorLiveData = MutableLiveData<String>()
    private var activeInternetRequestData = false

    private val itemsFavorite = ArrayList<MovieItem>()

    private val githubInteractor = App.instance!!.movieInteractor

    private val mMovieBase = App.mRepositoryBase

    init {

    }

    val moviesAll: LiveData<List<Movie>>?
        get() = allMoviesLiveData //mMovieBase?.getmAllMovie() //allMoviesLiveData

    val moviesFavorite: LiveData<List<Movie>>
        get() = allMoviesLiveData //favoriteLiveData

    val error: LiveData<String>
        get() = errorLiveData

    fun readLastTime(): Long {
        val sPref = app.getSharedPreferences("preference_key", Context.MODE_PRIVATE)
        val timeMsec = sPref.getLong("LastTime",0)
        return timeMsec
    }

    fun storeLastTime(timeMsec :Long   ) {
        val sPref = app.getSharedPreferences("preference_key", Context.MODE_PRIVATE)
        val edit = sPref.edit()
        edit.putLong("LastTime",timeMsec)
        edit.commit()
    }
    fun CheckTimeReqNewData() :Boolean {

        val data = Date();
        val curTime = data.time
        val deltaTimeSec =(curTime - readLastTime())/1000
        Log.d("MovieListViewModel","GetTime = $curTime $deltaTimeSec ")
        storeLastTime(curTime)
        if (deltaTimeSec > (6)) return true
        return false
    }


    fun onGetDataClick() : Boolean {
        if (CheckTimeReqNewData() == true) {
            if (activeInternetRequestData == false) {
                activeInternetRequestData = true
                githubInteractor.getNewMovies(object : MovieInteractor.GetMoviesCallback {
                    override fun onSuccess(newMovies: List<MovieItem>) {
                        activeInternetRequestData = false
                        mMovieBase?.AddToBase(newMovies)
                        val newBase = mMovieBase?.getmAllMovie2();
                        allMoviesLiveData.postValue(newBase)

                        //allMoviesLiveData.postValue(newMovies)

                    }

                    override fun onError(error: String) {
                        activeInternetRequestData = false
                        errorLiveData.postValue(error)
                    }
                })
            } else {
                Log.d("MovieListViewModel", "Block New data Request!!")
            }
            return true
        }
        return false
    }

    fun onMovieSelect(item: Movie, addToFavotite: Boolean) {
        if (addToFavotite){
            if (item.favorite == true) {
                item.favorite = false
                /*listOf(favoriteLiveData)
                favoriteLiveData.value*/

            }
            else {item.favorite = true }
            val inFavorite = item.favorite
            Log.d("MovieListViewModel","AddToFavorite item.favorite= $inFavorite")
            mMovieBase?.update(item)
            val newBase = mMovieBase?.getmAllMovie2();
            allMoviesLiveData.postValue(newBase)


/*!!!!! УДАЛЕНИЕ ДОДЕЛАТЬ
        if ((item.favorite == true)&&(itemsFavorite.indexOf(item)==-1)) { itemsFavorite.add(item) }
            else {
                itemsFavorite.remove(item)
            }
          favoriteLiveData.postValue(allMoviesLiveData.value)*/
        }
    }

    fun printBase(){
        Log.d("MovieListViewModel","printBase")

    }

}