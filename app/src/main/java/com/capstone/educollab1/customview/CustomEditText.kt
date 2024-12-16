package com.capstone.educollab1.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.capstone.educollab1.R

class CustomEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private var defaultBackground: Drawable? = null

    init {
        defaultBackground = ContextCompat.getDrawable(context, R.drawable.ic_edt)
        setPadding(24, 16, 24, 16)
        background = defaultBackground ?: ContextCompat.getDrawable(context, android.R.drawable.edit_text)

        // Tambahan kustomisasi
        isSingleLine = true
        ellipsize = TextUtils.TruncateAt.END
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        setTextColor(ContextCompat.getColor(context, R.color.white))
        setHintTextColor(ContextCompat.getColor(context, R.color.white))
    }
}
