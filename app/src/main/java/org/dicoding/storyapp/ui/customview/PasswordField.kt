package org.dicoding.storyapp.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import org.dicoding.storyapp.R

class PasswordField: AppCompatEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init(){
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val password = text.toString()
                when{
                    password.length < 8  -> error = context.getString(R.string.error_length_password)
                    password.isEmpty() -> error = context.getString(R.string.error_empty_password)
                }
            }
            override fun afterTextChanged(s: Editable) {
                val password = text.toString()
                when{
                    password.length < 8  -> error = context.getString(R.string.error_length_password)
                    password.isEmpty() -> error = context.getString(R.string.error_empty_password)
                }
            }
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint =  context.getString(R.string.enter_pass)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

}