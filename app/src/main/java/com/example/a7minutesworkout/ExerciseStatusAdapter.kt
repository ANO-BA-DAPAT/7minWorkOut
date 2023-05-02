package com.example.a7minutesworkout

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.a7minutesworkout.databinding.ItemExerciseStatusBinding

class ExerciseStatusAdapter(val mContext: Context, val items: ArrayList<ExerciseModel>):
    RecyclerView.Adapter<ExerciseStatusAdapter.ViewHolder>() {
        // Access the TextView Layout using ViewBinding
        inner class ViewHolder(binding: ItemExerciseStatusBinding):
            RecyclerView.ViewHolder(binding.root){
            val itemTv = binding.tvItem
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemExerciseStatusBinding.inflate(LayoutInflater.from(parent.context), parent, false ))
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: ExerciseModel = items[position]
        holder.itemTv.text = model.getId().toString()

        when{
            model.getIsSelected() -> {
                holder.itemTv.background = ContextCompat.getDrawable(mContext, R.drawable.item_circular_background_selected)
                holder.itemTv.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            }
            model.getIsCompleted() -> {
                holder.itemTv.background = ContextCompat.getDrawable(mContext, R.drawable.circular_timer_background)
                holder.itemTv.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            }else -> {
                holder.itemTv.background = ContextCompat.getDrawable(mContext, R.drawable.item_circular_background)
            }
        }
    }
    override fun getItemCount(): Int {
      return items.size
    }


}