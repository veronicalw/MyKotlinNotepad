package com.example.mykotlinnotepad.alarmmanager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build

class AlarmService(private val context: Context) {
    private val alarmManager: AlarmManager? = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
    fun setRepetitiveAlarm(timeInMillis: Long){
        setAlarm(
            timeInMillis,
            getPendingIntent(
                getIntent().apply{
                    action = Constants.ACTION_SET_REPETITIVE_EXACT
                    putExtra(Constants.EXTRA_EXACT_ALARM_TIME, timeInMillis)
                }
            )
        )
    }
    private fun getPendingIntent(intent: Intent) =
        PendingIntent.getBroadcast(
            context,
            getRandomRequestCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    fun cancelAlarm(pendingIntent: PendingIntent){
        alarmManager?.cancel(pendingIntent)
    }
    private fun setAlarm(timeInMillis: Long, pendingIntent: PendingIntent){
        alarmManager?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    timeInMillis,
                    pendingIntent
                )
            }
        }
    }

    private fun getIntent() = Intent(context, AlarmReceivers::class.java)
    private fun getRandomRequestCode() = RandomIntUtil.getRandomInt()

}