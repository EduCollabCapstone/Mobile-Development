import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.educollab1.databinding.ItemInsideScheduleBinding
import com.capstone.educollab1.local.ScheduleEntity

class InsideScheduleAdapter(
    private val onEditClick: (ScheduleEntity) -> Unit,
    private val onDeleteClick: (ScheduleEntity) -> Unit
) : ListAdapter<ScheduleEntity, InsideScheduleAdapter.InsideScheduleViewHolder>(ScheduleDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InsideScheduleViewHolder {
        val binding = ItemInsideScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InsideScheduleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InsideScheduleViewHolder, position: Int) {
        val schedule = getItem(position)
        holder.bind(schedule)
    }

    inner class InsideScheduleViewHolder(private val binding: ItemInsideScheduleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(schedule: ScheduleEntity) {
            binding.subjectNameText.text = schedule.subject
            binding.timeRangeText.text = schedule.time

            binding.editButton.setOnClickListener {
                onEditClick(schedule)
            }

            binding.deleteButton.setOnClickListener {
                onDeleteClick(schedule)
            }
        }
    }

    class ScheduleDiffCallback : DiffUtil.ItemCallback<ScheduleEntity>() {
        override fun areItemsTheSame(oldItem: ScheduleEntity, newItem: ScheduleEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ScheduleEntity, newItem: ScheduleEntity): Boolean {
            return oldItem == newItem
        }
    }
}
