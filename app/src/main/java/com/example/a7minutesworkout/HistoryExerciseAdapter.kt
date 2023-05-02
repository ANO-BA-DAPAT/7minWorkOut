package com.example.a7minutesworkout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.a7minutesworkout.databinding.HistoryExerciseBinding

class HistoryExerciseAdapter(private val historyList: ArrayList<HistoryEntity>):RecyclerView.Adapter<HistoryExerciseAdapter.ViewHolder>() {

    inner class ViewHolder(binding:HistoryExerciseBinding): RecyclerView.ViewHolder(binding.root){
        val parent = binding.llparent
        val historyId = binding.tvHistoryId
        val historyDate = binding.tvHistoryDate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(HistoryExerciseBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.historyId.text = historyList[position].historyId.toString()
        holder.historyDate.text = historyList[position].historyDate

        if(position % 2 == 0){
            holder.parent.setBackgroundColor(ContextCompat.getColor(context, R.color.lightGrey))
        }else{
            holder.parent.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }
    }

    override fun getItemCount(): Int {
        return historyList.size
    }
}