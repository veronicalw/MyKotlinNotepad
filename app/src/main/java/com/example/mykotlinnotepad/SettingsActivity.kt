package com.example.mykotlinnotepad

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import com.example.mykotlinnotepad.alarmmanager.AlarmService
import com.example.mykotlinnotepad.preferences.SettingsFragment
import java.util.*

class SettingsActivity : AppCompatActivity() {
    private lateinit var frameLayout: FrameLayout
    private lateinit var switchButton: Button

    lateinit var alarmService: AlarmService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.title = "Settings"

//        switchButton = findViewById(R.id.switchAlarm)
        alarmService = AlarmService(this)

//        switchButton.setOnClickListener{
//            setAlarm { alarmService.setRepetitiveAlarm(it) }
//        }
//        switchButton.setOnCheckedChangeListener { compoundButton, b ->
//            setAlarm { alarmService.setRepetitionAlarm() }
//            setRepetitive.setOnClickListener { setAlarm { alarmService.setRepetitiveAlarm(it) } }
//        }
        frameLayout = findViewById(R.id.frameLayoutPref)
//
        supportFragmentManager.beginTransaction().replace(android.R.id.content, SettingsFragment()).commit()

    }
    private fun setAlarm(callback: (Long) -> Unit) {
        Calendar.getInstance().apply {
            this.set(Calendar.SECOND, 0)
            this.set(Calendar.MILLISECOND, 0)
            DatePickerDialog(
                this@SettingsActivity,
                0,
                { _, year, month, day ->
                    this.set(Calendar.YEAR, year)
                    this.set(Calendar.MONTH, month)
                    this.set(Calendar.DAY_OF_MONTH, day)
                    TimePickerDialog(
                        this@SettingsActivity,
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
    }
}
