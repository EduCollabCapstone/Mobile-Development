package com.capstone.educollab1.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.capstone.educollab1.R

@SuppressLint("ClickableViewAccessibility")
class CustomPasswordEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private var isPasswordVisible = false
    private var eyeIcon: Drawable? = null
    private var defaultBackground: Drawable? = null

    init {
        defaultBackground = ContextCompat.getDrawable(context, R.drawable.ic_edt)
        setPadding(24, 16, 24, 16)
        background = defaultBackground ?: ContextCompat.getDrawable(context, android.R.drawable.edit_text)

        eyeIcon = ContextCompat.getDrawable(context, R.drawable.ic_eye)
        transformationMethod = PasswordTransformationMethod.getInstance()
        updatePasswordVisibilityIcon()

        isSingleLine = true
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        textSize = 16f

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validatePassword(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })


        setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = compoundDrawablesRelative[2]
                if (drawableEnd != null && event.x >= (width - paddingRight - drawableEnd.bounds.width())) {
                    togglePasswordVisibility()
                    return@setOnTouchListener true
                }
            }
            false
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        setTextColor(ContextCompat.getColor(context, R.color.white))
        setHintTextColor(ContextCompat.getColor(context, R.color.white))
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        transformationMethod = if (isPasswordVisible) null else PasswordTransformationMethod.getInstance()
        updatePasswordVisibilityIcon()
        setSelection(text?.length ?: 0)
    }

    private fun updatePasswordVisibilityIcon() {
        eyeIcon?.let {
            it.setBounds(0, 0, 40, 40)
            setCompoundDrawablesRelative(null, null, it, null)
        }
    }

    private fun validatePassword(input: String) {
        error = if (input.length < 8) {
            context.getString(R.string.password_length)
        } else {
            null
        }
    }
}
