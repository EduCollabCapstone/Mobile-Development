package com.capstone.educollab1.ui.schedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.educollab1.R
import com.capstone.educollab1.local.ScheduleEntity

class ScheduleAdapter(private val schedules: List<ScheduleEntity>) : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_schedule, parent, false)
        return ScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val schedule = schedules[position]
        holder.bind(schedule)
    }

    override fun getItemCount(): Int = schedules.size

    class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val subjectTextView: TextView = itemView.findViewById(R.id.subject_text_view)
        private val timeTextView: TextView = itemView.findViewById(R.id.time_text_view)

        fun bind(schedule: ScheduleEntity) {
            subjectTextView.text = schedule.subject
            timeTextView.text = schedule.time
        }
    }
}


