package com.example.moviefilmroomdeferredtask.presentation.view

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.example.moviefilmroomdeferredtask.R
import com.example.moviefilmroomdeferredtask.Receiver
import java.util.*


class MovieSetTimeSeeLate : Fragment() {
    companion object {
        const val TAG = "MovieSetTimeSeeLate"
        val dateAndTime=Calendar.getInstance()
        var currentDateTime: TextView? = null
        var textDataSet : TextView? = null
        var alarmMin : Int = 0
        var alarmSec : Int = 0
        var alarmHour : Int = 0
        var alarmYeard : Int = 0
        var alarmMonth : Int = 0
        var alarmDay: Int = 0
        var idMovie : Long? = null

        const val EXTRA_ID_MOVIE = "EXTRA_ID_MOVIE"

        fun newInstance(id: Long): MovieSetTimeSeeLate{
            val fragment = MovieSetTimeSeeLate()
            val bundle = Bundle()
            bundle.putLong(EXTRA_ID_MOVIE, id)
            fragment.arguments = bundle
            return fragment
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_settime_seelate,container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        idMovie = arguments?.getLong(EXTRA_ID_MOVIE,0)

        currentDateTime=view.findViewById<TextView>(R.id.textSetDate)

        currentDateTime?.text = "Дата "+ alarmDay + "." + alarmMonth +"." + alarmYeard+"  " +"Время " + alarmHour + ":" + alarmMin
        view.findViewById<Button>(R.id.btnSelectDate).setOnClickListener {
            Log.d(TAG,"Set SelectDate ")

            val c = Calendar.getInstance()
            val yr = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val display = DatePickerDialog(view.context, DatePickerDialog.OnDateSetListener {
                    view, year, monthOfYear, dayOfMonth ->
                //dateColumn.text = ("$dayOfMonth $monthInput, $year")
                Log.d(TAG, "Date :"+ year+" "+ (monthOfYear+1) + " " + dayOfMonth)
                alarmYeard = year
                alarmMonth = monthOfYear+1
                alarmDay = day
                currentDateTime?.text = "Дата "+ alarmDay + "." + alarmMonth +"." + alarmYeard+"  " +"Время " + alarmHour + ":" + alarmMin
            }, yr, month, day)
            display.datePicker.minDate = System.currentTimeMillis()
            display.show()

        }
        view.findViewById<Button>(R.id.btnSelectTime).setOnClickListener {
            Log.d(TAG, "Set SelectTime ")

            val c:Calendar= Calendar.getInstance()
            val hh=c.get(Calendar.HOUR_OF_DAY)
            val mm=c.get(Calendar.MINUTE)
            val timePickerDialog:TimePickerDialog=TimePickerDialog(view.context,TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                //view.findViewById<Button>(R.id.textSetTime).setText( ""+hourOfDay + ":" + minute);
                Log.d(TAG, "Time >"+hourOfDay + ":" + minute)


                alarmMin=minute
                alarmHour=hourOfDay
                currentDateTime?.text = "Дата "+ alarmDay + "." + alarmMonth +"." + alarmYeard+"  " +"Время " + alarmHour + ":" + alarmMin
            },hh,mm,true)
            timePickerDialog.show()

        }
        view.findViewById<Button>(R.id.btnSetTimeSeeLate).setOnClickListener {
            Log.d(TAG, "Set Alarm Time ")
            val c = Calendar.getInstance()
            val curYard = c.get(Calendar.YEAR)
            val curMonth = c.get(Calendar.MONTH)+1
            val curDay = c.get(Calendar.DAY_OF_MONTH)
            val curHour = c.get(Calendar.HOUR_OF_DAY)
            val curMin =c.get(Calendar.MINUTE)

            val curData = Date(c.get(Calendar.YEAR),c.get(Calendar.MONTH)+1,c.get(Calendar.DAY_OF_MONTH),c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE)).time
            val waitData = Date(alarmYeard, alarmMonth, alarmDay, alarmHour, alarmMin).time

            val delta = (waitData-curData)
            Log.d(TAG, "Time cur> "+curData + " Time wait> " + waitData + "delta = " + delta)

            Log.d(TAG, "SetTime min -> "+ delta/1000/60)
            //getActivity()?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            //getActivity()?.supportFragmentManager?.getBackStackEntryAt(2)
            if (delta>0) {
                Toast.makeText(view.context,"Фильм добавлен в спиок ожидания просмотра.",Toast.LENGTH_SHORT).show()
                setAlarm(delta)
            }else {
                Toast.makeText(view.context,"Неправильно задано время просмотра.",Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun createIntent(idMovie: Long): Intent =
        Intent(idMovie.toString(), null, context, Receiver::class.java)


    private fun setAlarm(timeDelayIn_mSec:Long) {
        Log.d(TAG, "setAlarm")

        val intent = createIntent(idMovie as Long)
        val pIntentOnce = PendingIntent.getBroadcast(requireContext(), 0, intent, 0)
        val am = getSystemService(requireContext(), AlarmManager::class.java)
        am?.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + timeDelayIn_mSec, pIntentOnce)

        Log.d(TAG, "SetTime мин "+ 1_000)
        Log.d(TAG, "SetTime My "+ timeDelayIn_mSec)
    }

}