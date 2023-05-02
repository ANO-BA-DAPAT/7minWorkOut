package com.example.a7minutesworkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.a7minutesworkout.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {
    private var binding: ActivitySettingBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val sharedPref by lazy { applicationContext.getSharedPreferences(SETTING_PREFERENCES, 0) }

        val getStatus = sharedPref.getBoolean(TEXT_TO_SPEECH_STATUS, false)

        if (getStatus) {
            binding?.ttsSwitch?.isChecked = true
        }
        binding?.ttsSwitch?.setOnClickListener {
            if (getStatus) {
                sharedPref.edit().putBoolean(TEXT_TO_SPEECH_STATUS, false).apply()
            } else {
                sharedPref.edit().putBoolean(TEXT_TO_SPEECH_STATUS, true).apply()
            }

        }

        val menu = binding?.bottomNavigationViewSetting?.menu
        val findItem = menu?.findItem(R.id.navigation_settings)
        val menuItem = R.id.navigation_settings
        binding?.bottomNavigationViewSetting?.selectedItemId = menuItem
        findItem?.setIcon(R.drawable.bottom_nav_settings_filled_24)
        binding?.bottomNavigationViewSetting?.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    // Handle home icon press
                    val intent = Intent(this@SettingActivity, MainActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.navigation_bmi -> {
                    // Handle search icon press
                    val intent = Intent(this@SettingActivity, CalculateBMIActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.navigation_history -> {
                    // Handle notifications icon press
                    val intent = Intent(this@SettingActivity, ExerciseHistoryActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.navigation_settings -> {
                    // Handle profile icon press
                    true
                }
                else -> false
            }

        }
    }

    companion object {
        var SETTING_PREFERENCES = "SETTING_PREFERENCES"
        var TEXT_TO_SPEECH_STATUS = "TEXT_TO_SPEECH_STATUS"
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}