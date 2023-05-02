package com.example.a7minutesworkout


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.a7minutesworkout.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.frameStart?.setOnClickListener {
            startActivity(Intent(this, ExerciseActivity::class.java))
        }

        val menu = binding?.bottomNavigationView?.menu
        val findItem = menu?.findItem(R.id.navigation_home)
        val menuItem = R.id.navigation_home
        binding?.bottomNavigationView?.selectedItemId = menuItem
        findItem?.setIcon(R.drawable.bottom_nav_home_filled_24)
        binding?.bottomNavigationView?.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    // Handle home icon press
                    true
                }
                R.id.navigation_bmi -> {
                    // Handle search icon press
                    val intent = Intent(this@MainActivity, CalculateBMIActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0,0)
                    finish()
                    true
                }
                R.id.navigation_history -> {
                    // Handle notifications icon press
                    val intent = Intent(this@MainActivity, ExerciseHistoryActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0,0)
                    finish()
                    true
                }
                R.id.navigation_settings -> {
                    // Handle profile icon press
                    val intent = Intent(this@MainActivity, SettingActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0,0)
                    finish()
                    true
                }
                else -> false
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}