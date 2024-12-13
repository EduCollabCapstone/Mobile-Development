package com.capstone.educollab1.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.educollab1.R

class DashboardAdapter(
    private val data: List<Subject>,
    private val onEdit: (Subject, Int) -> Unit
) : RecyclerView.Adapter<DashboardAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvLabel: TextView = itemView.findViewById(R.id.tvLabel)
        val etValue: EditText = itemView.findViewById(R.id.etValue)

        fun bind(item: Subject) {
            tvLabel.text = item.name
            etValue.setText(item.score.toString())

            etValue.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    val newValue = etValue.text.toString().toIntOrNull()
                    if (newValue != null) {
                        onEdit(item, newValue)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dashboard, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
}
