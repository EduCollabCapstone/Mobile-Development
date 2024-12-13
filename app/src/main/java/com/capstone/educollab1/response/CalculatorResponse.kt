package com.capstone.educollab1.response

data class CalculatorItem(
    val label: String,
    var value: String = "",
    val isResult: Boolean = false // True untuk "Performa Indeks"
)