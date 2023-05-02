package com.example.a7minutesworkout

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minutesworkout.SettingActivity.Companion.SETTING_PREFERENCES
import com.example.a7minutesworkout.SettingActivity.Companion.TEXT_TO_SPEECH_STATUS
import com.example.a7minutesworkout.databinding.ActivityExerciseBinding
import com.example.a7minutesworkout.databinding.CustomDialogBackButtonBinding
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var binding: ActivityExerciseBinding? = null
    // Rest View Settings
    private var restTimer: CountDownTimer? = null
    private var restProgress: Int = 0
    // Exercise View Settings
    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress: Int = 0

    private var exerciseList: ArrayList<ExerciseModel>? = null // Exercise List
    private var exerciseCurrPosition = -1 // Exercise Position in the List
    private var isStart: Boolean = true // Get Ready or Rest Time Text Boolean

    private var textSpeech: TextToSpeech? = null // Text to Speech

    private var exerciseAdapter: ExerciseStatusAdapter? = null // Recycler View Adapter
    var getStatus: Boolean? = null // Shared Preferences Settings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        // Shared Preference from settings
        val sharedPref by lazy { applicationContext.getSharedPreferences(SETTING_PREFERENCES, 0) }
        getStatus = sharedPref.getBoolean(TEXT_TO_SPEECH_STATUS, false)
        // Toolbar or App Bar
        setSupportActionBar(binding?.toolbarExercise)
        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.toolbarExercise?.setNavigationOnClickListener {
            customBackDialog()
        }
        textSpeech = TextToSpeech(this, this)
        exerciseList = Constants.defaultExerciseList() // Get the list of exercise from the Exercise Constant Model
        // Set the first View and the Recycler View
        setRestView()
        setupExerciseStatusAdapter()

    }
    // Customizing a dialog when back is pressed
    override fun onBackPressed() {
        customBackDialog()
    }
    // Custom dialog function
    private fun customBackDialog(){
        val customDialog = Dialog(this)
        val dialogBinding = CustomDialogBackButtonBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCancelable(false)

        dialogBinding.btnYes.setOnClickListener {
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }
        dialogBinding.btnNo.setOnClickListener {
            customDialog.dismiss()
        }

        customDialog.show()
    }
    // Rest view that will be set after exercise view
    private fun setRestView(){
        binding?.frameExerciseTimer?.visibility = View.INVISIBLE
        binding?.tvExerciseTitle?.visibility = View.INVISIBLE
        binding?.exerciseImage?.visibility = View.INVISIBLE
        binding?.frameRestTimer?.visibility = View.VISIBLE
        binding?.tvRestTitle?.visibility = View.VISIBLE
        binding?.tvUpcomingExercise?.visibility = View.VISIBLE
        binding?.tvUpcomingTitle?.visibility = View.VISIBLE
        if(restTimer!=null){
            restTimer?.cancel()
            restProgress = 0
        }
        binding?.tvUpcomingExercise?.text = exerciseList!![exerciseCurrPosition+1].getName()
        if(getStatus == true){
            speakOut("Cooldown for 10 seconds, Upcoming Exercise, ".plus(binding?.tvUpcomingExercise?.text.toString()))
        }
        setRestProgress()


    }
    // Rest view timer function
    private fun setRestProgress() {
        if(isStart){
            binding?.tvRestTitle?.text = "GET READY FOR"
            isStart = false
        }else{
            binding?.tvRestTitle?.text = "REST TIME"
        }
        var  countRestTime= 10
        restTimer = object : CountDownTimer(10000, 1000){
             override fun onTick(millisSecond: Long) {
                 restProgress++
                 binding?.progressBarRest?.progress = 10 - restProgress
                 binding?.tvRestTimer?.text = (10 - restProgress).toString()
                 countRestTime--
                 if(countRestTime >=0 && countRestTime<=3 && getStatus== true){
                         speakOut(countRestTime.toString())

                 }
             }
             override fun onFinish() {
                 exerciseCurrPosition++
                 exerciseList!![exerciseCurrPosition].setIsSelected(true)
                 exerciseAdapter!!.notifyDataSetChanged()
                 setExerciseView()
             }
         }.start()
    }
    // Exercise view that will be set after rest view
    private fun setExerciseView(){
        binding?.frameRestTimer?.visibility = View.INVISIBLE
        binding?.tvRestTitle?.visibility = View.INVISIBLE
        binding?.tvUpcomingExercise?.visibility = View.INVISIBLE
        binding?.tvUpcomingTitle?.visibility = View.INVISIBLE
        binding?.frameExerciseTimer?.visibility = View.VISIBLE
        binding?.tvExerciseTitle?.visibility = View.VISIBLE
        binding?.exerciseImage?.visibility = View.VISIBLE
        if(exerciseTimer!=null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }
        binding?.tvExerciseTitle?.text = exerciseList!![exerciseCurrPosition].getName()
        binding?.exerciseImage?.setImageResource(exerciseList!![exerciseCurrPosition].getImage())
        if(getStatus== true){
            val speakExerciseTimer = binding?.tvExerciseTitle?.text.toString().plus("in 30 Seconds")
            speakOut(speakExerciseTimer)
        }
        setExerciseTimer()
    }
    // Exercise view timer
    private fun setExerciseTimer() {
        var countExerciseTime = 30
        exerciseTimer = object : CountDownTimer(30000, 1000){
            override fun onTick(millisSecond: Long) {
                exerciseProgress++
                binding?.progressBaExercise?.progress = 30 - exerciseProgress
                binding?.tvExerciseTimer?.text = (30 - exerciseProgress).toString()
                countExerciseTime--
                if(countExerciseTime >=0 && countExerciseTime<=5 && getStatus== true){
                    speakOut(countExerciseTime.toString())
                }
            }
            override fun onFinish() {
                exerciseList!![exerciseCurrPosition].setIsSelected(false)
                exerciseList!![exerciseCurrPosition].setIsCompleted(true)
                exerciseAdapter!!.notifyDataSetChanged()
                if(exerciseCurrPosition != exerciseList!!.size-1){
                    setRestView()
                }else{
                    startActivity(Intent(applicationContext, FinishedExerciseActivity::class.java))
                }
            }
        }.start()
    }
    // Initializing Text to Speech
    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            val result = textSpeech!!.setLanguage(Locale.US)

            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("Language Error Message","Language is not supported!")
            }
        }else{
            Log.e("Language Initialization","Language initialization failed")
        }
    }
    // Pass a string that will be red by machine
    private fun speakOut(text: String){
        textSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }
    // Setup recylcerView Adapter
    private fun setupExerciseStatusAdapter(){
        binding?.rvExcerciseStatus?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        exerciseAdapter = ExerciseStatusAdapter(this, exerciseList!!)
        binding?.rvExcerciseStatus?.adapter = exerciseAdapter
    }


    override fun onDestroy() {
        super.onDestroy()
        if(restTimer!=null){
            restTimer?.cancel()
            restProgress = 0
        }
        if(exerciseTimer!=null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }
        if(textSpeech!=null){
            textSpeech?.stop()
            textSpeech?.shutdown()
        }
        binding = null
    }

}