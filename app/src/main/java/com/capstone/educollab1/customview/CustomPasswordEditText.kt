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
    private var isError: Boolean = false
    private var eyeIcon: Drawable? = null
    private var defaultBackground: Drawable? = null

    init {
        // Setup background dan padding
        defaultBackground = ContextCompat.getDrawable(context, R.drawable.ic_edt)
        setPadding(32, 20, 48, 20)
        background = defaultBackground ?: ContextCompat.getDrawable(context, android.R.drawable.edit_text)

        // Setup password visibility icon
        eyeIcon = ContextCompat.getDrawable(context, R.drawable.ic_eye)
        transformationMethod = PasswordTransformationMethod.getInstance()
        updatePasswordVisibilityIcon()

        // Kustomisasi tampilan
        isSingleLine = true
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        textSize = 16f

        // Animasi interaksi
        setOnFocusChangeListener { _, hasFocus ->
            alpha = if (hasFocus) 1f else 0.9f
            scaleX = if (hasFocus) 1.01f else 1f
            scaleY = if (hasFocus) 1.01f else 1f

            // Ubah warna background saat fokus
            background?.setTint(
                ContextCompat.getColor(context,
                    if (hasFocus) R.color.a64CCC5
                    else R.color.white
                )
            )
        }

        // Password validation
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validatePassword(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Password visibility toggle
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
        // Warna teks yang selaras
        setTextColor(ContextCompat.getColor(context, R.color.a04364A))
        setHintTextColor(ContextCompat.getColor(context, R.color.a64CCC5))

        // Tambahan styling
        background?.alpha = 230
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        transformationMethod = if (isPasswordVisible) null else PasswordTransformationMethod.getInstance()
        updatePasswordVisibilityIcon()
        setSelection(text?.length ?: 0)
    }

    private fun updatePasswordVisibilityIcon() {
        eyeIcon?.setBounds(0, 0, 40, 40)
        setCompoundDrawablesRelative(null, null, eyeIcon, null)
    }

    private fun validatePassword(input: String) {
        isError = if (input.length < 8) {
            error = context.getString(R.string.password_length)
            true
        } else {
            error = null
            false
        }
    }
}
