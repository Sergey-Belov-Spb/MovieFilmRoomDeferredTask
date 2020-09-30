package com.example.moviefilmroomdeferredtask

import android.app.Application
import com.example.moviefilmroomdeferredtask.data.api.NetworkConstants.BASE_URL
import com.example.moviefilmroomdeferredtask.data.api.Service
import com.example.moviefilmroomdeferredtask.data.db.MovieRepositoryBase
import com.example.moviefilmroomdeferredtask.data.entity.MovieItem
import com.example.moviefilmroomdeferredtask.domain.MovieInteractor
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import rx.Observable
import java.util.*


class App : Application(){

    lateinit var movieService: Service
    // lateinit var githubReposUpdater: GithubReposUpdater
    lateinit var movieInteractor: MovieInteractor

    lateinit var observableRetrofit: Observable<ArrayList<MovieItem>>
//??    lateinit var observableRetrofit : Observable<ArrayList<MovieItem>>


//    var movieRepository = MovieRepository()

    override fun onCreate() {
        super.onCreate()

        instance = this

        initBase()
        initRetrofit()
        initInteractor()
    }

    private fun initInteractor() {
        movieInteractor = MovieInteractor(movieService)
    }
    private fun initRetrofit() {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            //.addInterceptor(AuthInterceptor())
            .build()

        movieService = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            //.addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
            .create(Service::class.java!!)

        ///githubReposUpdater = GithubReposUpdater(githubService)
    }

    private fun initRetrofitRxJava() {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            //.addInterceptor(AuthInterceptor())
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            //.addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()

        movieService =retrofit.create(Service::class.java)
        observableRetrofit = movieService.popularMovies()
        //Надо вернуть observableRetrofit но как его получить? val observableRetrofit: Observable<ArrayList<MovieItem>>

    }


    private fun initBase(){
        //private val mRepositoryBase = MovieRepositoryBase(this)
        mRepositoryBase = MovieRepositoryBase(this)
        //mRepositoryBase?.MovieRepositoryBase(this)

    }


    companion object{

        //const val BASE_URL = "https://api.github.com/"

        var mRepositoryBase : MovieRepositoryBase? = null
        var instance : App? = null
            private set
    }
}