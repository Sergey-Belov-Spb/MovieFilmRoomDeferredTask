package com.example.moviefilmroomdeferredtask.presentation.view



import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.example.moviefilmroomdeferredtask.R
import com.example.moviefilmroomdeferredtask.Receiver
import com.example.moviefilmroomdeferredtask.data.db.Movie
import com.example.moviefilmroomdeferredtask.presentation.viewmodel.MovieListViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity(), MovieListFragment.MovieListListener,
    MovieListFavoriteFragment.MovieListListener {
    companion object{
        const val TAG = "MainActiviry"
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

    }
    //val movieViewModel : MovieListViewModel? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        //val Test = intent.getStringExtra("showSeeLate")
        val idMoveSeeLate = getIntent().getStringExtra("nameMovie")


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initButtonListener()

        if (idMoveSeeLate != null)
        {
            Log.d(TAG,"showFragmentDetailed ")
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer,MovieDetailedFragment.newInstance(idMoveSeeLate),MovieDetailedFragment.TAG)
                .addToBackStack("Detaled")
                .commit()
        }
        else {
            openAllMoviesList()
        }

        val movieViewModel =  MovieListViewModel(this.application)
    }

    fun CheckNewRequest() : Boolean{

        return false
    }

    fun readLastTime(): Long {
        val sPref = getPreferences(Context.MODE_PRIVATE)
        val timeMsec = sPref.getLong("LastTime",0)
        return timeMsec
    }
    fun storeLastTime(timeMsec :Long   ) {
        val sPref = getPreferences(Context.MODE_PRIVATE)
        val edit = sPref.edit()
        edit.putLong("LastTime",timeMsec)
        edit.commit()
    }

    fun createIntent (action: String,extra: String): Intent =
        Intent(action, null,this, Receiver::class.java).putExtra("extra", extra)

    fun CreateNotification ()
    {
        Log.d(TAG,"Set Alarm ")
        val intentN = Intent(this@MainActivity, MainActivity::class.java)
        intentN.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        //intent.putExtra("showSeeLate", "ID film 333")

        intentN.putExtra("nameMovie", "Hello ")
        intentN.putExtra("picMovie", "Pic 123");


        val pendingIntent = PendingIntent.getActivity(this@MainActivity, 0, intentN, 0)
        val CHANNEL_ID = "Me channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Channel name"
            val description = "Channel descriptio"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name , importance)
            channel.description = description
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager!!.createNotificationChannel(channel)
        }

        val build = NotificationCompat.Builder(this@MainActivity, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_watch_later_green_24dp)
            .setContentTitle("My notification")
            .setContentText("Hello World!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_watch_later_green_24dp, "Click me", pendingIntent)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(this@MainActivity)
        notificationManager.notify(2,build.build())

    }


    fun initButtonListener() {
        findViewById<Button>(R.id.debugBtn).setOnClickListener{

            CreateNotification ()


        }
        /*!!!findViewById<Button>(R.id.debugBtn2).setOnClickListener {

            Log.d(TAG,"Button2 ")
            val intent = Intent(this@MainActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent = PendingIntent.getActivity(this@MainActivity, 0, intent, 0)
            val CHANNEL_ID = "Me channel"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val name = "Channel name"
                val description = "Channel descriptio"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(CHANNEL_ID, name , importance)
                channel.description = description
                val notificationManager = getSystemService(NotificationManager::class.java)
                notificationManager!!.createNotificationChannel(channel)
            }

            val build = NotificationCompat.Builder(this@MainActivity, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_watch_later_green_24dp)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_watch_later_green_24dp, "Click me", pendingIntent)
                .setAutoCancel(true)

            val notificationManager = NotificationManagerCompat.from(this@MainActivity)
            notificationManager.notify(2,build.build())

            /*val builder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_watch_later_green_24dp)
                .setContentTitle("Напоминание")
                .setContentText("Пора смотреть фильм")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(contentIntent)*/
        }*/

        findViewById<BottomNavigationView>(R.id.navigationBottom).setOnNavigationItemSelectedListener {menuItem ->
            when(menuItem.itemId){
                R.id.action_allMovies ->{
                    Log.d(TAG,"action_allMovies")
                    openAllMoviesList()
                }
                R.id.action_favoriteMovies ->{
                    Log.d(TAG,"action_favoriteMovies")
                    openFavoriteMoviesList()
                }
            }
            false
        }
    }

    private fun openAllMoviesList(){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer,MovieListFragment(), MovieListFragment.TAG  )
            //.addToBackStack("Main")
            .commit()
    }

    private fun openFavoriteMoviesList(){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer,MovieListFavoriteFragment(), MovieListFavoriteFragment.TAG  )
            //.addToBackStack("Main")
            .commit()
    }

    private fun openDetailedFragment(movieItem: Movie) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer,MovieDetailedFragment.newInstance(movieItem.id.toString()),MovieDetailedFragment.TAG)
            .addToBackStack("Detaled")
            .commit()
    }

    private fun openSetTimeSeeLateFragment(movieItem: Movie) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer,MovieSetTimeSeeLate.newInstance(movieItem.id) ,MovieSetTimeSeeLate.TAG)
            .addToBackStack("SetTimeSeeLate")
            .commit()
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is MovieListFragment)
        {
            fragment.listener = this
            Log.d(TAG,"onAttachFragment -> MovieListFragment")
        }
        if (fragment is MovieListFavoriteFragment)
        {
            fragment.listener = this
            Log.d(TAG,"onAttachFragment -> MovieListFavoriteFragment")
        }
    }

    override fun onMovieSelected(moviesItemDetailed: Movie,addSeeLate: Boolean) {
        if (addSeeLate == false) {
            Log.d(TAG, "Show Detailed")
            openDetailedFragment(moviesItemDetailed)
        } else {
            Log.d(TAG, "Show Set Time See late")
            openSetTimeSeeLateFragment(moviesItemDetailed)
        }

    }
}

