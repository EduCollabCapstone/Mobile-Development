package com.capstone.educollab1.ui.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.educollab1.databinding.ItemScheduleBinding
import com.capstone.educollab1.ui.remote.ScheduleResponse

class ScheduleAdapter(
    private val onItemClick: (ScheduleResponse) -> Unit
) : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    private val scheduleList = mutableListOf<ScheduleResponse>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val binding = ItemScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScheduleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val schedule = scheduleList[position]
        holder.bind(schedule)
    }

    override fun getItemCount(): Int = scheduleList.size

    // Mengupdate list data
    fun submitList(newList: List<ScheduleResponse>) {
        scheduleList.clear()
        scheduleList.addAll(newList)
        notifyDataSetChanged()
    }

    // ViewHolder untuk item Schedule (item_schedule.xml)
    inner class ScheduleViewHolder(private val binding: ItemScheduleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(schedule: ScheduleResponse) {
            binding.dayTextView.text = schedule.day
            binding.subjectTextView.text = schedule.subject
            binding.timeTextView.text = schedule.period

            itemView.setOnClickListener {
                onItemClick(schedule)
            }
        }
    }
}

