package com.example.a7minutesworkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minutesworkout.databinding.ActivityFinishedExerciseBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class FinishedExerciseActivity : AppCompatActivity() {
    private var binding: ActivityFinishedExerciseBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishedExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.finishedBtn?.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        val dao = (application as WorkOutApp).db.historyDao()
        addDateToDatabase(dao)



    }

    private fun addDateToDatabase(historyDao: HistoryDao){

        val calendar = Calendar.getInstance()
        val dateTime = calendar.time
        val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy hh:mm:a", Locale.getDefault())
        val date = simpleDateFormat.format(dateTime)
        lifecycleScope.launch{
            historyDao.insert(HistoryEntity(0,date))
        }
    }
}