package com.example.a7minutesworkout

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minutesworkout.databinding.ActivityExerciseHistoryBinding
import kotlinx.coroutines.launch

class ExerciseHistoryActivity : AppCompatActivity() {
    private var binding: ActivityExerciseHistoryBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExerciseHistory)
        if(supportActionBar != null){
            supportActionBar?.title = "HISTORY"
        }
        val dao = (application as WorkOutApp).db.historyDao()
        lifecycleScope.launch {
            dao.fetAllDates().collect{
                val list = ArrayList(it)
                getAllCompleteDates(list, dao)
            }
        }
        // Bottom Nav Bar if this activity is selected
        val menu = binding?.bottomNavigationViewHistory?.menu
        val findItem = menu?.findItem(R.id.navigation_history)
        val menuItem = R.id.navigation_history
        binding?.bottomNavigationViewHistory?.selectedItemId = menuItem
        findItem?.setIcon(R.drawable.bottom_nav_history_filled_24)

        binding?.bottomNavigationViewHistory?.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    // Handle home icon press
                    val intent = Intent(this@ExerciseHistoryActivity, MainActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0,0)
                    finish()
                    true
                }
                R.id.navigation_bmi -> {
                    // Handle search icon press
                    val intent = Intent(this@ExerciseHistoryActivity, CalculateBMIActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0,0)
                    finish()
                    true
                }
                R.id.navigation_history -> {
                    // Handle notifications icon press
                    true
                }
                R.id.navigation_settings -> {
                    // Handle profile icon press
                    val intent = Intent(this@ExerciseHistoryActivity, SettingActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0,0)
                    finish()
                    true
                }
                else -> false
            }

        }
    }
    private fun getAllCompleteDates(historyList: ArrayList<HistoryEntity>, historyDao: HistoryDao){
        val historyAdapter = HistoryExerciseAdapter(historyList)
        if(historyList.isNotEmpty()){
            lifecycleScope.launch{
                historyDao.fetAllDates().collect{ allCompletedDates ->
                    for (count in allCompletedDates){
                        Log.e("Date: ","" +count.historyDate)
                    }
                }
            }
            binding?.rvHistoryRecords?.layoutManager = LinearLayoutManager(this)
            binding?.rvHistoryRecords?.adapter = historyAdapter
            binding?.rvHistoryRecords?.visibility = View.VISIBLE
            binding?.tvHistoryRecordsChecker?.visibility = View.GONE
        }else{
            binding?.rvHistoryRecords?.visibility = View.GONE
            binding?.tvHistoryRecordsChecker?.visibility = View.VISIBLE
        }

    }
}