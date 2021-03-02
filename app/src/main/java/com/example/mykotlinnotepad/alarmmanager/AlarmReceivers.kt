package com.example.mykotlinnotepad.alarmmanager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.format.DateFormat
import io.karn.notify.Notify
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

class AlarmReceivers : BroadcastReceiver(){
    override fun onReceive(context: Context, intent: Intent) {
        val timeInMillis = intent.getLongExtra(Constants.EXTRA_EXACT_ALARM_TIME, 0L)
        when (intent.action){
            Constants.ACTION_SET_REPETITIVE_EXACT -> {
                createNotification(context, "Diary Time", convertTime(timeInMillis))
            }
        }
    }
    private fun createNotification(context: Context, title: String, message: String){
        Notify.with(context)
            .content {
                this.title = title
                text = "It's your diary time!, write down your story right now :D : -$message"
            }.show()
    }

    private fun setRepetitiveAlarm(alarmService: AlarmService){
        val cal = Calendar.getInstance().apply {
            this.timeInMillis = timeInMillis + TimeUnit.DAYS.toMillis(1)
            Timber.d("Time to write again" + "${convertTime(this.timeInMillis)}")
        }
        alarmService.setRepetitiveAlarm(cal.timeInMillis)
    }

    private fun convertTime(timeInMillis: Long): String =
        DateFormat.format("dd/MM/yyyy hh:mm:ss", timeInMillis).toString()
}