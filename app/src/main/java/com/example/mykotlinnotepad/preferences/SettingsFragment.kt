package com.example.mykotlinnotepad.preferences

import android.app.*
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.mykotlinnotepad.R
import com.example.mykotlinnotepad.alarmmanager.AlarmService
import java.util.*

class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var preference: Preference
    lateinit var alarmService: AlarmService
//    private val mNotification = Calendar.getInstance().timeInMillis + 5000
//    private var mNotified = false
    private val PREFS_NAME = "kotlinAlarm"

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        preference = findPreference("alarm")!!
        preference.setOnPreferenceClickListener {
            if (preference.isEnabled){
                alarmService = AlarmService(context as Activity)
                val alertDialog = AlertDialog.Builder(context as Activity).setTitle("Schedule your alarm")
                        .setMessage("You're going to set alarm for daily writing notes")
                        .setPositiveButton("Set Alarm", object : DialogInterface.OnClickListener{
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                showTimePicker {
                                    alarmService.setRepetitiveAlarm(it)
                                }
//                        showTimePicker()
                            }

                        })
                        .setNegativeButton("Cancel", object : DialogInterface.OnClickListener{
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                p0?.dismiss()
                            }
                        })
                alertDialog.show()
            } else if (!!preference.isEnabled){
                val alertDialogSwitch = AlertDialog.Builder(context as Activity).setTitle("Turn off Alarm")
                        .setMessage("are you sure ?")
                        .setPositiveButton("Yes", object : DialogInterface.OnClickListener{
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                val intent = Intent(context as Activity, AlarmService::class.java)
                                var pendingIntent: PendingIntent = PendingIntent.getService(context as Activity,0,intent,0)

//                                showTimePicker {
//                                    alarmService.cancelAlarm(pendingIntent)
//                                }
                            }
                        })
                        .setNegativeButton("No", object : DialogInterface.OnClickListener{
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                p0?.dismiss()
                            }
                        })
                alertDialogSwitch.show()
            }

            true
        }
    }

    private fun showTimePicker(callback: (Long) -> Unit) {
        Calendar.getInstance().apply {
            this.set(Calendar.SECOND, 0)
            this.set(Calendar.MILLISECOND, 0)
            DatePickerDialog(
                context as Activity,
                0,
                { _, year, month, day ->
                    this.set(Calendar.YEAR, year)
                    this.set(Calendar.MONTH, month)
                    this.set(Calendar.DAY_OF_MONTH, day)
                    TimePickerDialog(
                        context as Activity,
                        0,
                        { _, hour, minute ->
                            this.set(Calendar.HOUR_OF_DAY, hour)
                            this.set(Calendar.MINUTE, minute)
                            callback(this.timeInMillis)
                        },
                        this.get(Calendar.HOUR_OF_DAY),
                        this.get(Calendar.MINUTE),
                        false
                    ).show()
                },
                this.get(Calendar.YEAR),
                this.get(Calendar.MONTH),
                this.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
//        var calendar = Calendar.getInstance()
//        val sharedPref: SharedPreferences = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!
//        var hour = calendar.get(Calendar.HOUR_OF_DAY)
//        var minute = calendar.get(Calendar.MINUTE)
//        val timePickerDialog = TimePickerDialog(context, object : TimePickerDialog.OnTimeSetListener {
//            override fun onTimeSet(timepicker: TimePicker?, hourSelected: Int, minuteSelected: Int) {
//                calendar.set(Calendar.HOUR_OF_DAY, hourSelected)
//                calendar.set(Calendar.MINUTE, minuteSelected)
//                var editor: SharedPreferences.Editor = sharedPref.edit()
//                editor.putInt("hours", hourSelected)
//                editor.putInt("minutes", minuteSelected)
//                Log.d(PREFS_NAME,"Hour " + hourSelected + " Minute " + minuteSelected)
//                editor.commit()
//            }
//        }, hour, minute, false)
////
//        timePickerDialog.show()
//        if (!mNotified) {
//            NotificationUtils().setNotification(mNotification, context as Activity)
//        }
    }

    }


