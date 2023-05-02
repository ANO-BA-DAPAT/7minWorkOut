package com.example.a7minutesworkout

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.a7minutesworkout.databinding.ActivityCalculateBmiactivityBinding
import com.example.a7minutesworkout.databinding.CustomBmiDialogBinding
import java.math.BigDecimal
import java.math.RoundingMode

class CalculateBMIActivity : AppCompatActivity() {
    private var binding: ActivityCalculateBmiactivityBinding? = null
    companion object {
        private const val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW" // Metric Unit View
        private const val US_UNITS_VIEW = "US_UNIT_VIEW" // US Unit View
    }

    private var currentVisibleView: String =
        METRIC_UNITS_VIEW // A variable to hold a value to make a selected view visible

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculateBmiactivityBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        // Activate Back Button in Header
        setSupportActionBar(binding?.toolbarExerciseBMI)
        if(supportActionBar != null){
            supportActionBar?.title = "CALCULATE BMI"
        }
        makeVisibleMetrics()
        binding?.rgBMI?.setOnCheckedChangeListener { _, checkedId: Int ->
            if(checkedId == R.id.rgBMIMetrics){
                makeVisibleMetrics()
            }else{
                makeVisibleUS()
            }
        }

        binding?.btnCalculate?.setOnClickListener {
            if(currentVisibleView == METRIC_UNITS_VIEW){
                if(validateMetrics()){
                    val height: Float = binding?.etMetricsHeight?.text.toString().toFloat() / 100
                    val weight: Float = binding?.etMetricsWeight?.text.toString().toFloat()
                    val bmi: Float = weight / (height*height)
                    customBMIDialog(bmi)
                }else{
                    Toast.makeText(this, "Please fill up the empty field first!", Toast.LENGTH_SHORT).show()
                }
            }else{
                if(validateUS()){
                    val usWeight: String = binding?.etUSWeight?.text.toString()
                    val usFeet: String = binding?.etFeet?.text.toString()
                    val usInch: String = binding?.etInch?.text.toString()

                    val usHeightValue: Float = usInch.toFloat() + usFeet.toFloat() * 12

                    val bmi = 703 * (usWeight.toFloat() / (usHeightValue * usHeightValue))
                    customBMIDialog(bmi)

                }else{
                    Toast.makeText(this, "Please fill up the empty field first!", Toast.LENGTH_SHORT).show()
                }
            }

        }
        val menu = binding?.bottomNavigationViewBmi?.menu
        val findItem = menu?.findItem(R.id.navigation_bmi)
        val menuItem = R.id.navigation_bmi
        binding?.bottomNavigationViewBmi?.selectedItemId = menuItem
        findItem?.setIcon(R.drawable.bottom_nav_bmi_filled_24)

        binding?.bottomNavigationViewBmi?.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    // Handle home icon press
                    val intent = Intent(this@CalculateBMIActivity, MainActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0,0)
                    finish()
                    true
                }
                R.id.navigation_bmi -> {
                    // Handle search icon press
                    true
                }
                R.id.navigation_history -> {
                    // Handle notifications icon press
                    val intent = Intent(this@CalculateBMIActivity, ExerciseHistoryActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0,0)
                    finish()
                    true
                }
                R.id.navigation_settings -> {
                    // Handle profile icon press
                    val intent = Intent(this@CalculateBMIActivity, SettingActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0,0)
                    finish()
                    true
                }
                else -> false
            }

        }

    }

    private fun customBMIDialog(bmi:Float) {
        var bmiLabel: String
        var bmiDescription: String

        val customBMIDialog = Dialog(this)
        val dialogBinding = CustomBmiDialogBinding.inflate(layoutInflater)
        customBMIDialog.setContentView(dialogBinding.root)
        customBMIDialog.setCancelable(false)

        if(bmi.compareTo(15) <= 0 ){
            bmiLabel = "Severely Underweight"
            bmiDescription = "It would be much better if take care of yourself by eating more nutritious foods"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0) {
            bmiLabel = "Severely underweight"
            bmiDescription = "It would be much better if take care of yourself by eating more nutritious foods"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0) {
            bmiLabel = "Underweight"
            bmiDescription = "It would be much better if take care of yourself by eating more nutritious foods"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in good shape, continue living healthy lifestyle!"
        } else if (java.lang.Float.compare(bmi, 25f) > 0 && java.lang.Float.compare(bmi, 30f) <= 0) {
            bmiLabel = "Overweight"
            bmiDescription = "It would be much better for you if take care of yourself by doing more workout and a better diet!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0
        ) {
            bmiLabel = "Moderately Obese"
            bmiDescription = "It would be much better for you if take care of yourself by doing more workout and a better diet!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0
        ) {
            bmiLabel = "Severely Obese Class"
            bmiDescription = "It would be much better for you if take care of yourself by doing more workout and a better diet!"
        } else {
            bmiLabel = "Very Severely Obese Class"
            bmiDescription = "It would be much better for you if take care of yourself by doing more workout and a better diet!"
        }

        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()
        dialogBinding.dialogBMIResult.text = bmiValue
        dialogBinding.dialogBMICategoryResult.text = bmiLabel
        dialogBinding.dialogMessage.text = bmiDescription

            dialogBinding.dialogBtn.setOnClickListener {
                customBMIDialog.dismiss()
            }
            customBMIDialog.show()

    }

    // Check if fields are not empty in Standard metrics
    private fun validateMetrics(): Boolean{
        var flag = true
        if(binding?.etMetricsHeight?.text.toString().isEmpty()){
            flag = false
        }else if(binding?.etMetricsWeight?.text.toString().isEmpty()){
            flag = false
        }
        return flag
    }
    // Check if fields are not empty in US metrics
    private fun validateUS(): Boolean{
        var flag = true
        if(binding?.etUSWeight?.text.toString().isEmpty()){
            flag = false
        }else if(binding?.etFeet?.text.toString().isEmpty()){
            flag = false
        }else if(binding?.etInch?.text.toString().isEmpty()){
            flag = false
        }
        return flag
    }
    // Make the fields visible
    private fun makeVisibleMetrics(){
        currentVisibleView = METRIC_UNITS_VIEW
        binding?.etMetricsWeight?.visibility = View.VISIBLE
        binding?.etMetricsHeight?.visibility = View.VISIBLE
        binding?.llUSWeight?.visibility = View.INVISIBLE

        binding?.etMetricsHeight?.text!!.clear()
        binding?.etMetricsWeight?.text!!.clear()

    }
    private fun makeVisibleUS(){
        currentVisibleView = US_UNITS_VIEW
        binding?.etMetricsWeight?.visibility = View.INVISIBLE
        binding?.etMetricsHeight?.visibility = View.INVISIBLE
        binding?.llUSWeight?.visibility = View.VISIBLE

        binding?.etUSWeight?.text!!.clear()
        binding?.etFeet?.text!!.clear()
        binding?.etInch?.text!!.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}