package com.capstone.educollab1.ui.calculator

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.educollab1.R
import com.capstone.educollab1.response.CalculatorItem

class CalculatorAdapter(private val items: List<CalculatorItem>) : RecyclerView.Adapter<CalculatorAdapter.FormViewHolder>() {

    inner class FormViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val label: TextView = view.findViewById(R.id.tvLabel)
        val input: EditText = view.findViewById(R.id.etValue)
        val result: TextView = view.findViewById(R.id.tvResult)
        val dropdown: Spinner = view.findViewById(R.id.spDropdown)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calculator, parent, false)
        return FormViewHolder(view)
    }

    override fun onBindViewHolder(holder: FormViewHolder, position: Int) {
        val item = items[position]
        holder.label.text = item.label

        if (item.isResult) {
            holder.result.visibility = View.VISIBLE
            holder.input.visibility = View.GONE
            holder.dropdown.visibility = View.GONE
            holder.result.text = item.value
        } else if (item.label == "Aktivitas Ektrakurikuler") {
            holder.result.visibility = View.GONE
            holder.input.visibility = View.GONE
            holder.dropdown.visibility = View.VISIBLE

            val options = listOf("Yes", "No")
            val adapter = ArrayAdapter(
                holder.itemView.context,
                android.R.layout.simple_spinner_dropdown_item,
                options
            )
            holder.dropdown.adapter = adapter

            holder.dropdown.setSelection(options.indexOf(item.value))
            holder.dropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    item.value = options[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        } else {
            holder.result.visibility = View.GONE
            holder.input.visibility = View.VISIBLE
            holder.dropdown.visibility = View.GONE
            holder.input.setText(item.value)

            holder.input.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    item.value = s.toString()
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }
    }

    override fun getItemCount(): Int = items.size
}
