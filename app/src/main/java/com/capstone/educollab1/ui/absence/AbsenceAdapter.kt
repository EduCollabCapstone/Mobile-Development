package com.capstone.educollab1.ui.absence

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.educollab1.R
import com.capstone.educollab1.ui.remote.ResponseAbsencesItem

class AbsenceAdapter(private val absenceList: List<ResponseAbsencesItem?>?): RecyclerView.Adapter<AbsenceAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUsername: TextView = itemView.findViewById(R.id.tv_username)
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
        val tvDate: TextView = itemView.findViewById(R.id.tv_date)
        val tvStatus: TextView = itemView.findViewById(R.id.tv_status)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_absence, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (date, studentName, jsonMemberClass, status) = absenceList?.get(position) ?: ResponseAbsencesItem()
        holder.tvUsername.text = studentName
        holder.tvName.text = jsonMemberClass
        holder.tvDate.text = date
        holder.tvStatus.text = status
    }

    override fun getItemCount(): Int = absenceList?.size ?: 0
}