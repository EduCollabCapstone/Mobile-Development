package com.capstone.educollab1.ui.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.educollab1.databinding.ItemInsideScheduleBinding
import com.capstone.educollab1.ui.remote.ScheduleResponse

class InsideScheduleAdapter(
    private val onEditClick: (ScheduleResponse) -> Unit,
    private val onDeleteClick: (ScheduleResponse) -> Unit
) : ListAdapter<ScheduleResponse, InsideScheduleAdapter.InsideScheduleViewHolder>(ScheduleDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InsideScheduleViewHolder {
        // Inflate the layout for each item using ViewBinding
        val binding = ItemInsideScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InsideScheduleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InsideScheduleViewHolder, position: Int) {
        val schedule = getItem(position) // Use getItem to get the schedule at the given position
        holder.bind(schedule)
    }

    // ViewHolder for each individual schedule item
    inner class InsideScheduleViewHolder(private val binding: ItemInsideScheduleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(schedule: ScheduleResponse) {
            // Bind the data to the views
            binding.subjectNameText.text = schedule.subject
            binding.timeRangeText.text = schedule.period

            // Set click listeners for Edit and Delete buttons
            binding.editButton.setOnClickListener {
                onEditClick(schedule)
            }

            binding.deleteButton.setOnClickListener {
                onDeleteClick(schedule)
            }
        }
    }

    // DiffUtil.Callback to optimize item updates in the list
    class ScheduleDiffCallback : DiffUtil.ItemCallback<ScheduleResponse>() {
        // Check if items are the same based on scheduleId
        override fun areItemsTheSame(oldItem: ScheduleResponse, newItem: ScheduleResponse): Boolean {
            return oldItem.scheduleId == newItem.scheduleId
        }

        // Check if the content of the items are the same
        override fun areContentsTheSame(oldItem: ScheduleResponse, newItem: ScheduleResponse): Boolean {
            return oldItem == newItem
        }
    }
}
